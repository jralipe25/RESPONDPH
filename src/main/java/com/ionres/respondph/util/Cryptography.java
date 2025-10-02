package com.ionres.respondph.util;

import com.ionres.respondph.database.DBConnection;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class Cryptography {

    private static final String CONFIG_FILE = "config/Outlet.properties";
    private static final String CONFIG_KEY = "secretKey";

    private static final String CIPHER = "AES/GCM/NoPadding";
    private static final int IV_LEN_BYTES = 12;      // recommended for GCM
    private static final int TAG_LEN_BITS = 128;     // 16-byte tag
    private static final byte VERSION_1 = 0x01;

    private static final SecretKey KEY = loadKeyFromConfig();

    private Cryptography() {
    }

    // -------------------- Public API --------------------
    /**
     * Encrypts a UTF-8 string and returns Base64 payload.
     */
    public static String encrypt(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        byte[] iv = new byte[]{
            (byte) 0xa3, (byte) 0xf9, (byte) 0xc1, (byte) 0xd2,
            (byte) 0xe4, (byte) 0xb5, (byte) 0x87, (byte) 0x6a,
            (byte) 0x9c, (byte) 0x0d, (byte) 0x3e, (byte) 0x2f
        };

        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, KEY, new GCMParameterSpec(TAG_LEN_BITS, iv));
            byte[] ct = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            ByteBuffer bb = ByteBuffer.allocate(1 + IV_LEN_BYTES + ct.length);
            bb.put(VERSION_1).put(iv).put(ct);
            return Base64.getEncoder().encodeToString(bb.array());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("AES-GCM encryption failed", e);
        }
    }

    /**
     * Decrypts a Base64 payload produced by encrypt(...) and returns UTF-8
     * string.
     */
    public static String decrypt(String base64Payload) {
        if (base64Payload == null) {
            return null;
        }
        byte[] payload = Base64.getDecoder().decode(base64Payload);

        if (payload.length < 1 + IV_LEN_BYTES + 16) {
            throw new IllegalArgumentException("Invalid payload length");
        }
        ByteBuffer bb = ByteBuffer.wrap(payload);
        byte version = bb.get();
        if (version != VERSION_1) {
            throw new IllegalArgumentException("Unsupported payload version: " + version);
        }
        byte[] iv = new byte[IV_LEN_BYTES];
        bb.get(iv);
        byte[] ct = new byte[bb.remaining()];
        bb.get(ct);

        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, KEY, new GCMParameterSpec(TAG_LEN_BITS, iv));
            byte[] pt = cipher.doFinal(ct);
            return new String(pt, StandardCharsets.UTF_8);
        } catch (AEADBadTagException e) {
            // Wrong key or tampered/corrupted data
            throw new SecurityException("Ciphertext authentication failed", e);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("AES-GCM decryption failed", e);
        }
    }

    // -------------------- Config/key loading --------------------
    private static SecretKey loadKeyFromConfig() {
        String value = readSecretKeyProperty();
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing 'secretKey' in " + CONFIG_FILE);
        }
        byte[] keyBytes = selectKeyBytes(value.trim());

        int len = keyBytes.length;
        if (len != 16 && len != 24 && len != 32) {
            throw new IllegalStateException(
                    "Invalid AES key length: " + len + " bytes. Use 16/24/32 (128/192/256-bit). "
                    + "Tip: for AES-256 use a 32-character ASCII key or a Base64/hex value that decodes to 32 bytes."
            );
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static String readSecretKeyProperty() {
        // 1) Try classpath
        try (InputStream in = Cryptography.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                return p.getProperty(CONFIG_KEY);
            }
        } catch (Exception ignored) {
        }

        // 2) Try working directory
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            Properties p = new Properties();
            p.load(in);
            return p.getProperty(CONFIG_KEY);
        } catch (Exception e) {
            return null;
        }
    }

    // Heuristic: prefer RAW ASCII/UTF-8 length first (so your 32-char value → 32 bytes → AES-256).
    // If that doesn't fit 16/24/32, try hex, then Base64.
    private static byte[] selectKeyBytes(String v) {
        Objects.requireNonNull(v);
        byte[] raw = v.getBytes(StandardCharsets.UTF_8);
        if (raw.length == 16 || raw.length == 24 || raw.length == 32) {
            return raw;
        }
        if (isHex(v)) {
            byte[] hex = hexDecode(v);
            if (hex.length == 16 || hex.length == 24 || hex.length == 32) {
                return hex;
            }
        }
        try {
            byte[] b64 = Base64.getDecoder().decode(v);
            if (b64.length == 16 || b64.length == 24 || b64.length == 32) {
                return b64;
            }
        } catch (IllegalArgumentException ignored) {
        }
        return raw; // will be validated by caller and likely rejected with a clear message
    }

    private static boolean isHex(String s) {
        int n = s.length();
        if ((n & 1) != 0 || n == 0) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            boolean ok = (c >= '0' && c <= '9')
                    || (c >= 'a' && c <= 'f')
                    || (c >= 'A' && c <= 'F');
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    private static byte[] hexDecode(String s) {
        int len = s.length();
        byte[] out = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int hi = Character.digit(s.charAt(i), 16);
            int lo = Character.digit(s.charAt(i + 1), 16);
            if (hi < 0 || lo < 0) {
                throw new IllegalArgumentException("Invalid hex");
            }
            out[i / 2] = (byte) ((hi << 4) + lo);
        }
        return out;
    }
    

    public static <T> T encryptFields(T obj, List<String> fieldsToEncrypt) {
        if (obj == null || fieldsToEncrypt == null || fieldsToEncrypt.isEmpty()) return obj;

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == String.class && fieldsToEncrypt.contains(field.getName())) {
                field.setAccessible(true);
                try {
                    String raw = (String) field.get(obj);
                    if (raw != null && !raw.isBlank()) {
                        String encrypted = Cryptography.encrypt(raw);
                        field.set(obj, encrypted);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to encrypt field: " + field.getName(), e);
                }
            }
        }
        return obj;
    }

    public static <T> T decryptFields(T obj, List<String> fieldsToDecrypt) {
        if (obj == null || fieldsToDecrypt == null || fieldsToDecrypt.isEmpty()) return obj;

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == String.class && fieldsToDecrypt.contains(field.getName())) {
                field.setAccessible(true);
                try {
                    String encrypted = (String) field.get(obj);
                    if (encrypted != null && !encrypted.isBlank()) {
                        String decrypted = Cryptography.decrypt(encrypted);
                        field.set(obj, decrypted);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decrypt field: " + field.getName(), e);
                }
            }
        }
        return obj;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(DBConnection.getInstance().getConnection().toString());
        System.out.println(Cryptography.encrypt("admin"));
    }
}

//AfZUb10BH6UnhKPj3Wo5lAPr0PLmrnlZ1b6BSzMgTMk3BA==

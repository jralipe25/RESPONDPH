package com.ionres.respondph.util;

import java.lang.reflect.Field;

public final class ObjectEncDec {
    
    private ObjectEncDec() {}

    public static <T> T encryptFields(T obj) {
        if (obj == null) return null;

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == String.class) {
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

    public static <T> T decryptFields(T obj) {
        if (obj == null) return null;

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                try {
                    String encrypted = (String) field.get(obj);
                    if (encrypted != null && !encrypted.isBlank()) {
                        String decrypted = Cryptography.decrypt(encrypted);
                        //System.out.println(decrypted);
                        field.set(obj, decrypted);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decrypt field: " + field.getName(), e);
                }
            }
        }
        return obj;
    }
    
    
}


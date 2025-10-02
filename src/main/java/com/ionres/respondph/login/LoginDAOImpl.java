package com.ionres.respondph.login;

import com.ionres.respondph.database.DBConnection;
import com.ionres.respondph.admin.AdminModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author jrali
 */
public class LoginDAOImpl implements LoginDAO {
    
    @Override
    public AdminModel adminLogin(String user, char[] pass) {
        AdminModel admin = null;
        String passwordStr = new String(pass);
        
        if (user == null || user.isEmpty() || pass == null || passwordStr.isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM admin WHERE username = ?";

        try (
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, user);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("hash");

                    if (BCrypt.checkpw(passwordStr, hash)) {
                        admin = new AdminModel();
                        admin.setUsername(rs.getString("username"));
                        System.out.println(rs.getString("username"));
                        admin.setfName(rs.getString("first_name"));
                        admin.setmName(rs.getString("middle_name"));
                        admin.setlName(rs.getString("last_name"));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Or use a logger for production use
        } finally {
            java.util.Arrays.fill(pass, '0'); // Clear sensitive data
        }

        return admin;
    }
}

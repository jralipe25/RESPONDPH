package com.ionres.respondph.admin;

import com.ionres.respondph.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public List<AdminModel> getAll() {
        List<AdminModel> admins = new ArrayList<>();
        String query = "SELECT * FROM admin";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AdminModel admin = new AdminModel();
                    admin.setId(rs.getInt("admin_id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setfName(rs.getString("fname"));
                    admin.setmName(rs.getString("mname"));
                    admin.setlName(rs.getString("lname"));
                    admins.add(admin);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // You might want to log or throw a custom exception
        }
        return admins;
    }

    @Override
    public boolean save(AdminModel admin) {
        String query = "INSERT INTO admin (username, fname, mname, lname, hash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getfName());
            stmt.setString(3, admin.getmName());
            stmt.setString(4, admin.getlName());
            stmt.setString(5, new String(admin.getHash()));
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(AdminModel admin) {
        String query = "UPDATE admin SET username = ?, fname = ?, mname = ?, lname = ? WHERE admin_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getfName());
            stmt.setString(3, admin.getmName());
            stmt.setString(4, admin.getlName());
            stmt.setInt(5, admin.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(int admin_id) {
        String query = "DELETE FROM admin WHERE admin_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, admin_id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        boolean flag = false;
        String query = "SELECT * FROM admin where username=?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                flag = (rs.next());
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // You might want to log or throw a custom exception
        }
        return flag;
    }

    @Override
    public AdminModel findById(int id) {
        String query = "SELECT * FROM admin WHERE admin_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                AdminModel admin = new AdminModel();
                admin.setId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setfName(rs.getString("fname"));
                admin.setmName(rs.getString("mname"));
                admin.setlName(rs.getString("lname"));
                admin.setHash(rs.getString("hash")); // assuming hash is stored as String
                return admin;
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return null;
    }
}


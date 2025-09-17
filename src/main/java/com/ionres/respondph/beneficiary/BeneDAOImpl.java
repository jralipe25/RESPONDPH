package com.ionres.respondph.beneficiary;

import com.ionres.respondph.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeneDAOImpl implements BeneDAO {

    @Override
    public List<BeneModel> getAll() {
        List<BeneModel> benes = new ArrayList<>();
        String query = "SELECT * FROM beneficiary";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BeneModel bene = new BeneModel();
//                    admin.setId(rs.getInt("admin_id"));
//                    admin.setUsername(rs.getString("username"));
//                    admin.setfName(rs.getString("fname"));
//                    admin.setmName(rs.getString("mname"));
//                    admin.setlName(rs.getString("lname"));
                    benes.add(bene);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // You might want to log or throw a custom exception
        }
        return benes;
    }

    @Override
    public boolean save(BeneModel bene) {
        String query = "INSERT INTO beneficiary (username, fname, mname, lname, hash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, admin.getUsername());
//            stmt.setString(2, admin.getfName());
//            stmt.setString(3, admin.getmName());
//            stmt.setString(4, admin.getlName());
//            stmt.setString(5, admin.getHash());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(BeneModel bene) {
        String query = "UPDATE beneficiary SET username = ?, fname = ?, mname = ?, lname = ? WHERE admin_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, admin.getUsername());
//            stmt.setString(2, admin.getfName());
//            stmt.setString(3, admin.getmName());
//            stmt.setString(4, admin.getlName());
//            stmt.setInt(5, admin.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int admin_id) {
        String query = "DELETE FROM beneficiary WHERE BeneficiaryID = ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
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

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                flag = (rs.next());
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // You might want to log or throw a custom exception
        }
        return flag;
    }
}

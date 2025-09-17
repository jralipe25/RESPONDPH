package com.ionres.respondph.admin;

import com.ionres.respondph.exception.ExceptionFactory;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class AdminServiceImpl implements AdminService{
    AdminDAO adminDao = new AdminDAOImpl();

    @Override
    public List<AdminModel> getAllAdmins() {
        List<AdminModel> admins = adminDao.getAll();
        return admins;
    }

    @Override
    public boolean createAdmin(AdminModel admin, String confirmPassword) {
        if (admin.getUsername() == null || admin.getUsername().isBlank()) {
            throw ExceptionFactory.missingField("Username");
        }

        if (!admin.getHash().equals(confirmPassword)) {
            throw ExceptionFactory.passwordMismatch();
        }

        if (adminDao.existsByUsername(admin.getUsername())) {
            throw ExceptionFactory.duplicate("Admin", admin.getUsername());
        }
        
        admin.setHash(BCrypt.hashpw(admin.getHash(), BCrypt.gensalt()));
        boolean flag = adminDao.save(admin);
        if (!flag) {
            throw ExceptionFactory.failedToCreate("Admin");
        }
        
        return flag;
    }

    @Override
    public boolean updateAdmin(AdminModel admin, String confirmPassword) {
        if (admin.getUsername() == null || admin.getUsername().isBlank()) {
            throw ExceptionFactory.missingField("Username");
        }

        if (admin.getfName() == null || admin.getfName().isBlank()) {
            throw ExceptionFactory.missingField("First Name");
        }

        if (admin.getlName() == null || admin.getlName().isBlank()) {
            throw ExceptionFactory.missingField("Last Name");
        }

        AdminModel existing = adminDao.findById(admin.getId());
        if (existing == null) {
            throw ExceptionFactory.entityNotFound("Admin with ID " + admin.getId());
        }

        boolean flag = adminDao.update(admin);
        if (!flag) {
            throw ExceptionFactory.failedToUpdate("Admin");
        }

        return flag;
    }

    @Override
    public void deleteAdminById(int id) {
        // 🔍 Check if the admin exists before attempting deletion
        AdminModel existing = adminDao.findById(id);
        if (existing == null) {
            throw ExceptionFactory.entityNotFound("Admin with ID " + id);
        }

        boolean deleted = adminDao.deleteById(id);
        if (!deleted) {
            throw ExceptionFactory.failedToDelete("Admin with ID " + id);
        }
    }
}

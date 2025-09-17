package com.ionres.respondph.admin;

import java.util.List;

public interface AdminService {
    List<AdminModel> getAllAdmins();
    boolean createAdmin(AdminModel admin, String confirmPassword);
    boolean updateAdmin(AdminModel admin, String confirmPassword);
    void deleteAdminById(int id);
}

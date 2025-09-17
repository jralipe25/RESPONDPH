package com.ionres.respondph.admin;

import java.util.List;

public interface AdminDAO {
    List<AdminModel> getAll();
    boolean save(AdminModel admin);
    boolean update(AdminModel admin);
    boolean deleteById(int admin_id);
    boolean existsByUsername(String username);
    AdminModel findById(int id);
}

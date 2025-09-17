package com.ionres.respondph.login;

import com.ionres.respondph.admin.AdminModel;

public interface LoginDAO {
    AdminModel adminLogin(String user, char[] pass);
}

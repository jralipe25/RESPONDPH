package com.ionres.respondph.login;

import com.ionres.respondph.admin.AdminModel;
import com.ionres.respondph.util.Cryptography;
import com.ionres.respondph.util.Global;

public class LoginServiceImpl implements LoginService{
    LoginDAO loginDAO;

    public LoginServiceImpl(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    @Override
    public AdminModel login(String user, char[] pass) {
        user = Cryptography.encrypt(user);
        AdminModel adminModel = loginDAO.adminLogin(user, pass);
        java.util.Arrays.fill(pass, '0'); // clear sensitive data
        if (adminModel != null) {
            Global.ADMIN = adminModel;
        }
        return adminModel;
    }
}

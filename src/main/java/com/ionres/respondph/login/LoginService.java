package com.ionres.respondph.login;

import com.ionres.respondph.admin.AdminModel;

public interface LoginService {
    AdminModel login(String user, char[] pass);
}

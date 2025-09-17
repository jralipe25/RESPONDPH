/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.ionres.respondph;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.ionres.respondph.login.LoginController;
import com.ionres.respondph.login.LoginDAO;
import com.ionres.respondph.login.LoginDAOImpl;
import com.ionres.respondph.login.LoginFrame;
import com.ionres.respondph.login.LoginService;
import com.ionres.respondph.login.LoginServiceImpl;

/**
 *
 * @author jrali
 */
public class RESPONDPH {

    public static void main(String[] args) {
        try {
            FlatIntelliJLaf.setup(); // or FlatDarkLaf.setup()
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        LoginFrame loginFrame = new LoginFrame();
        LoginDAO loginDAO = new LoginDAOImpl();
        LoginService loginService = new LoginServiceImpl(loginDAO);
        new LoginController(loginFrame, loginService);
        loginFrame.setVisible(true);
        System.out.println(loginFrame.getClass().getName());
    }
}

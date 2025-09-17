package com.ionres.respondph.login;

import com.ionres.respondph.admin.AdminModel;
import com.ionres.respondph.main.MainController;
import com.ionres.respondph.main.MainFrame;
import com.ionres.respondph.util.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginController {

    LoginFrame loginFrame;
    LoginService loginService;

    public LoginController(LoginFrame loginFrame, LoginService loginService) {
        this.loginFrame = loginFrame;
        this.loginService = loginService;
        LoginActions actions = new LoginActions();
        this.loginFrame.allListeners(actions, actions); // Register actions
    }

    private void login() {
        String user = loginFrame.unTF.getText();
        char[] pass = loginFrame.pwPF.getPassword();
        AdminModel adminModel = loginService.login(user, pass);

        if (adminModel != null) {
            MainFrame mainFrame = new MainFrame();
            new Timer(mainFrame); 
            new MainController(mainFrame); 
            mainFrame.setVisible(true); 
            loginFrame.setVisible(false);
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                loginFrame,
                "Invalid username or password.",
                "Login Failed",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
   

    class LoginActions implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginFrame.loginBtn) {
                login();
            } else if (e.getSource() == loginFrame.exitBtn) {
                System.exit(0);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == loginFrame.pwPF) {
                login();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}

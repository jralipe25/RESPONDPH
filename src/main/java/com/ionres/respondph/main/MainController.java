package com.ionres.respondph.main;

import com.ionres.respondph.admin.AdminController;
import com.ionres.respondph.admin.AdminModel;
import com.ionres.respondph.admin.view.AdminPanel;
import com.ionres.respondph.beneficiary.BeneController;
import com.ionres.respondph.beneficiary.view.BenePanel;
import com.ionres.respondph.util.Global;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {

    MainFrame mainFrame;
    AdminPanel adminPanel;
    BenePanel benePanel;
    CardLayout cl;

    public MainController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adminPanel = new AdminPanel();
        this.benePanel = new BenePanel();
        new AdminController(this.mainFrame, adminPanel);
        new BeneController(this.mainFrame, benePanel);

        this.mainFrame.allListeners(new MainActions());
        
        this.mainFrame.loggedinLbl.setText(Global.ADMIN.getUsername());
    }

    class MainActions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == mainFrame.dashboardMI) {

            } else if (e.getSource() == mainFrame.exitMI) {
                System.exit(0);
            } else if (e.getSource() == mainFrame.beneficiaryMI) {
                cl = (CardLayout) (mainFrame.mainPanel.getLayout());
                mainFrame.mainPanel.add(benePanel, "BenePanel");
                cl.show(mainFrame.mainPanel, "BenePanel");
            } else if (e.getSource() == mainFrame.adminMI) {
                cl = (CardLayout) (mainFrame.mainPanel.getLayout());
                mainFrame.mainPanel.add(adminPanel, "AdminPanel");
                cl.show(mainFrame.mainPanel, "AdminPanel");
            }
        }
    }
}

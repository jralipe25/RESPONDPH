package com.ionres.respondph.admin;

import com.ionres.respondph.admin.view.AddAdminDialog;
import com.ionres.respondph.admin.view.AdminPanel;
import com.ionres.respondph.admin.view.EditAdminDialog;
import com.ionres.respondph.exception.*;
import com.ionres.respondph.main.MainFrame;
import com.ionres.respondph.util.QuickSearchUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AdminController {

    AdminPanel adminPanel;
    AddAdminDialog addAdminDialog;
    EditAdminDialog editAdminDialog;
    AdminService adminService;

    public AdminController(MainFrame mainFrame, AdminPanel adminPanel) {
        this.adminPanel = adminPanel;
        this.addAdminDialog = new AddAdminDialog(mainFrame, true);
        this.editAdminDialog = new EditAdminDialog(mainFrame, true);
        this.adminService = new AdminServiceImpl();

        AdminActions actions = new AdminActions();
        this.adminPanel.allListeners(actions);
        this.addAdminDialog.allListeners(actions);

        populateTable();
    }

    private void populateTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Username", "First Name", "Middle Name", "Last Name"});

        List<AdminModel> admins = adminService.getAllAdmins();

        for (AdminModel admin : admins) {
            model.addRow(new Object[]{
                admin.getId(),
                admin.getUsername(),
                admin.getfName(),
                admin.getmName(),
                admin.getlName()
            });
        }

        adminPanel.adminTable.setModel(model);
        new QuickSearchUtil(adminPanel.adminTable, adminPanel.searchTF);
    }

    private void createAdmin() {
        AdminModel adminModel = new AdminModel();
        adminModel.setUsername(addAdminDialog.usernameTF.getText());
        adminModel.setfName(addAdminDialog.fNameTF.getText());
        adminModel.setmName(addAdminDialog.mNameTF.getText());
        adminModel.setlName(addAdminDialog.lNameTF.getText());
        adminModel.setHash(new String(addAdminDialog.pwPF.getPassword()));

        String confirmPassword = new String(addAdminDialog.confirmPF.getPassword());

        try {
            adminService.createAdmin(adminModel, confirmPassword);
            JOptionPane.showMessageDialog(null, "Admin successfully created.", "Success", JOptionPane.INFORMATION_MESSAGE);
            addAdminDialog.dispose();
            populateTable();
        } catch (ValidationException | DuplicateEntityException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (EntityOperationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (DomainException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unexpected system failure: " + e.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handleEdit() {
        int dataRow = adminPanel.adminTable.getSelectedRow();
        if (dataRow >= 0) {
            String admin_id = adminPanel.adminTable.getValueAt(dataRow, 0) + "";
            editAdminDialog.usernameTF.setText((String) adminPanel.adminTable.getValueAt(dataRow, 1));
            editAdminDialog.fNameTF.setText((String) adminPanel.adminTable.getValueAt(dataRow, 2));
            editAdminDialog.mNameTF.setText((String) adminPanel.adminTable.getValueAt(dataRow, 3));
            editAdminDialog.lNameTF.setText((String) adminPanel.adminTable.getValueAt(dataRow, 4));
            editAdminDialog.setTitle("Edit Information of Admin ID: " + admin_id);
            editAdminDialog.pack();
            editAdminDialog.setLocationRelativeTo(adminPanel);
            editAdminDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select admin to edit.");
        }
    }

    private void handleDelete() {
        int dataRow = adminPanel.adminTable.getSelectedRow();
        if (dataRow >= 0) {
            int admin_id = Integer.valueOf(adminPanel.adminTable.getValueAt(dataRow, 0).toString());
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to "
                    + "Delete Admin: " + admin_id + "?", "Warning", dialogButton);
            if (dialogResult == JOptionPane.YES_OPTION) {
                adminService.deleteAdminById(admin_id);
                populateTable();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select admin to delete.");
        }
    }
    
    private void udpateAdmin() {
        AdminModel adminModel = new AdminModel();
        adminModel.setUsername(editAdminDialog.usernameTF.getText());
        adminModel.setfName(editAdminDialog.fNameTF.getText());
        adminModel.setmName(editAdminDialog.mNameTF.getText());
        adminModel.setlName(editAdminDialog.lNameTF.getText());
        adminModel.setHash(new String(editAdminDialog.pwPF.getPassword()));

        String confirmPassword = new String(editAdminDialog.confirmPF.getPassword());

        try {
            adminService.updateAdmin(adminModel, confirmPassword);
            JOptionPane.showMessageDialog(null, "Admin updated created.", "Success", JOptionPane.INFORMATION_MESSAGE);
            editAdminDialog.dispose();
            populateTable();
        } catch (ValidationException | DuplicateEntityException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (EntityOperationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (DomainException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unexpected system failure: " + e.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    class AdminActions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == adminPanel.addAdminBtn) {
                addAdminDialog.pack();
                addAdminDialog.setLocationRelativeTo(adminPanel);
                addAdminDialog.setVisible(true);
            } else if (e.getSource() == adminPanel.editAdminBtn) {
                handleEdit();
            } else if (e.getSource() == addAdminDialog.saveBtn) {
                createAdmin();
            } else if (e.getSource() == adminPanel.deleteAdminBtn) {
                handleDelete();
            } else if (e.getSource() == editAdminDialog.saveBtn) {
                udpateAdmin();
                System.out.println("sulod");
            }
        }
    }
}

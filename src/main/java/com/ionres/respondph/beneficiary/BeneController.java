package com.ionres.respondph.beneficiary;

import com.ionres.respondph.beneficiary.view.AddBeneDialog;
import com.ionres.respondph.beneficiary.view.BenePanel;
import com.ionres.respondph.beneficiary.view.EditBeneDialog;
import com.ionres.respondph.main.MainFrame;
import com.ionres.respondph.util.QuickSearchUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class BeneController {

    BenePanel benePanel;
    AddBeneDialog addBeneDialog;
    EditBeneDialog editBeneDialog;
    BeneService beneService;

    public BeneController(MainFrame mainFrame, BenePanel benePanel) {
        this.benePanel = benePanel;
        this.addBeneDialog = new AddBeneDialog(mainFrame, true);
        this.editBeneDialog = new EditBeneDialog(mainFrame, true);
        this.beneService = new BeneServiceImpl();

        BeneActions actions = new BeneActions();
        this.benePanel.allListeners(actions);
        this.addBeneDialog.allListeners(actions);

        populateTable();
    }

    private void populateTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Username", "First Name", "Middle Name", "Last Name"});

        List<BeneModel> benes = beneService.getAllBene();

        for (BeneModel bene : benes) {
            model.addRow(new Object[]{ 
            //                bene.getId(),
            //                bene.getUsername(),
            //                bene.getfName(),
            //                bene.getmName(),
            //                bene.getlName()
            });
        }

        benePanel.beneTable.setModel(model);
        new QuickSearchUtil(benePanel.beneTable, benePanel.searchTF);
    }

    class BeneActions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //SHOW ADD BENE DIALOG
            if (e.getSource() == benePanel.addBeneBtn) {
                addBeneDialog.setTitle("Add Beneficiary");
                addBeneDialog.pack();
                addBeneDialog.setLocationRelativeTo(benePanel);
                addBeneDialog.setVisible(true);

                //SHOW EDIT BENE DIALOG
            } else if (e.getSource() == benePanel.editBeneBtn) {
                int dataRow = benePanel.beneTable.getSelectedRow();
                if (dataRow >= 0) {
                    String bene_id = benePanel.beneTable.getValueAt(dataRow, 0) + "";
                    editBeneDialog.usernameTF.setText((String) benePanel.beneTable.getValueAt(dataRow, 1));
                    editBeneDialog.fNameTF.setText((String) benePanel.beneTable.getValueAt(dataRow, 2));
                    editBeneDialog.mNameTF.setText((String) benePanel.beneTable.getValueAt(dataRow, 3));
                    editBeneDialog.lNameTF.setText((String) benePanel.beneTable.getValueAt(dataRow, 4));
                    editBeneDialog.setTitle("Edit Information of Beneficiary ID: " + bene_id);
                    //editBeneDialog.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
                    editBeneDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select bene to edit.");
                }

                //CREATE BENE
            } else if (e.getSource() == addBeneDialog.saveBtn) {
                BeneModel beneModel = new BeneModel();
//                beneModel.setUsername(addBeneDialog.usernameTF.getText());
//                beneModel.setfName(addBeneDialog.fNameTF.getText());
//                beneModel.setmName(addBeneDialog.mNameTF.getText());
//                beneModel.setlName(addBeneDialog.lNameTF.getText());
//                beneModel.setHash(BCrypt.hashpw(new String(addBeneDialog.pwPF.getPassword()), BCrypt.gensalt()));

                if (beneService.createBene(beneModel)) {
                    JOptionPane.showMessageDialog(addBeneDialog, "Beneficiary Added!");
                }
            }
        }
    }
}

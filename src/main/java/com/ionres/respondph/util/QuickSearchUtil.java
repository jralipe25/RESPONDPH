package com.ionres.respondph.util;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class QuickSearchUtil {

    public QuickSearchUtil(JTable tbl, JTextField tf) {
        DefaultTableModel model = new DefaultTableModel(getTableData(tbl), getHeaders(tbl)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // disables editing for all cells
            }
        };
        tbl.setModel(model);
        tbl.getColumnModel().getColumn(0).setMinWidth(0);
        tbl.getColumnModel().getColumn(0).setMaxWidth(100);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(50);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(tbl.getModel());
        tbl.setRowSorter(rowSorter);
        tf.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tf.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tf.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    Object[][] getTableData (JTable table) 
    {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0 ; i < nRow ; i++)
            for (int j = 0 ; j < nCol ; j++)
                tableData[i][j] = dtm.getValueAt(i,j);
        return tableData;
    }
    
    String[] getHeaders(JTable table)
    {
        String[] headers = new String[table.getColumnCount()];
        for(int x = 0 ; x < headers.length ; x++)
        {
            headers[x] = table.getColumnName(x);
        }
        return headers;
    }
}
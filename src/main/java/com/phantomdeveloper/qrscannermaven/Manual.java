/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phantomdeveloper.qrscannermaven;

import com.phantomdeveloper.qrscannermaven.util.DbConn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Leonardy
 */
public class Manual extends javax.swing.JFrame {

    private Connection myConn = null;
    private PreparedStatement myStmt = null;
    private ResultSet myRs = null;
//    String orderBy;
    String orderBy[] = {
        "",
        "WHERE SIGN_IN = 1",
        "WHERE SIGN_IN = 0",
        "WHERE SIGN_OUT = 1",
        "WHERE SIGN_OUT = 0",
        "WHERE SIGN_IN = 1 AND SIGN_OUT = 1",
        "WHERE SIGN_IN = 0 AND SIGN_OUT = 0"
    };
    int orderNum = 0;

    public Manual() {
        initComponents();
        connectDatabase();
        Show_SignIn_JTable();
        jTable1.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                try {
                    int signIn, signOut;
                    //in column sign in
                    if (jTable1.isColumnSelected(3)) {
                        if ((Boolean) jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn())) {
                            signIn = 1;
                        } else {
                            signIn = 0;
                        }
                        System.out.println("UPDATE PESERTA SET SIGN_IN=" + signIn
                                + " WHERE NAMA='" + jTable1.getValueAt(jTable1.getSelectedRow(), 1)
                                + "' AND EMAIL='" + jTable1.getValueAt(jTable1.getSelectedRow(), 2) + "'");
                        setExecuteUpdate("UPDATE PESERTA SET SIGN_IN=" + signIn
                                + " WHERE NAMA='" + jTable1.getValueAt(jTable1.getSelectedRow(), 1)
                                + "' AND EMAIL='" + jTable1.getValueAt(jTable1.getSelectedRow(), 2) + "'");
                        // in column sign out
                    }
                    if (jTable1.isColumnSelected(4)) {
                        if ((Boolean) jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn())) {
                            signOut = 1;
                        } else {
                            signOut = 0;
                        }
                        System.out.println("UPDATE PESERTA SET SIGN_OUT=" + signOut
                                + " WHERE NAMA='" + jTable1.getValueAt(jTable1.getSelectedRow(), 1)
                                + "' AND EMAIL='" + jTable1.getValueAt(jTable1.getSelectedRow(), 2) + "'");
                        setExecuteUpdate("UPDATE PESERTA SET SIGN_OUT=" + signOut
                                + " WHERE NAMA='" + jTable1.getValueAt(jTable1.getSelectedRow(), 1)
                                + "' AND EMAIL='" + jTable1.getValueAt(jTable1.getSelectedRow(), 2) + "'");

                    }
                } catch (Exception ee) {
                    ee.printStackTrace();

                }
            }
        });
    }

    public void setExecuteUpdate(String sql) {
        try {
            myStmt = myConn.prepareStatement(sql);
            // Execute SQL query
            myStmt.executeUpdate();
            myStmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void connectDatabase() {
        try {
            myConn = DriverManager.getConnection(DbConn.JDBC_URL, DbConn.JDBC_USERNAME, DbConn.JDBC_PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getOrderByComboBox() {
        if (jComboBox1.getSelectedItem().equals("SHOW ALL")) {
//            orderBy="";
            orderNum = 0;
        }
        if (jComboBox1.getSelectedItem().equals("SHOW ALL SIGN IN")) {
//            orderBy="WHERE SIGN_IN = 1";
            orderNum = 1;
        }
        if (jComboBox1.getSelectedItem().equals("SHOW ALL HASNT SIGN IN")) {
//            orderBy="WHERE SIGN_IN = 0";
            orderNum = 2;
        }
        if (jComboBox1.getSelectedItem().equals("SHOW ALL SIGN OUT")) {
//            orderBy="WHERE SIGN_OUT = 1";
            orderNum = 3;
        }
        if (jComboBox1.getSelectedItem().equals("SHOW ALL HASNT SIGN OUT")) {
//            orderBy="WHERE SIGN_OUT = 0";
            orderNum = 4;
        }
        if (jComboBox1.getSelectedItem().equals("SHOW ALL SIGN IN AND SIGN OUT")) {
//            orderBy="WHERE SIGN_IN = 1 AND SIGN_OUT = 1";
            orderNum = 5;
        }
        if (jComboBox1.getSelectedItem().equals("SHOW ALL HASNT SIGN IN AND SIGN OUT")) {
//            orderBy = "WHERE SIGN_IN = 0 AND SIGN_OUT = 0";
            orderNum = 6;
        }
    }

    public ArrayList<peserta> getSignInList() {
        ArrayList<peserta> pesertaList = new ArrayList<peserta>();
        String find = jTextField1.getText();
        try {
            peserta psrt;
            if (find.equals(null)) {
                System.out.println("select * from peserta"
                        + orderBy[orderNum] + " ORDER BY nama ;");
                myStmt = myConn.prepareStatement("select * from peserta"
                        + orderBy[orderNum] + " ORDER BY nama ;");

            } else {
                System.out.println("select * from (select * from peserta p " + orderBy[orderNum]
                        + ") where nama like '" + find + '%'
                        + "' OR email like '" + find + '%'
                        + "' ORDER BY nama;");
                myStmt = myConn.prepareStatement("select * from (select * from peserta p " + orderBy[orderNum]
                        + ") a where a.nama like '" + find + '%'
                        + "' OR a.email like '" + find + '%'
                        + "' ORDER BY nama;");
//                select a.nama,a.email from (select * from dbscrum.peserta p where p.sign_in =0 and p.sign_out =0) a 
//                        where a.nama like 'l%';
            }
            myRs = myStmt.executeQuery();
            while (myRs.next()) {
                psrt = new peserta(myRs.getString("nama"),
                        myRs.getString("email"),
                        myRs.getString("code"),
                        myRs.getBoolean("sign_in"),
                        myRs.getBoolean("sign_out"));
                pesertaList.add(psrt);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pesertaList;
    }

    private void Show_SignIn_JTable() {
        ArrayList<peserta> list = getSignInList();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        Object[] row = new Object[6];
        for (int i = 0; i < list.size(); i++) {
            row[0] = model.getRowCount() + 1;
            row[1] = list.get(i).getNama();
            row[2] = list.get(i).getEmail();
            row[3] = list.get(i).getSign_in();
            row[4] = list.get(i).getSign_out();
            model.addRow(row);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton1.setText("SEARCH");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SHOW ALL", "SHOW ALL SIGN IN", "SHOW ALL HASNT SIGN IN", "SHOW ALL SIGN OUT", "SHOW ALL HASNT SIGN OUT", "SHOW ALL SIGN IN AND SIGN OUT", "SHOW ALL HASNT SIGN IN AND SIGN OUT" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 25)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO", "NAMA", "EMAIL", "SIGN IN ", "SIGN OUT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setPreferredSize(new java.awt.Dimension(1600, 900));
        jTable1.setRowHeight(30);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(30);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(20);
            jTable1.getColumnModel().getColumn(3).setMinWidth(200);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(200);
            jTable1.getColumnModel().getColumn(4).setMinWidth(200);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(200);
        }

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton2.setText("REFRESH");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jTextField1))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        Show_SignIn_JTable();
    }//GEN-LAST:event_jTextField1ActionPerformed

//button search
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Show_SignIn_JTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        getOrderByComboBox();
        Show_SignIn_JTable();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    //BUTTON REFRESH
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Show_SignIn_JTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    //BUTTON BACK
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Manual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Manual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Manual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Manual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new Manual().setVisible(true);
                Manual manual = new Manual();
                manual.setLocationRelativeTo(null);
                manual.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phantomdeveloper.qrscannermaven;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.phantomdeveloper.qrscannermaven.util.DbConn;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Phantom
 */
public class FrmMain extends javax.swing.JFrame implements Runnable, ThreadFactory {

    /**
     * Creates new form FrmMain
     */
    enum Status {
        SIGN_IN, SIGN_OUT;
    }
    private Status status = Status.SIGN_IN;

    private Connection myConn = null;
    private PreparedStatement myStmt = null;
    private ResultSet myRs = null;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    private Webcam webcam = null;
    private WebcamPanel webPanel = null;

    public FrmMain() {
        initComponents();
        setTitle("Qr Code Scanner");

        setLocationRelativeTo(null);

        Dimension size = new Dimension(640, 480);

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        webPanel = new WebcamPanel(webcam);
        webPanel.setPreferredSize(size);
        webPanel.setFPSDisplayed(true);

        jPanel1.add(webPanel);
        executor.execute(this);
        connectDatabase();
        Show_Table();
    }

    private void connectDatabase() {
        try {
            myConn = DriverManager.getConnection(DbConn.JDBC_URL, DbConn.JDBC_USERNAME, DbConn.JDBC_PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Show_Table() {
        Show_SignIn_JTable();
        Show_SignOut_JTable();
    }

    public ArrayList<peserta> getSignInList() {
        ArrayList<peserta> pesertaList = new ArrayList<peserta>();

        try {
            peserta psrt;
            switch (status) {
                case SIGN_IN:
                    myStmt = myConn.prepareStatement("select * from peserta where peserta.sign_in = 0 AND peserta.sign_out=0 ORDER BY nama;");
                    break;
                case SIGN_OUT:
                    myStmt = myConn.prepareStatement("select * from peserta where peserta.sign_in = 1 AND peserta.sign_out=0 ORDER BY nama;");
                    break;
            }
            // Execute SQL query
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

    public void Show_SignIn_JTable() {
        ArrayList<peserta> list = getSignInList();
        DefaultTableModel model = (DefaultTableModel) SignInTable.getModel();
        model.setRowCount(0);
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = model.getRowCount() + 1;
            row[1] = list.get(i).getNama();
            row[2] = list.get(i).getEmail();
            row[3] = list.get(i).getCode();
            model.addRow(row);
        }

    }

    public ArrayList<peserta> getSignOutList() {
        ArrayList<peserta> pesertaList = new ArrayList<peserta>();

        try {
            peserta psrt;
            switch (status) {
                case SIGN_IN:
                    myStmt = myConn.prepareStatement("select * from peserta where peserta.sign_in = 1 ORDER BY nama");
                    break;
                case SIGN_OUT:
                    myStmt = myConn.prepareStatement("select * from peserta where peserta.sign_in = 1 AND peserta.sign_out = 1 ORDER BY nama;");
                    break;
            }
            // Execute SQL query
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

    public void Show_SignOut_JTable() {
        ArrayList<peserta> list = getSignOutList();
        DefaultTableModel model = (DefaultTableModel) SignOutTable.getModel();
        model.setRowCount(0);
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = model.getRowCount() + 1;
            row[1] = list.get(i).getNama();
            row[2] = list.get(i).getEmail();
            row[3] = list.get(i).getCode();
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

        jPanel1 = new javax.swing.JPanel();
        btnSignIn = new javax.swing.JButton();
        btnSignOut = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        SignInTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        SignOutTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextArea = new javax.swing.JTextArea();
        Down = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1600, 900));
        setSize(new java.awt.Dimension(1600, 900));

        jPanel1.setBackground(new java.awt.Color(104, 240, 240));
        jPanel1.setPreferredSize(new java.awt.Dimension(640, 420));
        jPanel1.setLayout(new java.awt.CardLayout());

        btnSignIn.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnSignIn.setText("Sign in");
        btnSignIn.setEnabled(false);
        btnSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignInActionPerformed(evt);
            }
        });

        btnSignOut.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        btnSignOut.setText("Sign out");
        btnSignOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignOutActionPerformed(evt);
            }
        });

        SignInTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Nama", "Email", "Code"
            }
        ));
        jScrollPane2.setViewportView(SignInTable);
        if (SignInTable.getColumnModel().getColumnCount() > 0) {
            SignInTable.getColumnModel().getColumn(0).setMinWidth(20);
            SignInTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            SignInTable.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        SignOutTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Nama", "Email", "Code"
            }
        ));
        jScrollPane3.setViewportView(SignOutTable);
        if (SignOutTable.getColumnModel().getColumnCount() > 0) {
            SignOutTable.getColumnModel().getColumn(0).setMinWidth(20);
            SignOutTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            SignOutTable.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SCAN YOUR BARCODE HERE");

        TextArea.setColumns(20);
        TextArea.setRows(5);
        jScrollPane1.setViewportView(TextArea);

        Down.setText("v");
        Down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setText("Sign in");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSignIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSignOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1099, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Down)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1099, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Down)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSignIn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSignOut)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignInActionPerformed
        btnSignIn.setEnabled(false);
        btnSignOut.setEnabled(true);
        status = Status.SIGN_IN;
        jLabel2.setText("Sign In");
        Show_Table();
    }//GEN-LAST:event_btnSignInActionPerformed

    private void btnSignOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignOutActionPerformed
        btnSignIn.setEnabled(true);
        btnSignOut.setEnabled(false);
        status = Status.SIGN_OUT;
        jLabel2.setText("Sign Out");
        Show_Table();
    }//GEN-LAST:event_btnSignOutActionPerformed

    private void DownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownActionPerformed
        String key = (String) SignInTable.getValueAt(SignInTable.getSelectedRow(),3);
        generateResult(key);
        // TODO add your handling code here:
    }//GEN-LAST:event_DownActionPerformed

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
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMain().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Down;
    private javax.swing.JTable SignInTable;
    private javax.swing.JTable SignOutTable;
    private javax.swing.JTextArea TextArea;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JButton btnSignOut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                int length = result.getText().length();
                String code = "";
                if (length >= 32) {
                    TextArea.setText(result.getText());
                    code = result.getText().substring(length - 32, length);
                    generateResult(code);
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong QR Code.");

                }
            } else {
                TextArea.setText("");
            }
        } while (true);
    }

    private void generateResult(String code) {
        try {
            // Prepare statement
            myStmt = myConn.prepareStatement("select * from peserta where peserta.code='" + code + "';");
            // Execute SQL query
            myRs = myStmt.executeQuery();
            System.out.println(myRs);
            // Process result set
            if (myRs != null) {
                while (myRs.next()) {
                    switch (status) {
                        case SIGN_IN:
                            if (!myRs.getBoolean("sign_in")) {
                                JOptionPane.showMessageDialog(this, "Welcome " + myRs.getString("nama"));
                                signIn(code);
                            } else {
                                JOptionPane.showMessageDialog(this, "You had signed in");
                            }
                            break;
                        case SIGN_OUT:
                            if (!myRs.getBoolean("sign_out")) {
                                JOptionPane.showMessageDialog(this, "Thanks " + myRs.getString("nama") + " for paticipating");
                                signOut(code);
                            } else {
                                JOptionPane.showMessageDialog(this, "You had signed out");
                            }
                            break;
                    }

                }
            } else {
                JOptionPane.showMessageDialog(this, "Wrong QR Code.");

            }
            Show_Table();
            myStmt.close();
            myRs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void signIn(String code) {
        try {
            myStmt = myConn.prepareStatement("UPDATE `dbscrum`.`peserta` SET `sign_in`='1' WHERE `code`='" + code + "';");
            // Execute SQL query
            myStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void signOut(String code) {
        try {
            myStmt = myConn.prepareStatement("UPDATE `dbscrum`.`peserta` SET `sign_out`='1' WHERE `code`='" + code + "';");
            // Execute SQL query
            myStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "example-runner");
        t.setDaemon(true);
        return t;
    }
}

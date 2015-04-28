/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.client;

import java.awt.Dialog;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Martin
 */
public class RegisterAndLoginDialog extends javax.swing.JDialog {

    /**
     * Creates new form RegisterAndLoginDialog
     */
    public RegisterAndLoginDialog() {
        super((Dialog)null, false);
        initComponents();
        
        this.setDefaultCloseOperationTo_Exit_On_Close();
        this.getRootPane().setDefaultButton(VerbindenButton);
    }

    
    public String getUserName() {
        return this.BenutzerField.getText();
    }
    
    public String getPassword() {
        return this.PasswortField.getPassword().toString();
    }
    
    public boolean isMessageHistoryRequested() {
        return this.VerlaufAnzeigenCheckBox.isSelected();
    }
    
    public long getTimestampForBeginningMessageHistory() {
        long result = 0;
        Calendar currentTime = Calendar.getInstance(); //aktuelle Zeit
        if (this.VerlaufAnzeigenCheckBox.isSelected()) {
            int comboBoxIndex = this.VerlaufanzeigeZeitraumComboBox.getSelectedIndex();
            if (comboBoxIndex == 0) {
                currentTime.add(Calendar.DATE, -1);
                result = currentTime.getTimeInMillis();
            } else if (comboBoxIndex == 1) {
                currentTime.add(Calendar.DATE, -3);
                result = currentTime.getTimeInMillis();
            } else if (comboBoxIndex == 2) {
                currentTime.add(Calendar.DATE, -7);
                result = currentTime.getTimeInMillis();
            } else if (comboBoxIndex == 3) {
                currentTime.add(Calendar.MONTH, -1);
                result = currentTime.getTimeInMillis();
            } else if (comboBoxIndex == 4) {
                result = 1;
            }
        } 
        return result;
    }
    
    public String getServerIp() {
        return this.ServerComboBox.getSelectedItem().toString();
    }
    
    
    private void setDefaultCloseOperationTo_Exit_On_Close() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        BenutzerLabel = new javax.swing.JLabel();
        PasswortLabel = new javax.swing.JLabel();
        BenutzerField = new javax.swing.JTextField();
        PasswortField = new javax.swing.JPasswordField();
        VerlaufAnzeigenCheckBox = new javax.swing.JCheckBox();
        VerlaufanzeigeZeitraumComboBox = new javax.swing.JComboBox();
        ServerLabel = new javax.swing.JLabel();
        ServerComboBox = new javax.swing.JComboBox();
        VerbindenButton = new javax.swing.JButton();
        AnmeldungRegistrierungLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ChatZilla");
        setLocationByPlatform(true);
        setResizable(false);

        BenutzerLabel.setLabelFor(BenutzerField);
        BenutzerLabel.setText("Benutzer:");

        PasswortLabel.setLabelFor(PasswortField);
        PasswortLabel.setText("Passwort:");

        PasswortField.setEnabled(false);

        VerlaufAnzeigenCheckBox.setText("Verlauf anzeigen");

        VerlaufanzeigeZeitraumComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "heute", "letzten 3 Tage", "letzte Woche", "letzten Monat", "komplett" }));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, VerlaufAnzeigenCheckBox, org.jdesktop.beansbinding.ELProperty.create("${selected}"), VerlaufanzeigeZeitraumComboBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        ServerLabel.setLabelFor(ServerComboBox);
        ServerLabel.setText("Server:");

        ServerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "172.16.59.230", "172.16.53.124" }));

        VerbindenButton.setText("Verbinden");
        VerbindenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerbindenButtonActionPerformed(evt);
            }
        });

        AnmeldungRegistrierungLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        AnmeldungRegistrierungLabel.setText("Anmeldung/Registrierung:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(VerbindenButton)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PasswortLabel)
                            .addComponent(BenutzerLabel)
                            .addComponent(ServerLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(VerlaufanzeigeZeitraumComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PasswortField, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BenutzerField, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(VerlaufAnzeigenCheckBox)
                            .addComponent(ServerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(AnmeldungRegistrierungLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(AnmeldungRegistrierungLabel)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BenutzerLabel)
                    .addComponent(BenutzerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PasswortLabel)
                    .addComponent(PasswortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(VerlaufAnzeigenCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VerlaufanzeigeZeitraumComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ServerLabel)
                    .addComponent(ServerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(VerbindenButton)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void VerbindenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerbindenButtonActionPerformed
        Application.SocketHandler = new SocketHandler();
        if ( Application.SocketHandler.TryConnectToServerSocket() ) {
            Application.ChatFrame = new ChatFrame();
            Application.SocketHandler.StartSocketThread();
            this.setVisible(false);
            Application.ChatFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                    Application.RegisterAndLoginDialog,
                    "Server ist nicht erreichbar.\nBitte wählen Sie einen anderen aus!",
                    "Connection Problem",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_VerbindenButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AnmeldungRegistrierungLabel;
    private javax.swing.JTextField BenutzerField;
    private javax.swing.JLabel BenutzerLabel;
    private javax.swing.JPasswordField PasswortField;
    private javax.swing.JLabel PasswortLabel;
    private javax.swing.JComboBox ServerComboBox;
    private javax.swing.JLabel ServerLabel;
    private javax.swing.JButton VerbindenButton;
    private javax.swing.JCheckBox VerlaufAnzeigenCheckBox;
    private javax.swing.JComboBox VerlaufanzeigeZeitraumComboBox;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}

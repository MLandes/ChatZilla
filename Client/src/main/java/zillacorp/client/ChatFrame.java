/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import zillacorp.dbModel.Message;
import zillacorp.dbModel.UserOnline;

/**
 *
 * @author Martin
 */
public class ChatFrame extends javax.swing.JFrame {

    private DefaultListModel<String> onlineClientsListModel = new DefaultListModel<>();
    
    
    /**
     * Creates new form ApplicationFrame
     */
    public ChatFrame() {
        initComponents();
        
        this.MessageHistoryTextPane.setText("Willkommen bei ChatZilla, " + Application.RegisterAndLoginDialog.getUserName() + "!\n");
        this.AddOnlineClientToList(Application.RegisterAndLoginDialog.getUserName());
        this.StatusLabel.setText("Verbunden mit: " + Application.RegisterAndLoginDialog.getServerIp());
        this.MessageTextField.requestFocus();
        this.getRootPane().setDefaultButton(SendenButton);
    }

    
    public void UpdateMessageHistory(ArrayList<Message> newMessages) {
        for (Message item : newMessages) {
            this.UpdateMessageHistory(item);
        }
    }
    public void UpdateMessageHistory(Message newMessage) {
        String oldMessageHistoryEntries = this.MessageHistoryTextPane.getText();
        String newMessageHistoryEntry = new String();
        Date newMessage_date = new Date(newMessage.serverTimeStamp);
        newMessageHistoryEntry = newMessageHistoryEntry
                + newMessage_date.toString() + " - " + newMessage.userNickname + ":\n"
                + newMessage.messageText + "\n";
        this.MessageHistoryTextPane.setText(oldMessageHistoryEntries + newMessageHistoryEntry);
    }
    
    public void UpdateServerIp(String serverIp) {
        this.StatusLabel.setText("Verbunden mit: " + serverIp);
    }
    
    public void AddOnlineClientsToList(LinkedList<UserOnline> newClients) {
        for (UserOnline item : newClients) {
            this.AddOnlineClientToList(item.nickname);
        }
    }
    public void AddOnlineClientToList(String newClient) {
        this.onlineClientsListModel.addElement(newClient);
        this.OnlineClientsList.setModel(this.onlineClientsListModel);
    }
    
    public void RemoveOnlineClientsFromList(LinkedList<UserOnline> leftClients) {
        for (UserOnline item : leftClients) {
            this.RemoveOnlineClientFromList(item.nickname);
        }
    }
    public void RemoveOnlineClientFromList(String leftClient) {
        this.onlineClientsListModel.removeElement(leftClient);
        this.OnlineClientsList.setModel(this.onlineClientsListModel);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ChatRoomTabbedPane = new javax.swing.JTabbedPane();
        ChatRoomPanel = new javax.swing.JPanel();
        MessageHistoryScrollPane = new javax.swing.JScrollPane();
        MessageHistoryTextPane = new javax.swing.JTextPane();
        TrennenButton = new javax.swing.JButton();
        OnlineClientsScrollPane = new javax.swing.JScrollPane();
        OnlineClientsList = new javax.swing.JList();
        OnlineClientsLabel = new javax.swing.JLabel();
        MessageTextField = new javax.swing.JTextField();
        SendenButton = new javax.swing.JButton();
        StatusSeparator = new javax.swing.JSeparator();
        StatusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChatZilla");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(400, 300));

        MessageHistoryTextPane.setEditable(false);
        MessageHistoryScrollPane.setViewportView(MessageHistoryTextPane);

        TrennenButton.setText("Trennen");
        TrennenButton.setFocusable(false);
        TrennenButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        TrennenButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        TrennenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrennenButtonActionPerformed(evt);
            }
        });

        OnlineClientsScrollPane.setViewportView(OnlineClientsList);

        OnlineClientsLabel.setLabelFor(OnlineClientsList);
        OnlineClientsLabel.setText("Online Clients");

        SendenButton.setText("Senden");
        SendenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendenButtonActionPerformed(evt);
            }
        });

        StatusLabel.setText("Verbunden mit: ");

        javax.swing.GroupLayout ChatRoomPanelLayout = new javax.swing.GroupLayout(ChatRoomPanel);
        ChatRoomPanel.setLayout(ChatRoomPanelLayout);
        ChatRoomPanelLayout.setHorizontalGroup(
            ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StatusSeparator, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(ChatRoomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ChatRoomPanelLayout.createSequentialGroup()
                        .addGroup(ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ChatRoomPanelLayout.createSequentialGroup()
                                .addComponent(MessageTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SendenButton))
                            .addComponent(MessageHistoryScrollPane))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(OnlineClientsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ChatRoomPanelLayout.createSequentialGroup()
                                .addGroup(ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(OnlineClientsLabel)
                                    .addComponent(TrennenButton))
                                .addGap(24, 24, 24))))
                    .addComponent(StatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE))
                .addContainerGap())
        );
        ChatRoomPanelLayout.setVerticalGroup(
            ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ChatRoomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ChatRoomPanelLayout.createSequentialGroup()
                        .addComponent(MessageHistoryScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ChatRoomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(MessageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SendenButton)))
                    .addGroup(ChatRoomPanelLayout.createSequentialGroup()
                        .addComponent(TrennenButton)
                        .addGap(28, 28, 28)
                        .addComponent(OnlineClientsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OnlineClientsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(StatusSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(StatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        ChatRoomTabbedPane.addTab("Chat Room", ChatRoomPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChatRoomTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChatRoomTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SendenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendenButtonActionPerformed
        if ( !this.MessageTextField.getText().equals("") ) {
            Message messageToServer = new Message();
            messageToServer.messageText = this.MessageTextField.getText();
            messageToServer.userNickname = Application.RegisterAndLoginDialog.getUserName();
            Application.SocketHandler.Send(messageToServer);
            this.MessageTextField.setText("");
        }
    }//GEN-LAST:event_SendenButtonActionPerformed

    private void TrennenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrennenButtonActionPerformed
        Application.SocketHandler.StopCommunicationTasks();
        Application.SocketHandler.CloseSocketConnection();
        this.dispose();
        Application.RegisterAndLoginDialog.setVisible(true);
    }//GEN-LAST:event_TrennenButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ChatRoomPanel;
    private javax.swing.JTabbedPane ChatRoomTabbedPane;
    private javax.swing.JScrollPane MessageHistoryScrollPane;
    private javax.swing.JTextPane MessageHistoryTextPane;
    private javax.swing.JTextField MessageTextField;
    private javax.swing.JLabel OnlineClientsLabel;
    private javax.swing.JList OnlineClientsList;
    private javax.swing.JScrollPane OnlineClientsScrollPane;
    private javax.swing.JButton SendenButton;
    private javax.swing.JLabel StatusLabel;
    private javax.swing.JSeparator StatusSeparator;
    private javax.swing.JButton TrennenButton;
    // End of variables declaration//GEN-END:variables
}

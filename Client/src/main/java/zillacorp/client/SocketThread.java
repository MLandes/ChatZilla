/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import zillacorp.model.Message;

/**
 *
 * @author Martin
 */
public class SocketThread implements Runnable {
    
    private Thread baseThread;
    private volatile boolean isThreadRunning;
    private Socket clientSocket;
    
    public ConcurrentLinkedQueue<Message> OutgoingMessages = new ConcurrentLinkedQueue<>();
    
    
    public boolean tryConnectToServerSocket() {
        try {
            String serverIp = Application.RegisterAndLoginDialog.getServerIp();
            this.clientSocket = new Socket(serverIp, 4321);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    public void closeSocketConnection() {
        try {
            this.clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Methode, die in dem erstellten Thread ausgeführt wird.
     */
    @Override
    public void run() {
        this.handleAuthentification();
        this.handleMessageHistory();
        while(this.isThreadRunning && !clientSocket.isClosed()) { //ToDo: isClosed und co. geben nur Aufschluss über eigenen Socket
            this.handleOnlineClients();
            this.handleIncomingMessages();
            this.handleOutgoingMessages();
        }
        if (this.isThreadRunning && clientSocket.isClosed() && Application.ChatFrame.isVisible()) { //ToDo: isClosed und co. geben nur Aufschluss über eigenen Socket
            JOptionPane.showMessageDialog(
                    Application.ChatFrame,
                    "Die Verbindung zum Server ist abgebrochen.\nSie können Trennen klicken und versuchen neu zu verbinden!",
                    "Connection Problem",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void handleAuthentification() {
        
    }
    private void handleMessageHistory() {
        if (Application.RegisterAndLoginDialog.isMessageHistoryRequested()) {
            //Application.RegisterAndLoginDialog.getTimestampForBeginningMessageHistory();
        }
    }
    private void handleOnlineClients() {
        
    }
    private void handleIncomingMessages() {
        
    }
    private void handleOutgoingMessages() {
        if (!this.OutgoingMessages.isEmpty()) {
            //Message SentMessage = this.OutgoingMessages.poll();
        }
    }
    
    /**
     * Startet den Thread.
     */
    public void start() {
        if (this.baseThread == null) {
            this.baseThread = new Thread(this);
            this.isThreadRunning = true;
            this.baseThread.start();
        }
    }
    /**
     * Beendet den Thread.
     */
    public void terminate() {
        if (this.baseThread != null) {
            try {
                this.isThreadRunning = false;
                this.baseThread.join();
                // Alternative: this.baseThread.interrupt();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketThread.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.baseThread = null;
            }
        }
    }
    
}

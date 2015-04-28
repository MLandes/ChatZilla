/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import zillacorp.model.Message;

/**
 *
 * @author Martin
 */
public class SocketHandler implements Runnable {
    
    private Thread baseThread;
    private volatile boolean isThreadAllowedToRun;
    private volatile Socket clientSocket;
    
    public ConcurrentLinkedQueue<Message> OutgoingMessages = new ConcurrentLinkedQueue<>();
    
    
    public boolean TryConnectToServerSocket() {
        try {
            String serverIp = Application.RegisterAndLoginDialog.getServerIp();
            this.CloseSocketConnection();
            this.clientSocket = new Socket(serverIp, 4321);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    public void CloseSocketConnection() {
        try {
            if (this.clientSocket != null) {
                this.clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void StartSocketThread() {
        if (this.baseThread == null) {
            this.baseThread = new Thread(this);
            this.isThreadAllowedToRun = true;
            this.baseThread.start();
        }
    }
    public void TerminateSocketThread() {
        if (this.baseThread != null) {
            try {
                this.isThreadAllowedToRun = false;
                this.baseThread.join();
                // Alternative: this.baseThread.interrupt();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.baseThread = null;
            }
        }
    }
    
    /**
     * Methode, die in dem erstellten Thread ausgeführt wird.
     */
    @Override
    public void run() {
        try {          
            Scanner inputStream = new Scanner(this.clientSocket.getInputStream());
            PrintWriter outputStream = new PrintWriter(this.clientSocket.getOutputStream(), true);
            
            String inputToken = "neuer thread benötigt"; //inputStream.next();
            
            this.handleAuthentification(inputToken, outputStream);
            this.handleMessageHistory(inputToken,outputStream);
            while(this.isThreadAllowedToRun) {
                this.handleOnlineClients(inputToken, outputStream);
                this.handleIncomingMessages(inputToken);
                this.handleOutgoingMessages(outputStream);
            }
        } catch (IOException ex) {
            if (Application.ChatFrame.isVisible()) {
                JOptionPane.showMessageDialog(
                        Application.ChatFrame,
                        "Die Verbindung zum Server ist abgebrochen.\nSie können Trennen klicken und versuchen neu zu verbinden!",
                        "Connection Problem",
                        JOptionPane.WARNING_MESSAGE);
            }
            this.CloseSocketConnection();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                        Application.RegisterAndLoginDialog,
                        ex.toString(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            this.CloseSocketConnection();
            Application.ChatFrame.dispose();
            Application.RegisterAndLoginDialog.setVisible(true);
        }
    }
    
    
    private void handleAuthentification(String inputToken, PrintWriter outputStream) {
        
    }
    
    private void handleMessageHistory(String inputToken, PrintWriter outputStream) {
        if (Application.RegisterAndLoginDialog.isMessageHistoryRequested()) {
            //Application.RegisterAndLoginDialog.getTimestampForBeginningMessageHistory();
        }
    }
    
    private void handleOnlineClients(String inputToken, PrintWriter outputStream) {
        
    }
    
    private void handleIncomingMessages(String inputToken) {
        
    }
    
    private void handleOutgoingMessages(PrintWriter outputStream) {
        if (!this.OutgoingMessages.isEmpty()) {
            //Message SentMessage = this.OutgoingMessages.poll();
        }
    }
    
}

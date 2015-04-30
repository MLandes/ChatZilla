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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import zillacorp.dbModel.Message;
import zillacorp.dbModel.UserOnline;
import zillacorp.dbModel.UserRegistered;
import zillacorp.socketModel.HistoryRequest;
import zillacorp.socketModel.HistoryResponse;
import zillacorp.utils.JsonDeserializer;

/**
 *
 * @author Martin
 */
public class SocketListenerTask implements Runnable {
    
    private Thread baseThread;
    private volatile boolean isThreadAllowedToRun;
    
    private volatile Socket clientSocket;
    private Scanner inputStream;
    
    private boolean hasHistoryResponseBeenReceived = false;
    private Queue<String> incomingTokenQueue = new LinkedList<>();
    
    
    public SocketListenerTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    
    public void StartThread() {
        if (this.baseThread == null) {
            this.baseThread = new Thread(this);
            this.isThreadAllowedToRun = true;
            this.baseThread.start();
        }
    }
    public void TerminateThread() {
        if (this.baseThread != null) {
            try {
                this.isThreadAllowedToRun = false;
                this.baseThread.interrupt();
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
            this.inputStream = new Scanner(this.clientSocket.getInputStream());
            
            while(this.isThreadAllowedToRun) {
                
                String inputToken = inputStream.nextLine();
                this.handleIncomingTokens(inputToken);
                
            }
            
        } catch (IOException ex) {
            if (Application.ChatFrame.isVisible()) {
                JOptionPane.showMessageDialog(
                        Application.ChatFrame,
                        "Die Verbindung zum Server ist abgebrochen.\nSie können Trennen klicken und versuchen neu zu verbinden!",
                        "Connection Problem",
                        JOptionPane.WARNING_MESSAGE);
            }
            Application.SocketHandler.CloseSocketConnection();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                        Application.RegisterAndLoginDialog,
                        ex.toString(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            Application.SocketHandler.CloseSocketConnection();
            Application.ChatFrame.dispose();
            Application.RegisterAndLoginDialog.setVisible(true);
        }
    }
    
    
    private void handleIncomingTokens(String inputToken) {
        if (this.hasHistoryResponseBeenReceived == false) {
            this.incomingTokenQueue.offer(inputToken);
        } else if (this.incomingTokenQueue.isEmpty()) {
            this.deserializeAndReactSpecificTo(inputToken);
        } else {
            while (this.incomingTokenQueue.isEmpty() == false) {                
                this.deserializeAndReactSpecificTo(this.incomingTokenQueue.poll());
            }
        }
    }
    
    private void deserializeAndReactSpecificTo(String token) {
        
        Message inputAsMessage = JsonDeserializer.deserializeMessage(token);
        if (inputAsMessage != null)
        {
            Application.ChatFrame.UpdateMessageHistory(inputAsMessage);
            return;
        }
        
        UserOnline inputAsUserOnline = JsonDeserializer.deserializeUserOnline(token);
        if (inputAsUserOnline != null)
        {
            
            return;
        }
        
        HistoryResponse inputAsHistoryResponse = JsonDeserializer.deserializeHistoryResponse(token);
        if (inputAsHistoryResponse != null)
        {
            ArrayList<Message> messageHistory = inputAsHistoryResponse.messageList;
            Application.ChatFrame.UpdateMessageHistory(messageHistory);
            return;
        }
    }

}

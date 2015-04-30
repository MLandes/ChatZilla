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
                this.deserializeAndReactSpecificTo(inputToken);
                
            }
            
        } catch (Exception ex) {
            if (Application.ChatFrame.isVisible()) {
                JOptionPane.showMessageDialog(
                        Application.ChatFrame,
                        "Die Verbindung zum Server ist abgebrochen.\nEs wird versucht sich neu zu verbinden!",
                        "Connection Problem",
                        JOptionPane.WARNING_MESSAGE);
            }
            Application.SocketHandler.CloseSocketConnection();
            Application.ChatFrame.dispose();
            Application.RegisterAndLoginDialog.ReconnectToServer();
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(
//                        Application.RegisterAndLoginDialog,
//                        ex.toString(),
//                        "Error",
//                        JOptionPane.ERROR_MESSAGE);
//            Application.SocketHandler.CloseSocketConnection();
//            Application.ChatFrame.dispose();
//            Application.RegisterAndLoginDialog.setVisible(true);
        }
    }
    
    private void deserializeAndReactSpecificTo(String inputToken) {
        
        Message inputAsMessage = JsonDeserializer.deserializeMessage(inputToken);
        if (inputAsMessage != null && inputAsMessage.messageText != null)
        {
//            if (Application.RegisterAndLoginDialog.isMessageHistoryRequested() && this.hasHistoryResponseBeenReceived == false) {
//                this.incomingTokenQueue.offer(inputToken);
//            } else {
                Application.ChatFrame.UpdateMessageHistory(inputAsMessage);
//            }
            return;
        }
        
        HistoryResponse inputAsHistoryResponse = JsonDeserializer.deserializeHistoryResponse(inputToken);
        if (inputAsHistoryResponse != null && inputAsHistoryResponse.messageList != null)
        {
            this.hasHistoryResponseBeenReceived = true;
            Application.ChatFrame.UpdateMessageHistory(inputAsHistoryResponse.messageList);
            while (this.incomingTokenQueue.isEmpty() == false) {                
                this.deserializeAndReactSpecificTo(this.incomingTokenQueue.poll());
            }
            return;
        }
        
        UserOnline inputAsUserOnline = JsonDeserializer.deserializeUserOnline(inputToken);
        if (inputAsUserOnline != null && inputAsUserOnline.nickname != null)
        {
            
            return;
        }
        
    }

}

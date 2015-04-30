/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.client;

import com.google.gson.Gson;
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
import zillacorp.dbModel.Message;
import zillacorp.socketModel.HistoryRequest;

/**
 *
 * @author Martin
 */
public class SocketTalkerTask implements Runnable {
    
    private Thread baseThread;
    private volatile boolean isThreadAllowedToRun;
    
    private volatile Socket clientSocket;
    private PrintWriter outputStream;
    public ConcurrentLinkedQueue<Object> OutgoingTokenQueue = new ConcurrentLinkedQueue<>();
    
    
    public SocketTalkerTask(Socket clientSocket) {
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
            this.outputStream = new PrintWriter(this.clientSocket.getOutputStream(), true);
                       
            while(this.isThreadAllowedToRun) {
                
                this.handleOutgoingTokens();
                
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
    
    
    private void handleOutgoingTokens() {
        if (this.OutgoingTokenQueue.isEmpty() == false) {
            Object data = this.OutgoingTokenQueue.poll();
            String token = this.serialize(data);
            outputStream.println(token);
        }
    }
    
    private String serialize(Object data) {
        return new Gson().toJson(data);
    }
    
}

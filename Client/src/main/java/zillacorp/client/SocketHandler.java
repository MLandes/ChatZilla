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
import zillacorp.dbModel.Message;

/**
 *
 * @author Martin
 */
public class SocketHandler {
    
    private volatile Socket clientSocket;
    
    private SocketListenerTask socketListenerTask;
    private SocketTalkerTask socketTalkerTask;
    
    
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
    
    public void StartCommunicationTasks() {
        this.socketListenerTask = new SocketListenerTask(clientSocket);
        this.socketListenerTask.StartThread();
        this.socketTalkerTask = new SocketTalkerTask(clientSocket);
        this.socketTalkerTask.StartThread();
    }
    public void StopCommunicationTasks() {
        this.socketListenerTask.TerminateThread();
        this.socketTalkerTask.TerminateThread();
    }
    
    
    public void Send(Object data) {
        this.socketTalkerTask.OutgoingTokenQueue.offer(data);
    }
}

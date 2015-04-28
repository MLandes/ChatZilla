/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import java.awt.Frame;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author l.hurtz
 */
public class ServerSocketThread extends Thread implements Runnable
{    
    ServerSocket serverSocket;
    public boolean isAllowedToRun = true;
    
    public boolean TryCreateServerSocket()
    {
        try 
        {
            serverSocket = new ServerSocket(4321);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
    
    @Override
    public void run()
    {
        while (isAllowedToRun && !serverSocket.isClosed())
        {
            try 
            {
                Socket clientSocket = serverSocket.accept();
                ServerThread.newlyAcceptedClientSockets.add(clientSocket);
            } catch (Exception e)
            {
                System.out.println("Fehler bei Verbindung von neuem Client");
            }
        }
    }    
}

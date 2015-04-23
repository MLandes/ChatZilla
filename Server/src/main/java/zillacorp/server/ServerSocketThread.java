/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import java.awt.Frame;
import java.io.IOException;
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
    
    public ServerSocketThread()
    {
        try 
        {
            serverSocket = new ServerSocket(4321);
        } catch (Exception e)
        {
            System.out.println("Fehler beim Erstellen des Server Sockets");
            JOptionPane.showMessageDialog(new Frame(), "Fehler beim Erstellen des ServerSockets! Bitte Programm neu starten!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    @Override
    public void run()
    {
        while (true)
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author l.hurtz
 */
public class ServerSocketThread extends Thread implements Runnable
{    
    ServerSocket serverSocket;
    
    public ServerSocketThread() throws IOException
    {
        serverSocket = new ServerSocket(4321);
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

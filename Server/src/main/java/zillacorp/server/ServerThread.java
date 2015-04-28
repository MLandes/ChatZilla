/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JOptionPane;
import zillacorp.model.Message;

/**
 *
 * @author l.hurtz
 */
public class ServerThread extends Thread implements Runnable
{
    ApplicationFrame applicationFrame;
    
    DatabaseMessageThread databaseThread;
    ServerSocketThread serverSocketThread;
    ArrayList<ClientSocketThread> clientSocketThreads;
    
    static ConcurrentLinkedQueue<Message> messagesFromClients;
    static ConcurrentLinkedQueue<Message> messagesFromDatabase;
    static ConcurrentLinkedQueue<Socket> newlyAcceptedClientSockets;
    
    public ServerThread(ApplicationFrame applicationFrame)
    {
        this.setName("ServerThread");
        this.applicationFrame = applicationFrame;
        
        databaseThread = new DatabaseMessageThread();        
        serverSocketThread = new ServerSocketThread();        
        clientSocketThreads = new ArrayList<>();
        
        messagesFromClients = new ConcurrentLinkedQueue<>();
        messagesFromDatabase = new ConcurrentLinkedQueue<>();
        newlyAcceptedClientSockets = new ConcurrentLinkedQueue<>();
    }
    
    public boolean tryInitializeDatabaseConnectionAndServerSocket(String databaseIp)
    {
        if (!databaseThread.tryConnectToMessageDatbase(databaseIp))
        {
            JOptionPane.showMessageDialog(applicationFrame,
                    "Datenbank nicht erreichbar. Bitte trennen und erneut verbinden.", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            
            return false;
        }
        databaseThread.start();
        
        serverSocketThread = new ServerSocketThread();
        if (!serverSocketThread.TryCreateServerSocket())
        {
            JOptionPane.showMessageDialog(applicationFrame,
                    "Server Socket konnte nicht erstellt werden. Bitte trennen und erneut verbinden.", 
                    "Server Socket Error", 
                    JOptionPane.ERROR_MESSAGE);
            
            databaseThread.interrupt();
            return false;
        }
        serverSocketThread.start();
        
        return true;
    }
    
    public void run()
    {
        while (true) 
        {
            HandleNewClients();
            HandleNewMessagesFromClients();
            HandleNewMessagesFromDatabase();            
        }
        
    }

    private void HandleNewClients()
    {
        if (newlyAcceptedClientSockets.size() > 0)
        {
            Socket clientSocket = newlyAcceptedClientSockets.poll();
            
            ClientSocketThread newClientSocketThread = new ClientSocketThread(clientSocket);
            if (newClientSocketThread.retrieveStreamsFromClients())
            {
                newClientSocketThread.start();
                clientSocketThreads.add(newClientSocketThread); 
            }
            else
            {
                clientSocket.close();
                newClientSocketThread.interrupt();
            }      
        }
    }

    private void HandleNewMessagesFromClients()
    {
        if (messagesFromClients.size() > 0)
        {
            Message newMessageFromClient = messagesFromClients.poll();
            databaseThread.sendToDatabase(newMessageFromClient);
        }
    }

    private void HandleNewMessagesFromDatabase()
    {
        if (messagesFromDatabase.size() > 0)
        {
            Message newMessageFromDatabase = messagesFromDatabase.poll();
            
            for (ClientSocketThread client : clientSocketThreads)
            {
                client.sendMessageToClient(newMessageFromDatabase);
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import zillacorp.model.Message;

/**
 *
 * @author l.hurtz
 */
public class ServerThread extends Thread implements Runnable
{
    DatabaseMessageThread databaseThread;
    ServerSocketThread serverSocketThread;
    ArrayList<ClientSocketThread> clientSocketThreads;
    
    static ConcurrentLinkedQueue<Message> messagesFromClients;
    static ConcurrentLinkedQueue<Message> messagesFromDatabase;
    static ConcurrentLinkedQueue<Socket> newlyAcceptedClientSockets;
    
    public ServerThread(String databaseIp)
    {
        databaseThread = new DatabaseMessageThread(databaseIp);
        databaseThread.start();
        
        serverSocketThread = new ServerSocketThread();
        serverSocketThread.start();
        
        clientSocketThreads = new ArrayList<>();
        
        messagesFromClients = new ConcurrentLinkedQueue<>();
        messagesFromDatabase = new ConcurrentLinkedQueue<>();
        newlyAcceptedClientSockets = new ConcurrentLinkedQueue<>();
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
            try 
            {
                ClientSocketThread newClientSocketThread = new ClientSocketThread(clientSocket);
                newClientSocketThread.start();
                clientSocketThreads.add(newClientSocketThread);                
            } catch (Exception e) 
            {
                System.out.println("Fehler beim abgreifen des InputStreams des neuen Clients. CLient wurde abgelehnt.");
                try 
                {
                    clientSocket.close();                    
                } catch (Exception ex) 
                {
                    System.out.println("Fehler beim abgreifen des InputStreams des neuen Clients. CLient wurde abgelehnt.");
                }                
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

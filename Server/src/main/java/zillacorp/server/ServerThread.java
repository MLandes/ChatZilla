/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import zillacorp.dbModel.Message;
import zillacorp.socketModel.HistoryRequest;

/**
 *
 * @author l.hurtz
 */
public class ServerThread extends Thread implements Runnable
{
    static ApplicationFrame applicationFrame;
    boolean isAllowedToRun = true;
    
    static DatabaseMessageThread databaseMessageThread;
    static ServerSocketThread serverSocketThread;
    static ArrayList<ClientSocketThread> clientSocketThreads;
    
    static ConcurrentLinkedQueue<Message> messagesFromClients;
    static ConcurrentLinkedQueue<Message> messagesFromDatabase;
    static ConcurrentLinkedQueue<Socket> newlyAcceptedClientSockets;
    
    public ServerThread(ApplicationFrame applicationFrame)
    {
        this.setName("ServerThread");
        this.applicationFrame = applicationFrame;
        
        databaseMessageThread = new DatabaseMessageThread();        
        serverSocketThread = new ServerSocketThread();        
        clientSocketThreads = new ArrayList<>();
        
        messagesFromClients = new ConcurrentLinkedQueue<>();
        messagesFromDatabase = new ConcurrentLinkedQueue<>();
        newlyAcceptedClientSockets = new ConcurrentLinkedQueue<>();
    }
    
    public boolean tryInitializeDatabaseConnectionAndServerSocket(String databaseIp)
    {
        if (!databaseMessageThread.tryConnectToMessageDatbase(databaseIp))
        {
            JOptionPane.showMessageDialog(applicationFrame,
                    "Datenbank nicht erreichbar. Bitte zu anderer IP verbinden.", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            
            return false;
        }
        databaseMessageThread.start();
        
        serverSocketThread = new ServerSocketThread();
        if (!serverSocketThread.TryCreateServerSocket())
        {
            JOptionPane.showMessageDialog(applicationFrame,
                    "Server Socket konnte nicht erstellt werden. Bitte erneut verbinden.", 
                    "Server Socket Error", 
                    JOptionPane.ERROR_MESSAGE);
            
            databaseMessageThread.interrupt();
            return false;
        }
        serverSocketThread.start();
        
        return true;
    }
    
    static ArrayList<Message> getRequestedHistory(HistoryRequest historyRequest)
    {
        ArrayList<Message> messageHistory = databaseMessageThread.getHistorySince(historyRequest);
        return messageHistory;
    }
    
    @Override
    public void run()
    {
        while (isAllowedToRun) 
        {
            HandleNewClients();
            HandleNewMessagesFromClients();
            HandleNewMessagesFromDatabase();            
        }
        
        TerminateThisThread();        
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
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                newClientSocketThread.interrupt();
            }      
        }
    }

    private void HandleNewMessagesFromClients()
    {
        if (messagesFromClients.size() > 0)
        {
            Message newMessageFromClient = messagesFromClients.poll();
            databaseMessageThread.sendToDatabase(newMessageFromClient);
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

    public static void TerminateThisThread()
    {
        try
        {
            for (ClientSocketThread client : clientSocketThreads)
            {
                client.interrupt();
                try 
                {
                    client.clientSocket.close();
                } catch (IOException ex) 
                {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                clientSocketThreads.remove(client);
            }

            serverSocketThread.interrupt();
            try 
            {
                serverSocketThread.serverSocket.close();
            } catch (IOException ex) 
            {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            databaseMessageThread.interrupt();
            databaseMessageThread.messageDatabaseChangesClient.shutdown();
            databaseMessageThread.messageDatabaseClient.shutdown();
        } catch (Exception ex) 
        {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

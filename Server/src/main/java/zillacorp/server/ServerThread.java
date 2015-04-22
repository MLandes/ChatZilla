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
    DatabaseThread databaseThread;
    ServerSocketHandlerThread serverSocketHandlerThread;
    ArrayList<ClientSocketThread> clientSocketThreads;
    
    ConcurrentLinkedQueue<Message> messagesFromClients;
    ConcurrentLinkedQueue<Message> messagesFromDatabase;
    ConcurrentLinkedQueue<Socket> newlyAcceptedClientSockets;
    
    public ServerThread(String DatabaseIp)
    {
        
    }
    
    public void run()
    {
        //TODO
    }
}

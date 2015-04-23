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
    ServerSocketThread serverSocketHandlerThread;
    ArrayList<ClientSocketThread> clientSocketThreads;
    
    static ConcurrentLinkedQueue<Message> messagesFromClients;
    static ConcurrentLinkedQueue<Message> messagesFromDatabase;
    static ConcurrentLinkedQueue<Socket> newlyAcceptedClientSockets;
    
    public ServerThread(String databaseIp)
    {
        databaseThread = new DatabaseMessageThread(databaseIp);
    }
    
    public void run()
    {
        //TODO
    }
}

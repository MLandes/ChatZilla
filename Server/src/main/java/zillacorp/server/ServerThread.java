/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author l.hurtz
 */
public class ServerThread extends Thread implements Runnable
{
    DatabaseThread databaseThread;
    ServerSocketHandlerThread serverSocketHandlerThread;
    ArrayList<ClientSocketThread> clientSocketThreads;
    
    public ServerThread(String DatabaseIp)
    {
        
    }
    
    public void run()
    {
        //TODO
    }
}

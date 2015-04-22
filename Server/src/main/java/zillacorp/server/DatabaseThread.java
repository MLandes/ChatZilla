/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import zillacorp.model.Message;

/**
 *
 * @author l.hurtz
 */
public class DatabaseThread extends Thread implements Runnable
{
    CouchDbClient messageDatabaseClient;
    CouchDbClient userRegisteredDatabaseClient;
    CouchDbClient userOnlineDatabaseClient;
    
    public DatabaseThread(String databaseIp)
    {
        connectToMessageDatabse(databaseIp);
        connectToUserRegisteredDatabse(databaseIp);
        connectToUserOnlineDatabse(databaseIp);
    }
    
    public void run()
    {
        
    }

    private void connectToMessageDatabse(String databaseIp)
    {
        CouchDbProperties properties = new CouchDbProperties()
            .setDbName("chatzilla_message-history")
            .setCreateDbIfNotExist(true)
            .setProtocol("http")
            .setHost(databaseIp)
            .setPort(5984)
            .setMaxConnections(100)
            .setConnectionTimeout(0);
        
        messageDatabaseClient = new CouchDbClient(properties);
    }
    
    private void connectToUserRegisteredDatabse(String databaseIp)
    {
        CouchDbProperties properties = new CouchDbProperties()
            .setDbName("chatzilla_user-registered")
            .setCreateDbIfNotExist(true)
            .setProtocol("http")
            .setHost(databaseIp)
            .setPort(5984)
            .setMaxConnections(100)
            .setConnectionTimeout(0);
        
        userRegisteredDatabaseClient = new CouchDbClient(properties);
    }
    
    private void connectToUserOnlineDatabse(String databaseIp)
    {
        CouchDbProperties properties = new CouchDbProperties()
            .setDbName("chatzilla_user-online")
            .setCreateDbIfNotExist(true)
            .setProtocol("http")
            .setHost(databaseIp)
            .setPort(5984)
            .setMaxConnections(100)
            .setConnectionTimeout(0);
        
        userOnlineDatabaseClient = new CouchDbClient(properties);
    }
    
    public void sendToDatabase(Message message)
    {
        messageDatabaseClient.save(message);
    }
}

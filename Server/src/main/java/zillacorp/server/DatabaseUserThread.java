/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import org.lightcouch.Changes;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;

/**
 *
 * @author l.hurtz
 */
public class DatabaseUserThread extends Thread implements Runnable
{
    CouchDbClient userRegisteredDatabaseClient;
    CouchDbClient userOnlineDatabaseClient;
    
    public DatabaseUserThread(String databaseIp)
    {
        connectToUserRegisteredDatabse(databaseIp);
        connectToUserOnlineDatabse(databaseIp);
    }
    
    public void Run()
    {
        
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
    
    private Changes getUserOnlineChangesFeed() 
    {
        String lastUpdateSequence = userOnlineDatabaseClient.context().info().getUpdateSeq();
        
        return userOnlineDatabaseClient.changes()
                .includeDocs(true)
                .since(lastUpdateSequence)
                .heartBeat(30000)
                .continuousChanges();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import org.lightcouch.Changes;
import org.lightcouch.ChangesResult;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import zillacorp.model.Message;

/**
 *
 * @author l.hurtz
 */
public class DatabaseMessageThread extends Thread implements Runnable
{
    CouchDbClient messageDatabaseClient;
    
    
    public DatabaseMessageThread(String databaseIp)
    {
        connectToMessageDatbase(databaseIp);
    }
    
    public void run()
    {
        Changes messageChangesFeed = getMessageChangesFeed();
        
        while(messageChangesFeed.hasNext())
        {
            if(messageChangesFeed.next() != null)
            {
                JsonObject serializedMessage = messageChangesFeed.next().getDoc().getAsJsonObject();
                Message message = deserializeMessage(serializedMessage);
                ServerThread.messagesFromDatabase.add(message);
            }
        }
    }
    
    public void sendToDatabase(Message message)
    {
        messageDatabaseClient.save(message);
    }
    
    public ArrayList<Message> getHistorySince(long timeStamp)
    {
        
    }

    private Changes getMessageChangesFeed() 
    {
        String lastUpdateSequence = messageDatabaseClient.context().info().getUpdateSeq();
        
        return messageDatabaseClient.changes()
                .includeDocs(true)
                .since(lastUpdateSequence)
                .heartBeat(30000)
                .continuousChanges();
    }    

    private void connectToMessageDatbase(String databaseIp)
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
    
    

    private Message deserializeMessage(JsonObject serializedMessage) {
        Gson gson = messageDatabaseClient.getGson();
        
        return gson.fromJson(serializedMessage, Message.class);
    }
}

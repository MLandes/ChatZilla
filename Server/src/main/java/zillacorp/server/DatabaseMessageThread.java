/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.lightcouch.Changes;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import zillacorp.dbModel.Message;
import zillacorp.utils.JsonDeserializer;
import zillacorp.utils.MessageSorter;

/**
 *
 * @author l.hurtz
 */
public class DatabaseMessageThread extends Thread implements Runnable
{
    CouchDbClient messageDatabaseClient;
    
    public DatabaseMessageThread()
    {
        this.setName("DatabaseMessageThread");
    }
    
    @Override
    public void run()
    {
        Changes messageChangesFeed = getMessageChangesFeed();
        
        while(messageChangesFeed.hasNext())
        {
            if(messageChangesFeed.next() != null)
            {
                JsonObject serializedMessage = messageChangesFeed.next().getDoc().getAsJsonObject();
                Message message = JsonDeserializer.deserializeMessage(serializedMessage.getAsString());
                ServerThread.messagesFromDatabase.add(message);
            }
        }
    }
    
    public void sendToDatabase(Message message)
    {
        message.serverTimeStamp = Date.from(Instant.now()).getTime();
        
        messageDatabaseClient.save(message);
    }
    
    public ArrayList<Message> getHistorySince(long sinceTimeStamp)
    {
        List<JsonObject> allDocuments = getAllMessageDocumentsFromDatabase();
        
        ArrayList<Message> history = convertAndFilterDocumentsToMessages(allDocuments, sinceTimeStamp);
        
        Collections.sort(history, new MessageSorter());
        
        return history;
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

    public boolean tryConnectToMessageDatbase(String databaseIp)
    {
        CouchDbProperties properties = new CouchDbProperties()
            .setDbName("chatzilla_message-history")
            .setCreateDbIfNotExist(true)
            .setProtocol("http")
            .setHost(databaseIp)
            .setPort(5984)
            .setMaxConnections(100)
            .setConnectionTimeout(0);
        try
        {
            messageDatabaseClient = new CouchDbClient(properties);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
        
    }
    
    private List<JsonObject> getAllMessageDocumentsFromDatabase()
    {
        return messageDatabaseClient.view("_all_docs")
                .includeDocs(true)
                .query(JsonObject.class);
    }
    
    private ArrayList<Message> convertAndFilterDocumentsToMessages(List<JsonObject> allDocuments, long sinceTimeStamp)
    {
        ArrayList<Message> history = new ArrayList<>();
        
        for(JsonObject document : allDocuments)
        {
            Message messageFromDocument = JsonDeserializer.deserializeMessage(document.getAsString());
            if (messageFromDocument.serverTimeStamp >= sinceTimeStamp)
            {
                history.add(messageFromDocument);
            }
        }
        
        return history;
    }
}
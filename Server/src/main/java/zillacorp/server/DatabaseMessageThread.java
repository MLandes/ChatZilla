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
import javax.swing.JOptionPane;
import org.lightcouch.Changes;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import zillacorp.dbModel.Message;
import zillacorp.socketModel.HistoryRequest;
import zillacorp.utils.JsonDeserializer;
import zillacorp.utils.MessageSorter;

/**
 *
 * @author l.hurtz
 */
public class DatabaseMessageThread extends Thread implements Runnable
{
    CouchDbClient messageDatabaseChangesClient;
    CouchDbClient messageDatabaseClient;
    
    public DatabaseMessageThread()
    {
        this.setName("DatabaseMessageThread");
    }
    
    @Override
    public void run()
    {
        Changes messageChangesFeed = getMessageChangesFeed();
        
        try
        {
            while(messageChangesFeed.hasNext())
            {
                if(messageChangesFeed.next() != null)
                {
                    JsonObject serializedMessage = messageChangesFeed.next().getDoc().getAsJsonObject();
                    Message message = JsonDeserializer.deserializeMessage(serializedMessage.toString());
                    ServerThread.messagesFromDatabase.add(message);
                }
            }
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ServerThread.applicationFrame,
                    "Datenbank nicht erreichbar. Bitte zu anderer IP verbinden.", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            
            ServerThread.TerminateThisThread();
        }
    }
    
    public void sendToDatabase(Message message)
    {
        message.serverTimeStamp = Date.from(Instant.now()).getTime();
        
        messageDatabaseClient.save(message);
    }
    
    public ArrayList<Message> getHistorySince(HistoryRequest historyRequest)
    {
        List<JsonObject> allDocuments = getAllMessageDocumentsFromDatabase();
        
        ArrayList<Message> history = convertAndFilterDocumentsToMessages(allDocuments, historyRequest.historyTimestamp);
        
        Collections.sort(history, new MessageSorter());
        
        return history;
    }

    private Changes getMessageChangesFeed() 
    {
        String lastUpdateSequence = messageDatabaseChangesClient.context().info().getUpdateSeq();
        
        return messageDatabaseChangesClient.changes()
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
            messageDatabaseChangesClient = new CouchDbClient(properties);
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
        return messageDatabaseChangesClient.view("_all_docs")
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
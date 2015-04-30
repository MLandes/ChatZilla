/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.server;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import zillacorp.dbModel.Message;
import zillacorp.dbModel.UserOnline;
import zillacorp.dbModel.UserRegistered;
import zillacorp.socketModel.HistoryRequest;
import zillacorp.utils.JsonDeserializer;

/**
 *
 * @author l.hurtz
 */
public class ClientSocketThread extends Thread implements Runnable
{
    Socket clientSocket;
    Scanner inputFromClient;
    PrintWriter outputToClient;
    
    private volatile boolean hasHistoryBeenSent = false;
    
    
    public ClientSocketThread(Socket clientSocket)
    {
        this.setName("ClientSocketThread");
        
        this.clientSocket = clientSocket;
    }
    
    public boolean retrieveStreamsFromClients()
    {
        try 
        {
            inputFromClient = new Scanner(clientSocket.getInputStream());
            outputToClient = new PrintWriter(clientSocket.getOutputStream());     
            return true;
        } catch (Exception e) 
        {
            return false;
        }
        
    }
    
    @Override
    public void run()
    {
        String inputAsJson;
        while(true)
        {            
            inputAsJson = inputFromClient.nextLine();
            
            deserializeAndHandleInput(inputAsJson);
        }
    }
    
    public void sendMessageToClient(Message message)
    {
        while (!hasHistoryBeenSent) {            
            //wait
        }
        String serializedMessage = new Gson().toJson(message);
        outputToClient.println(serializedMessage);
    }
    public void sendHistoryToClient(ArrayList<Message> messageHistory)
    {
        String serializedHistory = new Gson().toJson(messageHistory);
        outputToClient.println(serializedHistory);
        hasHistoryBeenSent = true;
    }

    private void deserializeAndHandleInput(String inputAsJson) 
    {
        Message inputAsMessage = JsonDeserializer.deserializeMessage(inputAsJson);
        if (inputAsMessage != null)
        {
            handleMessage(inputAsMessage);
            return;
        }
        
        UserOnline inputAsUserOnline = JsonDeserializer.deserializeUserOnline(inputAsJson);
        if (inputAsUserOnline != null)
        {
            handleUserOnline(inputAsUserOnline);
            return;
        }
        
        UserRegistered inputAsUserRegistered = JsonDeserializer.deserializeUserRegistered(inputAsJson);
        if (inputAsUserRegistered != null)
        {
            handleUserRegistered(inputAsUserRegistered);
            return;
        }
        
        HistoryRequest inputAsHistoryRequest = JsonDeserializer.deserializeHistoryRequest(inputAsJson);
        if (inputAsUserRegistered != null)
        {
            handleHistoryRequest(inputAsHistoryRequest);
            return;
        }
        
        
    }

    private void handleMessage(Message inputAsMessage)
    {
        ServerThread.messagesFromClients.add(inputAsMessage);
    }

    private void handleUserOnline(UserOnline inputAsUserOnline)
    {
        
    }

    private void handleUserRegistered(UserRegistered inputAsUserRegistered)
    {
        
    }

    private void handleHistoryRequest(HistoryRequest inputAsHistoryRequest)
    {
        ArrayList<Message> messageHistory = ServerThread.getRequestedHistory(inputAsHistoryRequest);

        sendHistoryToClient(messageHistory);
    }
}

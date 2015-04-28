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
        String serializedMessage = new Gson().toJson(message);
        
        outputToClient.println(serializedMessage);
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
            handleUserOnline(inputAsMessage);
            return;
        }
        
        UserRegistered inputAsUserRegistered = JsonDeserializer.deserializeUserRegistered(inputAsJson);
        if (inputAsUserRegistered != null)
        {
            handleUserRegistered(inputAsMessage);
            return;
        }
        
        HistoryRequest inputAsHistoryRequest = JsonDeserializer.deserializeHistoryRequest(inputAsJson);
        if (inputAsUserRegistered != null)
        {
            handleHistoryRequest(inputAsMessage);
            return;
        }
        
        
    }

    private void handleMessage(Message inputAsMessage)
    {
        ServerThread.messagesFromClients.add(inputAsMessage);
    }

    private void handleUserOnline(Message inputAsUserOnline)
    {
        
    }

    private void handleUserRegistered(Message inputAsUserRegistered)
    {
        
    }

    private void handleHistoryRequest(Message inputAsHistoryRequest)
    {
        
    }
}

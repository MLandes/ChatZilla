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
import zillacorp.model.Message;
import zillacorp.utils.MessageDeserializer;

/**
 *
 * @author l.hurtz
 */
public class ClientSocketThread extends Thread implements Runnable
{
    Socket clientSocket;
    Scanner inputFromClient;
    PrintWriter outputToClient;
    
    public ClientSocketThread(Socket clientSocket) throws IOException
    {
        this.clientSocket = clientSocket;
        inputFromClient = new Scanner(clientSocket.getInputStream());
        outputToClient = new PrintWriter(clientSocket.getOutputStream());
    }
    
    @Override
    public void run()
    {
        String newMessageAsJson;
        while(true)
        {
            Message newMessage = new Message();
            
            newMessageAsJson = inputFromClient.nextLine();  
            
            newMessage = MessageDeserializer.deserializeMessage(newMessageAsJson);
            
            ServerThread.messagesFromClients.add(newMessage);
        }
    }
    
    public void sendMessageToClient(Message message)
    {
        String serializedMessage = new Gson().toJson(message);
        
        outputToClient.println(serializedMessage);
    }
}

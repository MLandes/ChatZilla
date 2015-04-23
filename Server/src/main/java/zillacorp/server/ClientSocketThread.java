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

/**
 *
 * @author l.hurtz
 */
public class ClientSocketThread extends Thread implements Runnable
{
    Scanner inputFromClient;
    PrintWriter outputToClient;
    String userNickname;
    
    public ClientSocketThread(Socket clientSocket) throws IOException
    {
        inputFromClient = new Scanner(clientSocket.getInputStream());
        outputToClient = new PrintWriter(clientSocket.getOutputStream());
        userNickname = inputFromClient.nextLine();
    }
    
    @Override
    public void run()
    {
        String newMessageText;
        while(true)
        {
            Message newMessage = new Message();
            
            newMessageText = inputFromClient.nextLine();  
            
            newMessage.message = newMessageText;
            newMessage.userNickname = userNickname;
            
            ServerThread.messagesFromClients.add(newMessage);
        }
    }
    
    public void sendMessageToClient(Message message)
    {
        String serializesMessage = new Gson().toJson(message);
        
        outputToClient.println(serializesMessage);
    }
}

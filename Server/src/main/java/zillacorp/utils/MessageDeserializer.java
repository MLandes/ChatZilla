/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.utils;

import com.google.gson.Gson;
import zillacorp.model.Message;

/**
 *
 * @author l.hurtz
 */
public class MessageDeserializer 
{
    
    public static Message deserializeMessage(String serializedMessage) 
    {
        Gson gson = new Gson();
        
        return gson.fromJson(serializedMessage, Message.class);
    }
    
}

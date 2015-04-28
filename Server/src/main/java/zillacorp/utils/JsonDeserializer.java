/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.utils;

import com.google.gson.Gson;
import zillacorp.dbModel.Message;
import zillacorp.dbModel.UserOnline;
import zillacorp.dbModel.UserRegistered;
import zillacorp.socketModel.HistoryRequest;

/**
 *
 * @author l.hurtz
 */
public class JsonDeserializer 
{
    
    public static Message deserializeMessage(String serializedMessage) 
    {
        Gson gson = new Gson();
        
        try 
        {
            Message message = gson.fromJson(serializedMessage, Message.class);
            return message;
        } catch (Exception e) 
        {
            return null;
        }
    }
    
    public static UserOnline deserializeUserOnline(String serializedMessage) 
    {
        Gson gson = new Gson();
        
        try 
        {
            UserOnline userOnline = gson.fromJson(serializedMessage, UserOnline.class);
            return userOnline;
        } catch (Exception e) 
        {
            return null;
        }
    }
    
    public static UserRegistered deserializeUserRegistered(String serializedMessage) 
    {
        Gson gson = new Gson();
        
        try 
        {
            UserRegistered userRegistered = gson.fromJson(serializedMessage, UserRegistered.class);
            return userRegistered;
        } catch (Exception e) 
        {
            return null;
        }
    }
    
    public static HistoryRequest deserializeHistoryRequest(String serializedMessage) 
    {
        Gson gson = new Gson();
        
        try 
        {
            HistoryRequest historyRequest = gson.fromJson(serializedMessage, HistoryRequest.class);
            return historyRequest;
        } catch (Exception e) 
        {
            return null;
        }
    }
    
}

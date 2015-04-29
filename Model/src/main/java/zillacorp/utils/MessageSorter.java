/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.utils;

import java.util.Comparator;
import zillacorp.dbModel.Message;

/**
 *
 * @author l.hurtz
 */
public class MessageSorter implements Comparator<Message>
{
    @Override
    public int compare(Message firstMessage, Message secondMessage)
    {
        return (int)(firstMessage.serverTimeStamp - firstMessage.serverTimeStamp);
    }    
}

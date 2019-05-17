package entity;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;

public class MsgQueue {
	 private Map<String, ChatMessage> msgQueue=new HashMap<String,ChatMessage>();
	 public void addMsg(String userName, ChatMessage chatMessage) {
		 msgQueue.put(userName, chatMessage);
	 }
	 
	 public ChatMessage getChatMessage(String username){
	        return msgQueue.get(username);
	 }
	 
	public Map<String, ChatMessage> getMsgQueue() {
	    return msgQueue;
	} 
	
	 public void removeMsgQueue(String username){
		  for (Map.Entry<String, ChatMessage> entry : msgQueue.entrySet()) {
			  System.out.println(entry.getKey() + "  username:--->" + username);
	            if(entry.getKey().equals(username)){
	            	msgQueue.remove(entry.getKey());
	            	System.out.println("移出消息组成功");
	            }
	        }
	 }	
}

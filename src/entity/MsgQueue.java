package entity;

import java.util.HashMap;
import java.util.Map;


public class MsgQueue {
	 private Map<String, Packet> msgQueue=new HashMap<String,Packet>();
	 public void addMsg(String userName, Packet packet) {
		 msgQueue.put(userName, packet);
	 }
	 
	 public Packet getChatMessage(String username){
	        return msgQueue.get(username);
	 }
	 
	public Map<String, Packet> getMsgQueue() {
	    return msgQueue;
	} 
	
	 public void removeMsgQueue(String username){
		  for (Map.Entry<String, Packet> entry : msgQueue.entrySet()) {
			  System.out.println(entry.getKey() + "  username:--->" + username);
	            if(entry.getKey().equals(username)){
	            	msgQueue.remove(entry.getKey());
	            	System.out.println("移出消息组成功");
	            }
	        }
	 }	
}

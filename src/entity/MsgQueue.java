package entity;

import java.util.*;


public class MsgQueue {
	 private Map<String, List<Packet>> msgQueue=new HashMap<>();
	 public void addMsg(String userName, Packet packet) {
		 if (!msgQueue.containsKey(userName)) {
		 	msgQueue.put(userName, new ArrayList<>());
		 }
		 List<Packet> list = msgQueue.get(userName);
		 list.add(packet);
	 }
	 
	 public List<Packet> getChatMessage(String username){
	        return msgQueue.get(username);
	 }
	 
	public Map<String, List<Packet>> getMsgQueue() {
	    return msgQueue;
	} 
	
	 public void removeMsgQueue(String username){
		  for (Map.Entry<String, List<Packet>> entry : msgQueue.entrySet()) {
			  System.out.println(entry.getKey() + "  username:--->" + username);
	            if(entry.getKey().equals(username)){
	            	msgQueue.remove(entry.getKey());
	            	System.out.println("移出消息组成功");
	            }
	        }
	 }	
}

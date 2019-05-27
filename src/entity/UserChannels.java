package entity;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  用于将在线用户的用户名和对应的通道channel相匹配
 *
 */
public class UserChannels {

    private Map<String, ArrayList<String>>  userFriends = new HashMap<String, ArrayList<String>>();

    private Map<String, Channel> onlineUsers=new HashMap<String,Channel>();

    public void addUserFriend(String userName, String friendName) {
        if (userFriends.containsKey(userName)) {
            List<String> temp = userFriends.get(userName);
            temp.add(friendName);
        } else {
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(friendName);
            userFriends.put(userName, temp);
        }
    }

    public boolean searchUserFriend(String userName, String friendName) {
        for (Map.Entry<String, ArrayList<String>> entry : userFriends.entrySet()) {
            if (entry.getKey().equals(userName)) {
                List<String> temp = entry.getValue();
                if (temp.indexOf(friendName) != -1) {
                  return true;
                }
            }
        }
        return false;
    }
    /**
     *     添加在线的用户
     * @param username 用户名
     * @param channel 该用户对应的channel通道
     */
    public void addOnlineUser(String username,Channel channel){
        onlineUsers.put(username,channel);
    }
    /**
     * 移除用户
     * @param username 要移除的用户的用户名
     */
    public void removeOnlineUser(String username){
        onlineUsers.remove(username);
    }
    /**
     *  移除channel通道
     * @param channel
     */
    public void removeChannel(Channel channel){
        for (Map.Entry<String, Channel> entry : onlineUsers.entrySet()) {
            if(entry.getValue()==channel){
                onlineUsers.remove(entry.getKey());
            }
        }
    }
    public Channel getChannel(String username){
        return onlineUsers.get(username);
    }



    public Map<String, Channel> getOnlineUsers() {
        return onlineUsers;
    }
 
}
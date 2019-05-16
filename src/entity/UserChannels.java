package entity;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;

/**
 *  用于将在线用户的用户名和对应的通道channel相匹配
 * @author zhuhaipeng
 *
 */
public class UserChannels {

    private Map<String, Channel> onlineUsers=new HashMap<String,Channel>();
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
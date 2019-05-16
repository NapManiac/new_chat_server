package entity;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;

/**
 *  ���ڽ������û����û����Ͷ�Ӧ��ͨ��channel��ƥ��
 * @author zhuhaipeng
 *
 */
public class UserChannels {

    private Map<String, Channel> onlineUsers=new HashMap<String,Channel>();
    /**
     *     ������ߵ��û�
     * @param username �û���
     * @param channel ���û���Ӧ��channelͨ��
     */
    public void addOnlineUser(String username,Channel channel){
        onlineUsers.put(username,channel);
    }
    /**
     * �Ƴ��û�
     * @param username Ҫ�Ƴ����û����û���
     */
    public void removeOnlineUser(String username){
        onlineUsers.remove(username);
    }
    /**
     *  �Ƴ�channelͨ��
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
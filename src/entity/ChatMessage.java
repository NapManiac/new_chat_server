package entity;
import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

import Coder.Util;
 
//消息实体，协议
@Message
public class ChatMessage {
    @Index(0)
    private String sendUser;
    @Index(1)
    private String receiveUser;
    @Index(2)
    private String message;
    @Index(3)
    private int messagetype;//1:初始化认证消息，2：聊天消息
 
    
    public byte[] encode() {
        int totalLength = sendUser.length() + receiveUser.length() + message.length() + 4 + 3;
        byte[] buffer = new byte[totalLength];
        int offset = 0;

        // copy send user to buffer
        System.arraycopy(sendUser.getBytes(), 0, buffer, offset, sendUser.length());
        offset += sendUser.length();

        // copy sign to buffer
        buffer[offset++] = ':';

        System.arraycopy(receiveUser.getBytes(), 0, buffer, offset, receiveUser.length());
        offset += receiveUser.length();

        // copy sign to buffer
        buffer[offset++] = ':';

        System.arraycopy(message.getBytes(), 0, buffer, offset, message.length());
        offset += message.length();

        // copy sign to buffer
        buffer[offset++] = ':';

        byte[] messageTypeBuffer = Util.int2bytes(messagetype);
        System.arraycopy(messageTypeBuffer, 0, buffer, offset, 4);

        return buffer;
    }

    public void decode(byte[] buffer) {
        int start_pos = 0;
        int end_pos = 0;
        // decode send user

        for (; end_pos < buffer.length; end_pos++) {
            if (buffer[end_pos] == ':') {
                break;
            }
        }
        sendUser = new String(buffer, start_pos, end_pos - start_pos);


        start_pos = end_pos + 1;
        end_pos = start_pos;
        for (; end_pos < buffer.length; end_pos++) {
            if (buffer[end_pos] == ':') {
                break;
            }
        }
        receiveUser = new String(buffer, start_pos, end_pos - start_pos);


        start_pos = end_pos + 1;
        end_pos = start_pos;
        for (; end_pos < buffer.length; end_pos++) {
            if (buffer[end_pos] == ':') {
                break;
            }
        }
        message = new String(buffer, start_pos, end_pos - start_pos);

        start_pos = end_pos + 1;
        messagetype = Util.bytes2int(buffer, start_pos);

    }
    
    public ChatMessage() {
    }
 
    public ChatMessage(String sendUser, String receiveUser, String message, int messagetype){
        this.sendUser=sendUser;
        this.receiveUser=receiveUser;
        this.message=message;
        this.messagetype=messagetype;
    }
 
    public String getSendUser() {
        return sendUser;
    }
 
    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }
 
    public String getReceiveUser() {
        return receiveUser;
    }
 
    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public int getMessagetype() {
        return messagetype;
    }
 
    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }
 
    @Override
    public String toString() {
        return "ChatMessage{" +
                "sendUser='" + sendUser + '\'' +
                ", receiveUser='" + receiveUser + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
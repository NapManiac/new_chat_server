package entity;

import java.io.UnsupportedEncodingException;

import Coder.Util;

/**
 * 消息实体类，协议
 */

public class ChatMessage {
    
    private String sendUser;
    
    private String receiveUser;
   
    private String message;
    
    private int messagetype;//1:初始化认证消息，2：聊天消息

    public ChatMessage() { // 空参构造
    }

    public ChatMessage(String sendUser, String receiveUser, String message, int messagetype){
        this.sendUser=sendUser;
        this.receiveUser=receiveUser;
        this.message=message;
        this.messagetype=messagetype;
    }

    public byte[] encode() throws UnsupportedEncodingException {
        int totalLength = sendUser.getBytes("UTF-8").length + receiveUser.getBytes("UTF-8").length + message.getBytes("UTF-8").length + 4 + 3;
        byte[] buffer = new byte[totalLength];
        int offset = 0;

        // copy send user to buffer
        System.arraycopy(sendUser.getBytes("UTF-8"), 0, buffer, offset, sendUser.getBytes("UTF-8").length);
        offset += sendUser.getBytes("UTF-8").length;

        // copy sign to buffer
        buffer[offset++] = ':';

        System.out.println(receiveUser + receiveUser.getBytes("UTF-8"));
        System.arraycopy(receiveUser.getBytes("UTF-8"), 0, buffer, offset, receiveUser.getBytes("UTF-8").length);
        offset += receiveUser.getBytes("UTF-8").length;

        // copy sign to buffer
        buffer[offset++] = ':';

        System.arraycopy(message.getBytes("UTF-8"), 0, buffer, offset, message.getBytes("UTF-8").length);
        offset += message.getBytes("UTF-8").length;

        // copy sign to buffer
        buffer[offset++] = ':';

        byte[] messageTypeBuffer = Util.int2bytes(messagetype);
        System.arraycopy(messageTypeBuffer, 0, buffer, offset, 4);
        
        System.out.println(new String(buffer, "UTF-8"));
        return buffer;
    }

    public void decode(byte[] buffer) throws UnsupportedEncodingException {
        int start_pos = 0;
        int end_pos = 0;
        // decode send user

        for (; end_pos < buffer.length; end_pos++) {
            if (buffer[end_pos] == ':') {
                break;
            }
        }
        sendUser = new String(buffer, start_pos, end_pos - start_pos, "UTF-8");


        start_pos = end_pos + 1;
        end_pos = start_pos;
        for (; end_pos < buffer.length; end_pos++) {
            if (buffer[end_pos] == ':') {
                break;
            }
        }
        receiveUser = new String(buffer, start_pos, end_pos - start_pos, "UTF-8");


        start_pos = end_pos + 1;
        end_pos = start_pos;
        for (; end_pos < buffer.length; end_pos++) {
            if (buffer[end_pos] == ':') {
                break;
            }
        }
        message = new String(buffer, start_pos, end_pos - start_pos, "UTF-8");

        start_pos = end_pos + 1;
        messagetype = Util.bytes2int(buffer, start_pos);

    }

    /**
     * 获得消息的发送者
     * @return 返回消息的发送者
     */
    public String getSendUser() {
        return sendUser;
    }

    /**
     * 设置要发送消息的对象
     * @param sendUser 发送者
     */
    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    /**
     * 获得消息的接受者
     * @return 返回消息的接受者
     */
    public String getReceiveUser() {
        return receiveUser;
    }

    /**
     * 设置消息的接受者
     * @param receiveUser 接受者
     */
    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    /**
     * 得到消息内容
     * @return 消息的内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置要发送的消息内容
     * @param message 消息的内容
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获得发送消息的种类
     * @return 返回消息的种类
     */
    public int getMessagetype() {
        return messagetype;
    }

    /**
     * 设置发送消息的类型
     * 1:初始化认证消息，2：聊天消息
     * @param messagetype 消息的类型
     */
    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }

}
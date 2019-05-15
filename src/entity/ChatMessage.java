package entity;
import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;
 
//��Ϣʵ�壬Э��
@Message
public class ChatMessage {
    @Index(0)
    private String sendUser;
    @Index(1)
    private String receiveUser;
    @Index(2)
    private String message;
    @Index(3)
    private int messagetype;//1:��ʼ����֤��Ϣ��2��������Ϣ
 
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
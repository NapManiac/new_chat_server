package entity;

import java.io.UnsupportedEncodingException;

import Coder.Util;
import io.netty.channel.Channel;
import server.ChattingServeHandler;

import static server.ChattingServeHandler.msgQueue;

/**
 * 消息实体类，协议
 */

public class ChatMessage extends Packet{

    private String message;


    public ChatMessage() { // 空参构造
        setPacketType(Util.MSG_CHAT);
    }

    public ChatMessage(String sendUser, String receiveUser, String message){
        super(Util.MSG_CHAT, sendUser, receiveUser);
        this.message = message;

    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        byte[] superEncode = super.encodeInit();
        int superMsg = superEncode.length;
        int lenMsg = message.getBytes().length;
        byte[] buffer = new byte[lenMsg + superMsg];
        System.arraycopy(superEncode, 0, buffer, 0, superEncode.length);
        System.arraycopy(message.getBytes(), 0, buffer, superMsg, message.getBytes("UTF-8").length);
        return buffer;
    }


    @Override
    public void decode(byte[] buffer) throws UnsupportedEncodingException {
        super.decodeInit(buffer);
        message = new String(buffer, getStartMsgPos(), buffer.length - getStartMsgPos(), "UTF-8");
    }

    @Override
    public void process() {
        System.out.println("进入chat process");
        UserChannels uc = ChattingServeHandler.uc;
        if(getReceiveUser().equals("")){// 接收方位空则表示是全体消息，发给所有朋友
            for(Channel ch : ChattingServeHandler.channels) {
                ch.writeAndFlush(this);
            }
        }else{//发给指定用户

            if(uc.getChannel(getReceiveUser())==null){
                msgQueue.addMsg(getReceiveUser(), this);
            } else {
                uc.getChannel(getReceiveUser()).writeAndFlush(this);
            }

        }
    }

    public String getMessage() {
        return message;
    }
}
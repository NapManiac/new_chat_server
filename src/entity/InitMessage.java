package entity;


import Coder.Util;
import server.ChattingServeHandler;

import java.io.UnsupportedEncodingException;

import io.netty.channel.Channel;

import static server.ChattingServeHandler.msgQueue;


public class InitMessage extends Packet {

    private String message;

    public String getMessage() {
        return message;
    }

    public InitMessage() { // 空参构造
        setPacketType(Util.MSG_INIT);
    }

    public InitMessage(String sendUser, String receiveUser, String message){
        super(Util.MSG_INIT, sendUser, receiveUser);
        this.message=message;

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
        System.out.println("进入init process");
        Channel newChannel =  getCtx().channel();
        if (ChattingServeHandler.searchUserExit(getSendUser())) {
            UserChannels uc = ChattingServeHandler.uc;
            uc.addOnlineUser(getSendUser(), newChannel);
            System.out.println("在线用户-->" + uc.getOnlineUsers());

            ChatMessage cmwarning = new ChatMessage(getReceiveUser(), getSendUser(),"welcome." + getSendUser());
            newChannel.writeAndFlush(cmwarning);

            if (msgQueue.getChatMessage(getSendUser()) != null) {
                newChannel.writeAndFlush(msgQueue.getChatMessage(getSendUser()));
                msgQueue.removeMsgQueue(getSendUser());

            }

        } else {
            ChatMessage cmwarning = new ChatMessage("server", getSendUser(),"user name exists：" + getSendUser());
            // 向这个加入连接失败的同名客户端发送提示信息
            newChannel.writeAndFlush(cmwarning);
        }

    }


}

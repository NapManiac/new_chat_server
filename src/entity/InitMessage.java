package entity;


import Coder.Util;
import server.ChattingServeHandler;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import io.netty.channel.Channel;

import static server.ChattingServeHandler.msgQueue;

public class InitMessage extends Packet {

    private String message;

    private String id;

    private String password;

    public String getMessage() {
        return message;
    }

    public InitMessage() { // 空参构造
        setPacketType(Util.MSG_INIT);
    }

    public InitMessage(String sendUser, String receiveUser, String message){
        super(Util.MSG_INIT, sendUser, receiveUser);
        this.message = message;

    }

    public InitMessage(String sendUser, String receiveUser, String id, String password){
        super(Util.MSG_INIT, sendUser, receiveUser);
        this.id = id;
        this.password = password;
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

        int lenId = Util.bytes2int(buffer, getStartMsgPos());//解码id长度
        int lenPassword = Util.bytes2int(buffer, getStartMsgPos() + 4);//解码password长度

        int star_index = getStartMsgPos() + 8;

        id = new String(buffer, star_index, lenId, "UTF-8");

        password = new String(buffer, star_index + lenId, lenPassword, "UTF-8");
    }
    @Override
    public void process() {
        System.out.println("进入init process");
        Channel newChannel =  getCtx().channel();

        if (UserChannels.searchId(id)) {
            if (UserChannels.searchPassword(id, password)) {
                UserChannels uc = ChattingServeHandler.uc;
                uc.addOnlineUser(getSendUser(), newChannel);
                System.out.println(getSendUser() + " " + newChannel.toString());
                InitMessage cmwarning = new InitMessage(getReceiveUser(), getSendUser(),"success");
                newChannel.writeAndFlush(cmwarning);


            } else {
                InitMessage cmwarning = new InitMessage(getReceiveUser(), getSendUser(),"error_pw");
                newChannel.writeAndFlush(cmwarning);
            }


        } else {
            InitMessage cmwarning = new InitMessage(getReceiveUser(), getSendUser(),"no_register");
            newChannel.writeAndFlush(cmwarning);
        }

    }


}

package entity;

import Coder.Util;
import io.netty.channel.Channel;
import server.ChattingServeHandler;

import java.io.UnsupportedEncodingException;

import static server.ChattingServeHandler.msgQueue;

public class AddFriendsMessage extends Packet {

    private String message;

    public String getMessage() {
        return message;
    }

    public AddFriendsMessage() { // 空参构造
        setPacketType(Util.MSG_ADDFRIENDS);
    }

    public AddFriendsMessage(String sendUser, String receiveUser, String message){
        super(Util.MSG_ADDFRIENDS, sendUser, receiveUser);
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
        super.process();
        Channel newChannel =  getCtx().channel();
        if (getReceiveUser().equals("")) { //如果没有接收者 则提示填
            ChatMessage cmwarning = new ChatMessage("server", getSendUser(),"用户名为空！" + getSendUser());

            newChannel.writeAndFlush(cmwarning);
        } else {
            UserChannels uc = ChattingServeHandler.uc;
            if (uc.getChannel(getReceiveUser())==null){ // 如果在线用户中不存在该用户，则向客户端回复不在线
                ChatMessage cmwarning = new ChatMessage("server", getSendUser(),"该用户不在线！");

                newChannel.writeAndFlush(cmwarning);
                msgQueue.addMsg(getReceiveUser(), this);
            } else if (getMessage().equals("agree")){
                uc.addUserFriend(getSendUser(), getReceiveUser());
                uc.addUserFriend(getReceiveUser(), getSendUser());
                uc.getChannel(getReceiveUser()).writeAndFlush(this);

            } else if (getMessage().equals("request")) { //如果是request请求
                //如果好友不存在
                if (!uc.searchUserFriend(getSendUser(), getReceiveUser())) {
                    uc.getChannel(getReceiveUser()).writeAndFlush(this);

                } else {
                    //如果已经有了，则反馈已存在
                    ChatMessage cmwarning = new ChatMessage("server", getSendUser(),"该好友已存在");
                    newChannel.writeAndFlush(cmwarning);
                }
            } else {
                uc.getChannel(getReceiveUser()).writeAndFlush(this);
            }

        }
    }
}

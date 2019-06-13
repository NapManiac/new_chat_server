package entity;


import Coder.Util;
import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;

import static server.ChattingServeHandler.msgQueue;

public class InitRequestMessage extends Packet {


    public InitRequestMessage() {}


    public InitRequestMessage(String send, String receive) {
        super(Util.MSG_INIT_REQUEST_INFO, send, receive);
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        return super.encodeInit();
    }

    @Override
    public void decode(byte[] buffer) throws UnsupportedEncodingException {
        super.decodeInit(buffer);
    }

    @Override
    public void process() {
        Channel newChannel =  getCtx().channel();
        if (getReceiveUser().equals("")) {
            for (String friendId : UserChannels.userFriends.get(getSendUser())) {
                Contacts contacts = UserChannels.userInfo.get(friendId);
                UserInfoMessage userInfoMessage = new UserInfoMessage("", friendId, contacts.getName(), contacts.getMotto(), Util.INFO_MAIL_INIT);
                newChannel.writeAndFlush(userInfoMessage);
            }
            for (String friendId : UserChannels.userReuest.get(getSendUser())) {
                Contacts contacts = UserChannels.userInfo.get(friendId);
                UserInfoMessage userInfoMessage = new UserInfoMessage("", friendId, contacts.getName(), contacts.getMotto(), Util.INFO_REQUESTL_INIT);
                newChannel.writeAndFlush(userInfoMessage);
            }
        }

        if (msgQueue.getChatMessage(getSendUser()) != null) {
            System.out.println("收到离线加好友消息并处理");
            for (Packet packet : msgQueue.getChatMessage(getSendUser())) {
                newChannel.writeAndFlush(packet);
            }
                msgQueue.removeMsgQueue(getSendUser());
        }

    }
}

package entity;

import Coder.Util;
import io.netty.channel.Channel;
import server.ChattingServeHandler;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class AddFriendsMessage extends Packet {

    private String message;

    public String getMessage() {
        return message;
    }

    public AddFriendsMessage() { // 空参构造
        setPacketType(Util.MSG_ADDFRIENDS);
    }

    public AddFriendsMessage(String sendUser, String receiveUser, String message) {
        super(Util.MSG_ADDFRIENDS, sendUser, receiveUser);
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
        UserChannels uc = ChattingServeHandler.uc;

         if (getMessage().equals("agree")) {

            uc.addUserFriend(getSendUser(), getReceiveUser());
            uc.addUserFriend(getReceiveUser(), getSendUser());

            System.out.println("agree " + getSendUser());
            uc.getChannel(getReceiveUser()).writeAndFlush(this);

        } else if (getMessage().equals("request")) { //如果是request请求
            List<String> list = uc.userReuest.get(getReceiveUser());
            list.add(getSendUser());

            uc.getChannel(getReceiveUser()).writeAndFlush(this);

            Contacts contacts = uc.userInfo.get(getSendUser());
            uc.getChannel(getReceiveUser()).writeAndFlush(new UserInfoMessage(getSendUser(), getReceiveUser(), contacts.getName(), contacts.getMotto(), Util.INFO_REQUESTL_ADD));

        } else {
            uc.getChannel(getReceiveUser()).writeAndFlush(this);
        }


    }
}

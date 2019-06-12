package entity;



import Coder.PacketDecodeErrorException;
import Coder.Util;
import io.netty.channel.Channel;
import server.ChattingServeHandler;

import java.io.UnsupportedEncodingException;

public class SearchMessage extends Packet {

    private int searchType;

    public SearchMessage(String sendUser, String receiveUser, int searchType){
        super(Util.MSG_SEARCH_FRIEND, sendUser, receiveUser);//接收者是搜索对象，message内容是找群还是找人
        this.searchType = searchType;
    }

    public SearchMessage() {
        setPacketType(Util.MSG_SEARCH_FRIEND);
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        byte[] superBuffer =  super.encodeInit();
        byte[] buffer = new byte[superBuffer.length + 4];
        System.arraycopy(superBuffer, 0, buffer, 0, superBuffer.length);

        System.arraycopy(Util.int2bytes(searchType), 0, buffer, superBuffer.length, 4);
        return buffer;
    }


    @Override
    public void decode(byte[] buffer) throws UnsupportedEncodingException {
        System.out.println("子类decode");
        super.decodeInit(buffer);
        searchType = Util.bytes2int(buffer, getStartMsgPos());
    }

    @Override
    public void process() {
        Channel newChannel =  getCtx().channel();
        //判断是否存在
        if (UserChannels.userInfo.containsKey(getReceiveUser())) {
            //发送他的个人信息给我
            Contacts contacts;
            contacts = UserChannels.userInfo.get(getReceiveUser());
            System.out.println(contacts.getName() + " " + contacts.getMotto());
            UserInfoMessage userInfoMessage = new UserInfoMessage("", getReceiveUser(), contacts.getName(), contacts.getMotto(), Util.INFO_SEARCH);
            newChannel.writeAndFlush(userInfoMessage);

        } else {
            SearchMessage cmwarning = new SearchMessage(getReceiveUser(), getSendUser(), Util.SEARCH_NO_FIND);
            newChannel.writeAndFlush(cmwarning);
        }
    }

    public int getSearchType() {
        return searchType;
    }
}

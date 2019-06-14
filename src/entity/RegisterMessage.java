package entity;

import Coder.Util;
import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RegisterMessage extends Packet{

    private String message;

    private String id;

    private String password;

    private String name;

    private String motto;

    public String getMessage() {
        return message;
    }

    public RegisterMessage() { // 空参构造
        setPacketType(Util.MSG_INIT);
    }

    public RegisterMessage(String sendUser, String receiveUser, String message){
        super(Util.MSG_REGISTER, sendUser, receiveUser);
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

        int lenId = Util.bytes2int(buffer, getStartMsgPos());//解码id长度
        int lenPassword = Util.bytes2int(buffer, getStartMsgPos() + 4);//解码password长度
        int lenName = Util.bytes2int(buffer, getStartMsgPos() + 8);
        int lenMotto = Util.bytes2int(buffer, getStartMsgPos() + 12);

        int star_index = getStartMsgPos() + 16;

        id = new String(buffer, star_index, lenId, "UTF-8");

        password = new String(buffer, star_index + lenId, lenPassword, "UTF-8");

        name = new String(buffer, star_index + lenId + lenPassword, lenName, "UTF-8");

        motto = new String(buffer, star_index + lenId + lenPassword + lenName, lenMotto, "UTF-8");
    }
    @Override
    public void process() {
        Channel newChannel =  getCtx().channel();

        //查找id是否被注册
        if (UserChannels.searchId(id)) {
            //已经被注册的话，提示客户端已存在
            RegisterMessage cmwarning = new RegisterMessage("server", getSendUser(),"exist");
            newChannel.writeAndFlush(cmwarning);
        } else {
            UserChannels.addAccount(id, password);
            if (name.equals("")) {
                name = "未取名";
            }

            if (motto.equals("")) {
                motto = "这个人很懒，啥也没留下~";
            }
            Contacts contacts = new Contacts(id, name, motto);
            UserChannels.userInfo.put(id, contacts);
            UserChannels.userReuest.put(id, new ArrayList<>());
            RegisterMessage cmwarning = new RegisterMessage("server", getSendUser(),"success");
            newChannel.writeAndFlush(cmwarning);
        }
    }
}

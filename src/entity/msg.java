package entity;


import Coder.Util;

import java.io.UnsupportedEncodingException;

public class msg extends Packet {

    private String message;

    private String id;

    private String password;

    public String getMessage() {
        return message;
    }

    public msg() { // 空参构造
        setPacketType(Util.MSG_INIT);
    }

    public msg(String sendUser, String receiveUser, String message){
        super(Util.MSG_REGISTER, sendUser, receiveUser);
        this.message = message;
    }

    public msg(String sendUser, String receiveUser, String id, String password) {
        super(Util.MSG_REGISTER, sendUser, receiveUser);
        this.id = id;
        this.password = password;
    }
    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        byte[] superEncode = super.encodeInit();
        int superMsg = superEncode.length;

        //消息内容为 accout 的 长度 和 内容

        int lenId = id.getBytes().length;
        int lenPassword = password.getBytes().length;

        byte[] buffer = new byte[superMsg + lenId + lenPassword + 4 * 2];
        //父类的编码
        System.arraycopy(superEncode, 0, buffer, 0, superEncode.length);

        int pos = superMsg;

        System.arraycopy(Util.int2bytes(lenId), 0, buffer, pos, 4);
        pos += 4;


        System.arraycopy(Util.int2bytes(lenPassword), 0, buffer, pos, 4);
        pos += 4;

        System.arraycopy(id.getBytes(), 0, buffer, pos, lenId);
        System.arraycopy(password.getBytes(), 0, buffer, pos + lenId, lenPassword);

        return buffer;
    }

    @Override

    public void decode(byte[] buffer) throws UnsupportedEncodingException {
//        super.decodeInit(buffer);
//
//        int lenId = Util.bytes2int(buffer, getStartMsgPos());//解码id长度
//        int lenPassword = Util.bytes2int(buffer, getStartMsgPos() + 4);//解码password长度
//
//        int star_index = getStartMsgPos() + 8;
//
//        id = new String(buffer, star_index, lenId, "UTF-8");
//
//        password = new String(buffer, star_index + lenId, lenPassword, "UTF-8");

        super.decodeInit(buffer);
        message = new String(buffer, getStartMsgPos(), buffer.length - getStartMsgPos(), "UTF-8");
    }

}

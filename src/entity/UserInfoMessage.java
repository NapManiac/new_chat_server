package entity;

import Coder.PacketDecodeErrorException;
import Coder.Util;

import java.io.UnsupportedEncodingException;

public class UserInfoMessage extends Packet {

    private String name;

    private String motto;

    private int where;

    public int getWhere() {
        return where;
    }

    public String getName() {
        return name;
    }

    public String getMotto() {
        return motto;
    }

    public UserInfoMessage() {

    }

    public UserInfoMessage(String sendUser, String recevieUser, String name, String motto, int where) {
        super(Util.MSG_USER_INFO, sendUser, recevieUser);
        this.name = name;
        this.motto = motto;
        this.where = where;
    }

    @Override
    public byte[] encode() throws UnsupportedEncodingException {
        byte[] superBuffer = super.encodeInit();
        int lenSuper = superBuffer.length;

        int lenName = name.getBytes().length;
        int lenMotto = motto.getBytes().length;

        byte[] buffer = new byte[lenMotto + lenName + lenSuper + 4 * 3];


        int pos = 0;

        System.arraycopy(superBuffer, 0, buffer, 0, lenSuper);
        pos += lenSuper;

        System.arraycopy(Util.int2bytes(lenName), 0, buffer, pos, 4);
        pos += 4;

        System.arraycopy(Util.int2bytes(lenMotto), 0, buffer, pos, 4);
        pos += 4;

        System.arraycopy(name.getBytes(), 0, buffer, pos, lenName);
        pos += lenName;

        System.arraycopy(motto.getBytes(), 0, buffer, pos, lenMotto);
        pos += lenMotto;

        System.arraycopy(Util.int2bytes(where), 0, buffer, pos, 4);

        return buffer;
    }

    @Override
    public void decode(byte[] buffer) throws UnsupportedEncodingException {
        super.decodeInit(buffer);

        int start_index = getStartMsgPos();

        int lenName = Util.bytes2int(buffer, start_index);
        int lenMotto = Util.bytes2int(buffer, start_index + 4);

        start_index += 8;

        name = new String(buffer, start_index, lenName, "UTF-8");
        motto = new String(buffer, start_index + lenName, lenMotto, "UTF-8");
        start_index += lenMotto + lenName;

        where = Util.bytes2int(buffer, start_index);
    }

    @Override
    public void process() {
        //上传信息
    }
}

package entity;

import Coder.PacketDecodeErrorException;
import Coder.Util;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

public class Packet {

    private ChannelHandlerContext ctx;

    private int packetType;

    private int startMsgPos;//真正的消息内容的位置

    private String sendUser;

    private String receiveUser;

    public Packet() {

    }

    public Packet(int packetType, String sendUser, String receiveUser) {
        this.packetType = packetType;
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public String getSendUser() {
        return sendUser;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public int getStartMsgPos() {
        return startMsgPos;
    }



    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public void decodeInit(byte[] buffer) throws UnsupportedEncodingException {
        System.out.println("decode!!!!!!!!");

        int start_index = 4;//开始解码发送者的位置
        int lenSendUser = Util.bytes2int(buffer, start_index);//解码发送者和接收者的长度
        int lenReceiveUser = Util.bytes2int(buffer, start_index + 4);
        int offeset = 4 * 3;
        sendUser = new String(buffer, offeset, lenSendUser, "UTF-8");//解码出发送者和接收者的id
        receiveUser = new String(buffer, offeset + lenSendUser, lenReceiveUser, "UTF-8");
        //方便子类解码 记录消息主体的下标
        startMsgPos = offeset + lenSendUser + lenReceiveUser;

        System.out.println("decode " + receiveUser);
    }

    public byte[] encodeInit() throws UnsupportedEncodingException {
        int lenSendUser = sendUser.getBytes().length;
        int lenReceiveUser = receiveUser.getBytes().length;
        byte[] buffer = new byte[lenReceiveUser + lenSendUser + 4 * 3];

        int pos = 0;
        System.arraycopy(Util.int2bytes(packetType), 0, buffer, pos, 4);
        pos += 4;


        System.arraycopy(Util.int2bytes(lenSendUser), 0, buffer, pos, 4);
        pos += 4;

        System.arraycopy(Util.int2bytes(lenReceiveUser), 0, buffer, pos, 4);
        pos += 4;

        System.arraycopy(sendUser.getBytes("UTF-8"), 0, buffer, pos, lenSendUser);
        pos += lenSendUser;

        System.arraycopy(receiveUser.getBytes("UTF-8"), 0, buffer, pos, lenReceiveUser);
        return buffer;
    }

    public void decode(byte[] buffer) throws UnsupportedEncodingException {
        throw new RuntimeException("没有实现decode方法");
    }
    public byte[] encode() throws UnsupportedEncodingException, PacketDecodeErrorException {
        throw new PacketDecodeErrorException("没有实现encode方法");
    }

    public void process() {
    }
}

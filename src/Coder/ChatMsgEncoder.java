package Coder;

import entity.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 消息的编码器，继承了MessageToByteEncoder，并且指定了实体类模板作为泛型
 * 该类是将ChatMessage类型的对象编码为字节流(即转换为byte类型)，在网络中传输
 */
public class ChatMsgEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        MessagePack msgpack = new MessagePack();
        // 在这里进行编码,将ChatMessage对象转换成Byte类型的数据
        byte[] msg = packet.encode();
        // 将转换成的byte类型数据写入到byteBuf缓冲区中，解码器也是从这个缓冲区中读取数据再进行解码
        //Log.i("ChatMsgEncoder", "encode length" + msg.length);
        byteBuf.writeBytes(msg);
    }

}

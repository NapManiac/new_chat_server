package Coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

import entity.ChatMessage;
 
public class ChatMsgEncoder extends MessageToByteEncoder<ChatMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, ByteBuf byteBuf) throws Exception {
        MessagePack msgpack=new MessagePack();
        byte[] msg = chatMessage.encode();
        // 将转换成的byte类型数据写入到byteBuf缓冲区中，解码器也是从这个缓冲区中读取数据再进行解码
        byteBuf.writeBytes(msg);
    }
}
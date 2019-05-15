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
        byte[] msg=msgpack.write(chatMessage);
        byteBuf.writeBytes(msg);
    }
}
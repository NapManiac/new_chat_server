package Coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import entity.ChatMessage;

import java.util.List;
 
public class ChatMsgDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final int length=byteBuf.readableBytes();
        final byte[] array=new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(),array,0,length);
        ChatMessage msg = new ChatMessage();
        System.out.println("received array size" +new String(array));
        msg.decode(array);
        list.add(msg);
    }
}
package Coder;

import entity.Packet;
import entity.PacketFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;


import java.util.List;
 
public class ChatMsgDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final int length=byteBuf.readableBytes();
        final byte[] array=new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(),array,0,length);
        Packet packet = PacketFactory.createPacketFromBuffer(array);
        packet.setCtx(channelHandlerContext);
        list.add(packet);
    }
}
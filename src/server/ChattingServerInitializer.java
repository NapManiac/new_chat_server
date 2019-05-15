package server;

import Coder.ChatMsgDecoder;
import Coder.ChatMsgEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 完成channel中pipeline的一些初始化内容，初始化完成后，将自身移除
 * @author zhuhaipeng
 *
 */
public class ChattingServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline=socketChannel.pipeline();
 
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
        // 解码器
        pipeline.addLast("msgpack decoder",new ChatMsgDecoder()); 
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
        // 编码器
        pipeline.addLast("msgpack encoder",new ChatMsgEncoder());
        	
        pipeline.addLast("handler",new ChattingServeHandler());
    }
}
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
 * 这个类相当于用来说明服务器启动过程中，需要经过哪些过程，相当于流水线上的几个部分
 * @author zhuhaipeng
 *
 */
public class ChattingServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline=socketChannel.pipeline();
        // 这个就相当于是流水线上的一个部分
        // pipeline.addLast()相当于为这个流水线添加一道工序之类的
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
        // 解码器
        pipeline.addLast("msgpack decoder",new ChatMsgDecoder()); 
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
        // 编码器
        pipeline.addLast("msgpack encoder",new ChatMsgEncoder());
        // 与channel有关的处理都是在ChattingServeHandler中实现
        pipeline.addLast("handler",new ChattingServeHandler());
    }
}
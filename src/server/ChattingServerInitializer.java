package server;

import Coder.ChatMsgDecoder;
import Coder.ChatMsgEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * ���channel��pipeline��һЩ��ʼ�����ݣ���ʼ����ɺ󣬽������Ƴ�
 * ������൱������˵�����������������У���Ҫ������Щ���̣��൱����ˮ���ϵļ�������
 * @author zhuhaipeng
 *
 */
public class ChattingServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline=socketChannel.pipeline();
        // ������൱������ˮ���ϵ�һ������
        // pipeline.addLast()�൱��Ϊ�����ˮ�����һ������֮���
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
        // ������
        pipeline.addLast("msgpack decoder",new ChatMsgDecoder()); 
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
        // ������
        pipeline.addLast("msgpack encoder",new ChatMsgEncoder());
        // ��channel�йصĴ�������ChattingServeHandler��ʵ��
        pipeline.addLast("handler",new ChattingServeHandler());
    }
}
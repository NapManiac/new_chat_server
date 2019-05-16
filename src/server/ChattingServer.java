package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChattingServer {
	// �˿ں�
    private final int port = 8888;
    
    public void startServer() {
        // ��һ���߳��飬���ڽ���Client������
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        // �ڶ����߳��飬������ʵ�ʵ�ҵ���������
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        
        try {
        	// ʹ��Netty�ṩ��ServerBootstrap���Է�������������
            ServerBootstrap bootstrap=new ServerBootstrap();
            // ���bossGroup�����������ӣ�workerGroup����������������ص��¼�
            // �൱���ϰ��Ա��֮��Ĺ�ϵ
            bootstrap.group(bossGroup, workerGroup)
            		// ��ʾ�������������NioServerSocketChannel���͵�channel???
            		// һ��channel�����Ӧ�þ���һ�����ӣ�
                    .channel(NioServerSocketChannel.class) 
                    // BACKLOG���ڹ��������׽���ServerSocket���󣬱�ʶ���������������߳�ȫ��ʱ��
                    // ������ʱ���������������ֵ�����Ķ��е���󳤶ȡ����δ���û������õ�ֵС��1 ��Ĭ��ֵ50
                    .option(ChannelOption.SO_BACKLOG, 1024) // ��tcp�жӻ�����
                    // SO_KEEPALIVE=true,������TCP��SO_KEEPALIVE����,
                	// ��SO_KEEPALIVE=true��ʱ��,����˿���̽��ͻ��˵������Ƿ񻹴����
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //���¼�������ChattingServerInitializer��ʵ���ǰ����е�ChattingServeHandler
                    .childHandler(new ChattingServerInitializer());
            // �Զ˿ڽ��а󶨲��Ҽ���
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("��������������");
            // �������ӷ�������channel״̬
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        	// �ر�EventLoopGroup�� �ͷ���Դ
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

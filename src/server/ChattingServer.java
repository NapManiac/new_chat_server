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
        // �����൱�ڿ��������߳��飬һ����ſͻ���A����һ����ſͻ���B
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
 
        try {
        	// ʹ��Netty�ṩ��ServerBootstrap��ע���������
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // BACKLOG���ڹ��������׽���ServerSocket���󣬱�ʶ���������������߳�ȫ��ʱ��
                    // ������ʱ���������������ֵ�����Ķ��е���󳤶ȡ����δ���û������õ�ֵС��1 ��Ĭ��ֵ50
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // SO_KEEPALIVE=true,������TCP��SO_KEEPALIVE����,
                	// ��SO_KEEPALIVE=true��ʱ��,����˿���̽��ͻ��˵������Ƿ񻹴����
                    .option(ChannelOption.SO_KEEPALIVE, true)
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

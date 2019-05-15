package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChattingServer {
	// 端口号
    private final int port = 8888;
    
    public void startServer() {
        // 这里相当于开了两个线程组，一个存放客户端A，另一个存放客户端B
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
 
        try {
        	// 使用Netty提供的ServerBootstrap来注册服务器端
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                    // 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1 ，默认值50
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // SO_KEEPALIVE=true,是利用TCP的SO_KEEPALIVE属性,
                	// 当SO_KEEPALIVE=true的时候,服务端可以探测客户端的连接是否还存活着
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChattingServerInitializer());
            // 对端口进行绑定并且监听
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("服务器已启动！");
            // 监听连接服务器的channel状态
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        	// 关闭EventLoopGroup， 释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

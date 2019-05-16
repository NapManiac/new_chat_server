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
        // 第一个线程组，用于接收Client端连接
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        // 第二个线程组，是用于实际的业务处理操作的
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        
        try {
        	// 使用Netty提供的ServerBootstrap来对服务器进行配置
            ServerBootstrap bootstrap=new ServerBootstrap();
            // 这个bossGroup用来接受连接，workerGroup用来处理与连接相关的事件
            // 相当于老板和员工之间的关系
            bootstrap.group(bossGroup, workerGroup)
            		// 表示服务端启动的是NioServerSocketChannel类型的channel???
            		// 一个channel代表的应该就是一个连接，
                    .channel(NioServerSocketChannel.class) 
                    // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                    // 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1 ，默认值50
                    .option(ChannelOption.SO_BACKLOG, 1024) // 即tcp列队缓冲区
                    // SO_KEEPALIVE=true,是利用TCP的SO_KEEPALIVE属性,
                	// 当SO_KEEPALIVE=true的时候,服务端可以探测客户端的连接是否还存活着
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //绑定事件处理器ChattingServerInitializer，实则是绑定其中的ChattingServeHandler
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

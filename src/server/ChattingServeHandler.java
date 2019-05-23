package server;

import java.util.Map;
import entity.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ChattingServeHandler extends ChannelInboundHandlerAdapter{
	// 定义一个用于存放已连接服务器的channel组
    public static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static UserChannels uc = new UserChannels();
    // 存放未发送的消息
    public static MsgQueue msgQueue = new MsgQueue();
    //新客户端进入时，将其加入channel队列
    @Override  // 第一步
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel newchannel=ctx.channel();
        // 遍历channels组，通知其它channel对象，有新客户端加入连接
        for(Channel ch:channels){
            if(ch!=newchannel){ 
                ch.writeAndFlush("欢迎新客户端："+newchannel.remoteAddress());
            }
        }
        channels.add(newchannel);
    }
 
    //有客户端断开连接后，将其移出队列
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
    	// 得到一个channel对象
        Channel newchannel =ctx.channel();
        // 遍历channels组，通知其它channel对象，该channel对象已经退出连接
        for(Channel ch:channels) {
            if (ch != newchannel) {
                ch.writeAndFlush(newchannel.remoteAddress() + "退出聊天室");
            }
        }
        // 将该channel对象移出组
        channels.remove(newchannel);
 
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Packet packet = (Packet) msg;
        packet.setCtx(ctx);
        System.out.println("type:" + packet.getClass());
        packet.process();

    }
    public static boolean searchUserExit(String username) {
		// 查找在线用户组中是否存在同名用户
		for (Map.Entry<String, Channel> user : uc.getOnlineUsers().entrySet()) {
			if(user.getKey().equals(username)) {
				return false;
			}
		}
		return true;
	}

	//服务器监听到客户端活动时
    @Override // 第二步
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]：在线");
    }
 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]：离线了");
        uc.removeChannel(ctx.channel());
    }
 
    @Override // 捕获通信过程中产生的异常
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]：通讯异常");
        System.out.println(cause.getMessage());
        newchannel.close();
    }
}

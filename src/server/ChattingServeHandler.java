package server;
import java.util.Map;

import entity.ChatMessage;
import entity.UserChannels;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
 
public class ChattingServeHandler extends ChannelInboundHandlerAdapter{
	// 定义一个用于存放已连接服务器的channel组
    public static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static UserChannels uc=new UserChannels();
    //新客户端进入时，将其加入channel队列
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("欢迎新客户端："+newchannel.remoteAddress());
        for(Channel ch:channels){
            if(ch!=newchannel){ // 判断这个连接服务器的客户端是否曾经连接过
                ch.writeAndFlush("欢迎新客户端："+newchannel.remoteAddress());
            }
        }
        channels.add(newchannel);
    }
 
    //有客户端断开连接后，将其移出队列
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
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
 
    //如果有客户端有写数据，则转发给其他人
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel newchannel=ctx.channel();
        ChatMessage cmsg=(ChatMessage)msg;
        for (Map.Entry<String, Channel> entry : uc.getOnlineUsers().entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        if(cmsg.getMessagetype()==1){//如果是初始化认证消息，则将该用户加入在线用户
            uc.addOnlineUser(cmsg.getSendUser(),newchannel);
            System.out.println(uc.getOnlineUsers());
            ChatMessage cmwarning=new ChatMessage("服务器", cmsg.getSendUser(),"欢迎你，"+cmsg.getSendUser() ,2);
            newchannel.writeAndFlush(cmwarning);
        }else if(cmsg.getMessagetype()==2){//如果是聊天消息，则判断发送的对象
 
            if(cmsg.getReceiveUser().equals("")){//发给所有人
                for(Channel ch:channels) {
                    ch.writeAndFlush(cmsg);
                }
            }else{//发给指定用户
                System.out.println("666"+uc.getChannel(cmsg.getReceiveUser()).remoteAddress());
                if(uc.getChannel(cmsg.getReceiveUser())==null){
                    ChatMessage cmwarning=new ChatMessage("服务器", cmsg.getSendUser(),"该用户不在线！" ,2);
                    newchannel.writeAndFlush(cmwarning);
                }else{
                    uc.getChannel(cmsg.getReceiveUser()).writeAndFlush(cmsg);
                }
 
            }
        }
    }
 
 
    //服务器监听到客户端活动时
    @Override
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
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]：通讯异常");
        System.out.println(cause.getMessage());
        newchannel.close();
    }
}

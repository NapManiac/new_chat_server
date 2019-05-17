package server;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import entity.ChatMessage;
import entity.MsgQueue;
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
    // 存放未发送的消息
    public static MsgQueue msgQueue = new MsgQueue();
    //新客户端进入时，将其加入channel队列
    @Override  // 第一步
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("欢迎新客户端："+newchannel.remoteAddress());
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
    @Override // 第三步
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel newchannel=ctx.channel();
        // 获得消息
        ChatMessage cmsg=(ChatMessage)msg;
        for (Map.Entry<String, Channel> entry : uc.getOnlineUsers().entrySet()) {
            System.out.println("用户名 = " + entry.getKey() + ", Value = " + entry.getValue());
        }
//        System.out.println("消息的类型----->" + cmsg.getMessagetype());
        if(cmsg.getMessagetype()==1){//如果是初始化认证消息，则将该用户加入在线用户
        	if(searchUserExit(cmsg.getSendUser())) {
        		uc.addOnlineUser(cmsg.getSendUser(),newchannel); // 将该用户添加进在线的用户中
                System.out.println("在线用户-->" + uc.getOnlineUsers());
                ChatMessage cmwarning=new ChatMessage("server", cmsg.getSendUser(),"welcome，"+cmsg.getSendUser() ,2);
                // 向这个新加入连接的客户端发送一条欢迎信息
                newchannel.writeAndFlush(cmwarning); 
                // 遍历消息组，查看是否存在发给该客户端(用户)的信息
                if (msgQueue.getChatMessage(cmsg.getSendUser()) != null) {
                	newchannel.writeAndFlush(msgQueue.getChatMessage(cmsg.getSendUser()));
                	msgQueue.removeMsgQueue(cmsg.getSendUser());
                	for (Map.Entry<String, ChatMessage> entry : msgQueue.getMsgQueue().entrySet()) {
    					System.out.println("消息组："+ "key="+entry.getKey()+", Value=" + entry.getValue());
    				}
                }
        	}else {
        		 ChatMessage cmwarning=new ChatMessage("server", cmsg.getSendUser(),"user name exists："+cmsg.getSendUser() ,2);
                 // 向这个加入连接失败的同名客户端发送提示信息
                 newchannel.writeAndFlush(cmwarning); 
        	}
            
        }else if(cmsg.getMessagetype()==2){//如果是聊天消息，则判断发送的对象
            if(cmsg.getReceiveUser().equals("")){// 接收方位空则表示是全体消息，发给所有人
                for(Channel ch:channels) {
                    ch.writeAndFlush(cmsg);
                }
            }else{//发给指定用户
            	try {
            		// 这条语句当你发送的用户不在线时会报错,所以在外层用try-catch捕获下异常，进行提示
            		// 在用户不在线的时候，可以将消息保存在一个消息队列中，当用户上线时在发送过去
                    System.out.println("666"+uc.getChannel(cmsg.getReceiveUser()).remoteAddress()); 
                    if(uc.getChannel(cmsg.getReceiveUser())==null){ // 如果在线用户中不存在该用户，则向客户端回复不在线
                        ChatMessage cmwarning=new ChatMessage("server", cmsg.getSendUser(),"user not online！" ,2);
                        newchannel.writeAndFlush(cmwarning);
                        msgQueue.addMsg(cmsg.getReceiveUser(), cmsg);
                    }else{
                        uc.getChannel(cmsg.getReceiveUser()).writeAndFlush(cmsg);
                    }
				} catch (Exception e) { 
					msgQueue.addMsg(cmsg.getReceiveUser(), cmsg);
					ChatMessage cmwarning=new ChatMessage("server", cmsg.getSendUser(),"user not online！" ,2);
                    newchannel.writeAndFlush(cmwarning);
				}
            }
        }
    }
 
 
    private boolean searchUserExit(String username) {
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

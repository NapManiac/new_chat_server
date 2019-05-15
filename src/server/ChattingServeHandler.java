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
	// ����һ�����ڴ�������ӷ�������channel��
    public static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static UserChannels uc=new UserChannels();
    //�¿ͻ��˽���ʱ���������channel����
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("��ӭ�¿ͻ��ˣ�"+newchannel.remoteAddress());
        for(Channel ch:channels){
            if(ch!=newchannel){ // �ж�������ӷ������Ŀͻ����Ƿ��������ӹ�
                ch.writeAndFlush("��ӭ�¿ͻ��ˣ�"+newchannel.remoteAddress());
            }
        }
        channels.add(newchannel);
    }
 
    //�пͻ��˶Ͽ����Ӻ󣬽����Ƴ�����
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    	// �õ�һ��channel����
        Channel newchannel =ctx.channel();
        // ����channels�飬֪ͨ����channel���󣬸�channel�����Ѿ��˳�����
        for(Channel ch:channels) {
            if (ch != newchannel) {
                ch.writeAndFlush(newchannel.remoteAddress() + "�˳�������");
            }
        }
        // ����channel�����Ƴ���
        channels.remove(newchannel);
 
    }
 
    //����пͻ�����д���ݣ���ת����������
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel newchannel=ctx.channel();
        ChatMessage cmsg=(ChatMessage)msg;
        for (Map.Entry<String, Channel> entry : uc.getOnlineUsers().entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        if(cmsg.getMessagetype()==1){//����ǳ�ʼ����֤��Ϣ���򽫸��û����������û�
            uc.addOnlineUser(cmsg.getSendUser(),newchannel);
            System.out.println(uc.getOnlineUsers());
            ChatMessage cmwarning=new ChatMessage("������", cmsg.getSendUser(),"��ӭ�㣬"+cmsg.getSendUser() ,2);
            newchannel.writeAndFlush(cmwarning);
        }else if(cmsg.getMessagetype()==2){//�����������Ϣ�����жϷ��͵Ķ���
 
            if(cmsg.getReceiveUser().equals("")){//����������
                for(Channel ch:channels) {
                    ch.writeAndFlush(cmsg);
                }
            }else{//����ָ���û�
                System.out.println("666"+uc.getChannel(cmsg.getReceiveUser()).remoteAddress());
                if(uc.getChannel(cmsg.getReceiveUser())==null){
                    ChatMessage cmwarning=new ChatMessage("������", cmsg.getSendUser(),"���û������ߣ�" ,2);
                    newchannel.writeAndFlush(cmwarning);
                }else{
                    uc.getChannel(cmsg.getReceiveUser()).writeAndFlush(cmsg);
                }
 
            }
        }
    }
 
 
    //�������������ͻ��˻ʱ
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]������");
    }
 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]��������");
        uc.removeChannel(ctx.channel());
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]��ͨѶ�쳣");
        System.out.println(cause.getMessage());
        newchannel.close();
    }
}

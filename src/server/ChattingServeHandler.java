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
	// ����һ�����ڴ�������ӷ�������channel��
    public static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static UserChannels uc=new UserChannels();
    // ���δ���͵���Ϣ
    public static MsgQueue msgQueue = new MsgQueue();
    //�¿ͻ��˽���ʱ���������channel����
    @Override  // ��һ��
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("��ӭ�¿ͻ��ˣ�"+newchannel.remoteAddress());
        // ����channels�飬֪ͨ����channel�������¿ͻ��˼�������
        for(Channel ch:channels){
            if(ch!=newchannel){ 
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
    @Override // ������
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel newchannel=ctx.channel();
        // �����Ϣ
        ChatMessage cmsg=(ChatMessage)msg;
        for (Map.Entry<String, Channel> entry : uc.getOnlineUsers().entrySet()) {
            System.out.println("�û��� = " + entry.getKey() + ", Value = " + entry.getValue());
        }
//        System.out.println("��Ϣ������----->" + cmsg.getMessagetype());
        if(cmsg.getMessagetype()==1){//����ǳ�ʼ����֤��Ϣ���򽫸��û����������û�
        	if(searchUserExit(cmsg.getSendUser())) {
        		uc.addOnlineUser(cmsg.getSendUser(),newchannel); // �����û���ӽ����ߵ��û���
                System.out.println("�����û�-->" + uc.getOnlineUsers());
                ChatMessage cmwarning=new ChatMessage("������", cmsg.getSendUser(),"��ӭ�㣬"+cmsg.getSendUser() ,2);
                // ������¼������ӵĿͻ��˷���һ����ӭ��Ϣ
                newchannel.writeAndFlush(cmwarning); 
                // ������Ϣ�飬�鿴�Ƿ���ڷ����ÿͻ���(�û�)����Ϣ
                if (msgQueue.getChatMessage(cmsg.getSendUser()) != null) {
                	newchannel.writeAndFlush(msgQueue.getChatMessage(cmsg.getSendUser()));
                	msgQueue.removeMsgQueue(cmsg.getSendUser());
                	for (Map.Entry<String, ChatMessage> entry : msgQueue.getMsgQueue().entrySet()) {
    					System.out.println("��Ϣ�飺"+ "key="+entry.getKey()+", Value=" + entry.getValue());
    				}
                }
        	}else {
        		 ChatMessage cmwarning=new ChatMessage("������", cmsg.getSendUser(),"�û����Ѵ��ڣ�"+cmsg.getSendUser() ,2);
                 // �������������ʧ�ܵ�ͬ���ͻ��˷�����ʾ��Ϣ
                 newchannel.writeAndFlush(cmwarning); 
        	}
            
        }else if(cmsg.getMessagetype()==2){//�����������Ϣ�����жϷ��͵Ķ���
            if(cmsg.getReceiveUser().equals("")){// ���շ�λ�����ʾ��ȫ����Ϣ������������
                for(Channel ch:channels) {
                    ch.writeAndFlush(cmsg);
                }
            }else{//����ָ���û�
            	try {
            		// ������䵱�㷢�͵��û�������ʱ�ᱨ��,�����������try-catch�������쳣��������ʾ
            		// ���û������ߵ�ʱ�򣬿��Խ���Ϣ������һ����Ϣ�����У����û�����ʱ�ڷ��͹�ȥ
                    System.out.println("666"+uc.getChannel(cmsg.getReceiveUser()).remoteAddress()); 
                    if(uc.getChannel(cmsg.getReceiveUser())==null){ // ��������û��в����ڸ��û�������ͻ��˻ظ�������
                        ChatMessage cmwarning=new ChatMessage("������", cmsg.getSendUser(),"���û������ߣ�" ,2);
                        newchannel.writeAndFlush(cmwarning);
                        msgQueue.addMsg(cmsg.getReceiveUser(), cmsg);
                    }else{
                        uc.getChannel(cmsg.getReceiveUser()).writeAndFlush(cmsg);
                    }
				} catch (Exception e) { 
					msgQueue.addMsg(cmsg.getReceiveUser(), cmsg);
					ChatMessage cmwarning=new ChatMessage("������", cmsg.getSendUser(),"���û������ߣ�" ,2);
                    newchannel.writeAndFlush(cmwarning);
				}
            }
        }
    }
 
 
    private boolean searchUserExit(String username) {
		// ���������û������Ƿ����ͬ���û�
		for (Map.Entry<String, Channel> user : uc.getOnlineUsers().entrySet()) {
			if(user.getKey().equals(username)) {
				return false;
			}
		}
		return true;
	}

	//�������������ͻ��˻ʱ
    @Override // �ڶ���
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
 
    @Override // ����ͨ�Ź����в������쳣
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel newchannel=ctx.channel();
        System.out.println("["+newchannel.remoteAddress()+"]��ͨѶ�쳣");
        System.out.println(cause.getMessage());
        newchannel.close();
    }
}

package server;



import entity.Contacts;
import entity.UserChannels;

import java.sql.SQLException;
import java.util.ArrayList;

public class StartServer {
    public static void main(String[] args) throws SQLException {


        UserChannels.addAccount("id1", "123");
        UserChannels.addAccount("id2", "123");
        UserChannels.addAccount("id3", "123");
        UserChannels.addAccount("id4", "123");
        UserChannels.addAccount("id5", "123");

        UserChannels.userInfo.put("id1", new Contacts("id1", "id1的名字", "这个人很懒，什么都没留下~"));
        UserChannels.userInfo.put("id3", new Contacts("id3", "id3的名字", "这个人很懒，什么都没留下~"));
        UserChannels.userInfo.put("id2", new Contacts("id2", "id2的名字", "这个人很懒，什么都没留下~"));
        UserChannels.userInfo.put("id4", new Contacts("id4", "id4的名字", "这个人很懒，什么都没留下~"));
        UserChannels.userInfo.put("id5", new Contacts("id5", "id5的名字", "这个人很懒，什么都没留下~"));

        UserChannels.userReuest.put("id1", new ArrayList<>());
        UserChannels.userReuest.get("id1").add("id3");

        UserChannels.addUserFriend("id1", "id2");
        UserChannels.addUserFriend("id2", "id1");



        for (int i = 0; i < 10; i++) {
            new Thread(new WorkThread()).start();
        }
        ChattingServer cs= new ChattingServer();
        cs.startServer();

    }
}
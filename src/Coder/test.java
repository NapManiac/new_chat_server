package Coder;

import entity.*;

import java.io.UnsupportedEncodingException;

public class test {


    public static void main(String[] args) throws UnsupportedEncodingException {


        SearchMessage msg = new SearchMessage("send", "rec", 1);

        byte[] a = msg.encode();


        SearchMessage msg2 = new SearchMessage();
        msg2.decode(a);


        System.out.println(msg2.getSendUser() + " " + msg2.getReceiveUser() + " " + msg2.getSearchType());
    }
}

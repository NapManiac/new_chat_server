package entity;

import Coder.Util;

import java.io.UnsupportedEncodingException;

public class PacketFactory {
    public static Packet createPacketFromBuffer(byte[] buffer) throws UnsupportedEncodingException {
        int messageType = Util.bytes2int(buffer, 0);
        Packet packet = null;
        if (messageType == Util.MSG_INIT) {
            packet = new InitMessage();
            packet.decode(buffer);
        } else if (messageType == Util.MSG_CHAT) {
            packet = new ChatMessage();
            packet.decode(buffer);
        } else if (messageType == Util.MSG_ADDFRIENDS) {
            packet = new AddFriendsMessage();
            packet.decode(buffer);
        }
        return packet;
    }
}

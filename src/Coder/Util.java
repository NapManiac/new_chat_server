package Coder;

public class Util {

	public static int MSG_INIT = 0;
	public static int MSG_CHAT = 1;
	public static int MSG_ADDFRIENDS = 2;
	public static int MSG_REGISTER = 3;
	public static int MSG_SEARCH_FRIEND = 4;
	public static int MSG_USER_INFO = 5;
	public static int MSG_INIT_REQUEST_INFO = 6;



	public static int SEARCH_FREIND = 1;
	public static int SEARCH_GROUP = 2;
	public static int SEARCH_NO_FIND = 3;

	public static int INFO_SEARCH = 21;
	public static int INFO_SEE_NEW_FRIEND = 22;
	public static int INFO_SEE_FRIEND = 23;
	public static int INFO_MAIL_INIT = 24;
	public static int INFO_REQUESTL_INIT = 25;
	public static int INFO_REQUESTL_ADD = 26;

	public static byte[] int2bytes(int num) {
		byte[] data = new byte[4];
		data[0] = (byte)(num & 0xFF);
		data[1] = (byte)((num >> 8) & 0xFF);
		data[2] = (byte)((num >> 16) & 0xFF);
		data[3] = (byte)((num >> 24) & 0xFF);
		return data;
	}
	
	public static int bytes2int(byte data[], int offset) {
		int num = 0;
		num += data[offset];
		num += ((int)(data[offset+1]) << 8);
		num += ((int)(data[offset+2]) << 16);
		num += ((int)(data[offset+3]) << 24);

		return num;
	}
}

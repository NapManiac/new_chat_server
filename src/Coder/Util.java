package Coder;

public class Util {

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

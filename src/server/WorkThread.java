package server;

import entity.Packet;

public class WorkThread extends Thread {

	public void run() {
		
		while (true) {
			Packet packet;
			try {
				packet = TaskQueue.INSTANCE.pop();
				packet.process();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

package server;

import entity.Packet;

public class WorkThread implements Runnable{

    @Override
    public void run() {
        while (true) {
            Packet packet;
            try {
                packet = TaskQueue.INSTANCE.pop();
                packet.process();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package server;

import entity.Packet;
import entity.TaskQueue;

public class WorkThread implements Runnable{

    @Override
    public void run() {
        while (true) {
            Packet packet;
            try {
                packet = TaskQueue.INSTANCE.pop();
                packet.process();
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

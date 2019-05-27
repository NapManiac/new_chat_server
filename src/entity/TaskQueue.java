package entity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {

    public Queue<Packet> taskQueue = new LinkedList<>();

    public static TaskQueue INSTANCE = new TaskQueue();
    //
    private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	public void push(Packet packet) {
	    lock.lock();
	    taskQueue.add(packet);
	    condition.signalAll();
	    lock.unlock();
    }

    public Packet pop() throws InterruptedException {
	    lock.lock();
	    while (taskQueue.size() == 0) {
	        condition.await();
        }

	    Packet packet = taskQueue.element();
	    taskQueue.remove();
	    lock.unlock();

	    return packet;
    }
}

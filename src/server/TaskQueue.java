package server;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import entity.Packet;

public class TaskQueue {

	public ArrayList<Packet> tasks = new ArrayList();
	
	public static TaskQueue INSTANCE = new TaskQueue();
	
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	
	public void push(Packet packet) {
		lock.lock();
		tasks.add(packet);
		condition.signalAll();
		lock.unlock();
	}
	
	public Packet pop() throws InterruptedException {

			lock.lock();
			while (tasks.size() == 0) {
				condition.await();
			}

			Packet packet = tasks.get(0);
			tasks.remove(0);
			lock.unlock();
			return packet;
	}
}

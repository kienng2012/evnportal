package Icom.ActiveMQ;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Icom.Utils.Utils;

public class MsgQueue {
	private BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();

	public BlockingQueue<Object> getQueue() {
		return queue;
	}

	public void add(Object value) {
		queue.add(value);
	}

	public Object remove() {
		try {
			Object data = queue.take();
			return data;
		} catch (Throwable e) {
			Utils.logger.printStackTrace(e);
		}
		return null;
	}

	public void removeAll() {
         queue.clear();
    }
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public int getSize() {
		return queue.size();
	}
}

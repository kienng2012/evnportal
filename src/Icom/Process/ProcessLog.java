package Icom.Process;

import java.util.ArrayList;
import Icom.ActiveMQ.MsgQueue;
import Icom.Dao.MTQueueDao;
import Icom.Entities.MTQueueDTO;
import Icom.Utils.Utils;

public class ProcessLog extends Thread {
	MsgQueue queue;
	public static final int ITERATION_COUNT = 100;
	String className = "ProcessLog ";
	private int processIndex;

	public ProcessLog(MsgQueue queueLog, int index) {
		this.queue = queueLog;
		this.processIndex = index;
		Utils.logger.info(className + " " + processIndex + " started");
	}

	public void run() {
		while (Main.getData) {

			try {
				if (!this.queue.isEmpty()) {

					long iteration_count;
					if (queue.getSize() > ITERATION_COUNT) {
						iteration_count = ITERATION_COUNT;

					} else {
						iteration_count = queue.getSize();
					}

					int size = 0;
					ArrayList<MTQueueDTO> messageArrayListTemp = new ArrayList<MTQueueDTO>();
					while (size < iteration_count) {

						if (queue.getSize() > 0) {
							MTQueueDTO message = (MTQueueDTO) queue.remove();
							if (message != null) {
								messageArrayListTemp.add(message);
								size++;

							} else {
								iteration_count--;
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						} else {
							try {
								sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}

					iteration_count = messageArrayListTemp.size();
					if (iteration_count > 0) {
						String query = MTQueueDao.getQueryMTLog(iteration_count);
						boolean isSuccess = MTQueueDao.writeBatchMTLog(messageArrayListTemp, query);
						if (!isSuccess) {
							Utils.logger.error(className
									+ "@bulkInsert: Insert "
									+ messageArrayListTemp.size()
									+ " records fail");
						} else {
							Utils.logger
									.info(className
											+ "@@bulkInsert insert success, queueMotLog remain: "
											+ queue.getSize() + " records");
						}

					} else {
						sleep(100);
					}
				}
			} catch (Exception e) {
				Utils.logger.error(this.className + " @error:" + e);
				Utils.logger.printStackTrace(e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
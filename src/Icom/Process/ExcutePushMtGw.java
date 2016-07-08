package Icom.Process;

import java.util.ArrayList;
import Icom.ActiveMQ.MsgQueue;
import Icom.Dao.MTQueueGWDAO;
import Icom.Entities.MTQueueDTO;
import Icom.Utils.Constants;
import Icom.Utils.Utils;

public class ExcutePushMtGw extends Thread{
	public String className = "ExcutePushMtGw";
	public MsgQueue msgQueueWaitting;
	private int processIndex;
	private int processNum;
	private String tableName = "mt_queue";
	public static final int ITERATION_COUNT = Constants.ITERATION_COUNT_TOGW;
	
	public ExcutePushMtGw(MsgQueue queue, int index, int totalThread, String tableName){
		this.msgQueueWaitting = queue;
		this.processIndex = index;
		this.processNum = totalThread;
		this.tableName = tableName;
		Utils.logger.info(className + " " + processIndex + "/"+processNum+ " started");
	}
	
	public void run(){
		while(Main.getData){
			try {
				if (!this.msgQueueWaitting.isEmpty()) {

					long iteration_count;
					if (msgQueueWaitting.getSize() > ITERATION_COUNT) {
						iteration_count = ITERATION_COUNT;

					} else {
						iteration_count = msgQueueWaitting.getSize();
					}

					int size = 0;
					ArrayList<MTQueueDTO> messageArrayListTemp = new ArrayList<MTQueueDTO>();
					while (size < iteration_count) {

						if (msgQueueWaitting.getSize() > 0) {
							MTQueueDTO message = (MTQueueDTO) msgQueueWaitting.remove();
							if (message != null) {
								Utils.logger.info(className + " push to mt_queue: @msisdn="+message.getUserId() + ", serviceID="+message.getServiceId() + ", mobileOperator="+message.getMobileOperator()+", mtContent:" +message.getInfo());
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
						String query = MTQueueGWDAO.getQueryMtQueue(iteration_count, tableName);
						boolean isSuccess = MTQueueGWDAO.writeBatchMtQueue(messageArrayListTemp, query);
						if (!isSuccess) {
							Utils.logger.error(className + "@bulkInsert mt_queue: "+ messageArrayListTemp.size()+ " records fail");
						} else {
							Utils.logger.info(className + "@@bulkInsert mt_queue: "+messageArrayListTemp.size()+" records success, msgQueueWaitting remain: " + msgQueueWaitting.getSize() + " records");
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
				Thread.sleep(Constants.TIME_SLEEP_PUSH_MTQUEUE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

package Icom.Process;

import java.util.concurrent.atomic.AtomicInteger;

import Icom.ActiveMQ.MsgQueue;
import Icom.Entities.MTQueueDTO;
import Icom.Utils.Constants;
import Icom.Utils.HttpPost;
import Icom.Utils.TextConverter;
import Icom.Utils.Utils;

public class ExcutePushMt extends Thread{
	public static String className = "ExcutePushMt";
	public MsgQueue queue;
	public MsgQueue queueLog;
	private int processIndex;
	private int processNum;
	public ExcutePushMt(MsgQueue queue, MsgQueue queueLog, int index, int totalThread){
		this.queue = queue;
		this.queueLog = queueLog;
		this.processIndex = index;
		this.processNum = totalThread;
		Utils.logger.info(className + " " + processIndex + "/"+processNum+ " started");
	}
	
	public void run(){
		while(Main.getData){
			try {
				MTQueueDTO obj = (MTQueueDTO)queue.remove();
				if(obj != null){
					
					String msisdn = obj.getUserId().trim();
					String alias = obj.getServiceId().trim();
					String message = obj.getInfo().trim();
					
					message = TextConverter.unAccent(message);
					
					int contentType = obj.getContentType();
					int countSMS = obj.getCountSms();
					
					obj.setNotes("");
					
					String sendTime = Utils.getInstance().formatDateDDMMYY(obj.getSumitDate());
					String result = "-1";
					
					Utils.logger.info(className + " get from queue @msisdn=" + msisdn + ", alias="+alias + ", message="+message+", contentType=" + contentType+ ", sendTime=" + sendTime + ", queueMt size=" + queue.getSize());
					
					/*if(obj.getMobileOperator().equalsIgnoreCase("VIETTEL") || obj.getMobileOperator().equalsIgnoreCase("VIETEL")){
						alias = BrandNameDAO.getInstance().getBrandName(alias, Main.listBrandName);						
					}*/
					if(obj.getMobileOperator().equalsIgnoreCase("VMS") && Main.testlist.get(msisdn) != null && Icom.Utils.Config.getInstance().getIntProperty("CHECK_TESTLIST") == 1){
						Utils.logger.info(className + " @sendRequestVMSNew: msisdn= " + msisdn + ", mobileOperator=" + obj.getMobileOperator() + ", info=" + obj.getInfo());
						obj.setServiceId(alias);	
						
						if(Main.testlist.get(msisdn) != null){
							String[] response = HttpPost.sendRequestVMSNew(alias, msisdn, message);
							if(response != null){
								result = "200".equalsIgnoreCase(response[0]) ? "1" : response[0];
								obj.setNotes(response[1]);
							}
							
							/*result = HttpPost.sendVmgRequest(Constants.VMG_URL_BRANDNAME+"?wsdl", obj, Constants.ACCOUNT, Constants.PASSWORD);
							result = result.trim();*/
						}
						
					}else{
						String userName = Constants.ACCOUNT;
						String password = Constants.PASSWORD;
						/*if(obj.getMobileOperator().equalsIgnoreCase("VIETTEL") || obj.getMobileOperator().equalsIgnoreCase("VIETEL")){
							userName = Constants.VMG_ACCOUNT_VIETTEL;
							password = Constants.VMG_PASSWORD_VIETTEL; 
						}*/
						try {
							/*String url= Constants.VMG_URL_BRANDNAME+"/SendBulkSms?msisdn="+msisdn+"&alias="+alias+"&message="+message+"&contentType="+contentType+"&sendTime="+sendTime+"&authenticateUser="+userName+"&authenticatePass="+password;
							url = url.replaceAll(" ", "%20");
							Utils.logger.info(className + " @send request: " + url);
					    	result = HttpPost.makeRequest(url);*/
							
							
							//TODO : gui MT qua API cua VMG
//							//OLD API
//							result = HttpPost.sendVmgRequest(Constants.VMG_URL_BRANDNAME+"?wsdl", obj, userName, password);
							//NEW API
							result = HttpPost.sendVmgRequestNew(Constants.VMG_URL_BRANDNAME+"?wsdl", obj, userName, password);
							
							result = result.trim();
							
						} catch (Exception e) {
							// TODO: handle exception
							Utils.logger.error("Send VMG_URL_BRANDNAME to msisdn="+obj.getUserId()+" @error: " + e.getMessage());
							Utils.logger.printStackTrace(e);
							result = "-2";							
						}
						
						if(result.equalsIgnoreCase("-2")){
							
							/*int countRetry=0;
							while(result.equalsIgnoreCase("-2") && countRetry < 3){
								try {
									
									result = HttpPost.sendVmgRequest(Constants.VMG_URL_BRANDNAME+"?wsdl", obj, userName, password);
									result = result.trim();
									
								} catch (Exception e) {
									// TODO: handle exception
									Utils.logger.error("Send VMG_URL_BRANDNAME to msisdn="+obj.getUserId()+" @error: " + e.getMessage());
									Utils.logger.printStackTrace(e);
									result = "-2";				
								}
								
								countRetry++;
								
								Utils.logger.info("retry send request to msisdn=" + obj.getUserId() + ", result= " + result +", count= " + countRetry + " times");
							}*/
							
							
							
							//TODO : Loc cac ban tin exception de xu ly nhung tin loi (unicode) sau do ms add vao queue Kienng
							Thread.sleep(500);
							
							queue.add(obj);
							
							Utils.logger.info("timeout send request to msisdn=" + obj.getUserId() + ", service_id="+obj.getServiceId()+", mobile_operator="+obj.getMobileOperator()+", result= " + result +", add to queue size= " + queue.getSize());
							continue;
						}
					}
					
					obj.setNotes(result);
					Utils.logger.info(className + " @response send mt from VMG  to msisdn="+obj.getUserId()+" @result=" + result);
					
			    	int iResult = 1;
			    	try {
						iResult = Integer.parseInt(result);
						if(iResult==0) iResult = 1;
					} catch (Exception e) {
						// TODO: handle exception
						Utils.logger.printStackTrace(e);
					}
			    	try {
			    		
			    		if(iResult > 1000){
				    		iResult = 1;
				    		AtomicInteger remain = Main.remainQuota.get(";" + obj.getMobileOperator() + ";" + alias);
				    		Utils.logger.info("remain @obj.getMobileOperator(): " + obj.getMobileOperator() + ", alias: "+ alias);
				    		if(remain !=  null){
					    		if(remain.get() - countSMS > 0){
					    			remain.getAndAdd(countSMS * (-1)); 
						    		Main.remainQuota.put(";" + obj.getMobileOperator() + ";" + alias, remain);
					    		}
				    		}
				    	}
					} catch (Exception e) {
						Utils.logger.printStackTrace(e);
					}
			    	
			    	obj.setProcessResult(iResult);
			    	
					queueLog.add(new MTQueueDTO(obj));
					
					Utils.logger.info(className + " add to queueLog @msisdn=" + obj.getUserId() + ", service_id=" + obj.getServiceId() + ", operator="+obj.getMobileOperator() + ", process_result=" + obj.getProcessResult() + ", queueLog.size=" + queueLog.getSize());
				}
				
			} catch (Exception e) {
				Utils.logger.error(className + " @error: " + e.getMessage()); 
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Utils.logger.printStackTrace(e);
			}
		}
	}
}

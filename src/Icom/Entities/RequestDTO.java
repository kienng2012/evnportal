package Icom.Entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Icom.Process.Main;
import Icom.Utils.Utils;

public class RequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5497579850427932730L;
	private String msgID;
	private String md5;
	private Timestamp timestamp;
	private String msisdn;
	
	public RequestDTO() {
		
	}

	public RequestDTO(String msgID, String md5, Timestamp timestamp,
			String msisdn) {
		super();
		this.msgID = msgID;
		this.md5 = md5;
		this.timestamp = timestamp;
		this.msisdn = msisdn;
	}


	public String getMsisdn() {
		return msisdn;
	}
	
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public boolean check24Hour(){
		
		ArrayList<RequestDTO> arrRequest = Main.listMsisdn.get(msisdn);
		
		if(arrRequest != null){
			for(RequestDTO request : arrRequest){
				if(request != null){
					long currentTime = System.currentTimeMillis();
					long lTimestamp = request.getTimestamp().getTime();
					
					if(request.getMd5().equals(md5) && (currentTime - lTimestamp) <= 5 * 60 * 60 * 1000){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static void initResource(){		
		Utils.logger.info("initResource from listMsisdn.dat");
		try{
			Main.listMsisdn = (ConcurrentHashMap<String, ArrayList<RequestDTO>>) Utils.getInstance().loadData("listMsisdn.dat");
			if(Main.listMsisdn == null){
				Main.listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
			}
		}catch(Exception e){
			if(Main.listMsisdn == null){
				Main.listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
			}
			
			Utils.logger.printStackTrace(e);
		}
	}
	
	public static void saveResource(){
		Utils.logger.info("saveResource to listMsisdn.dat");
		Utils.getInstance().saveData(Main.listMsisdn, "listMsisdn.dat");		
	}
	
	public static void updateListMsisdn(RequestDTO request, MTQueueDTO mtObject){
		ArrayList<RequestDTO> arrResquest = Main.listMsisdn.get(mtObject.getUserId());
		  
		if(arrResquest == null){
			arrResquest = new ArrayList<RequestDTO>();
		}
		  
		arrResquest.add(request);
		Main.listMsisdn.put(mtObject.getUserId(), arrResquest);
	}
}

package Icom.DTO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

import Icom.API.ResourceEntity;
import Icom.DAO.ResponseUtils;

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
		
		ArrayList<RequestDTO> arrRequest = ResourceEntity.listMsisdn.get(msisdn);
		
		if(arrRequest != null){
			for(RequestDTO request : arrRequest){
				if(request != null){
					long currentTime = System.currentTimeMillis();
					long lTimestamp = request.getTimestamp().getTime();
					//24 * 60 * 60 * 1000
					if(request.getMsgID().equals(msgID) && request.getMd5().equals(md5) && (currentTime - lTimestamp) <= 12 * 60 * 60 * 1000){
						return true;
					}
					
					
					/*
					if(request.getMd5().equals(md5) && (currentTime - lTimestamp) <= 24 * 60 * 60 * 1000){
						return true;
					}
					*/
				}
			}
		}
		
		return false;
	}
	
	public boolean check15Minute(){
		ArrayList<RequestDTO> arrRequest = ResourceEntity.listMsisdn.get(msisdn);
		
		if(arrRequest != null){
			for(RequestDTO request : arrRequest){
				if(request != null){
					long currentTime = System.currentTimeMillis();
					long lTimestamp = request.getTimestamp().getTime();
					
					if(!request.getMsgID().equals(msgID) && request.getMd5().equals(md5) && (currentTime - lTimestamp) <= 15 * 60 * 1000){
						return true;
					}
				}
			}
		}
		
		return false;
	}
}

package Icom.DTO;

import java.sql.Timestamp;
import java.util.ArrayList;

import Icom.API.ResourceEntity;
import Icom.DAO.ResponseUtils;
import Icom.DAO.TelcoUtils;
import Icom.DTO.PushSMSDTO.MessageDTO;
import Icom.Utils.Utils;

public class BulkSMSDTO {
	private String username;
	private String password;
	private String loaiSP;
	private String content;
	private String msgID;
	private String brandName;
	private ArrayList<MessageDTO> listMsisdn;
	private int status;
	private String statusInfo;
	private int priority;
	private String serviceID;
	private int countSMS;
	private String partner;
	
	public BulkSMSDTO() {
	
	}

	public BulkSMSDTO(String username, String password, String loaiSP, String serviceID,
			String content, String msgID, ArrayList<MessageDTO> listMsisdn, String brandName, int status, String statusInfo, int priority) {
		super();
		this.username = username;
		this.password = password;
		this.loaiSP = loaiSP;
		this.content = content;
		this.msgID = msgID;
		this.listMsisdn = listMsisdn;
		this.brandName = brandName;
		this.status = status;
		this.statusInfo = statusInfo;
		this.priority = priority;
		this.serviceID = serviceID;
	}
	
	public String getPartner() {
		return partner;
	}
	
	public void setPartner(String partner) {
		this.partner = partner;
	}
	
	public int getCountSMS() {
		return countSMS;
	}
	
	public void setCountSMS(int countSMS) {
		this.countSMS = countSMS;
	}
	
	public String getServiceID() {
		return serviceID;
	}
	
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}
	
	public String getStatusInfo() {
		return statusInfo;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoaiSP() {
		return loaiSP;
	}

	public void setLoaiSP(String loaiSP) {
		this.loaiSP = loaiSP;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public ArrayList<MessageDTO> getListMsisdn() {
		return listMsisdn;
	}

	public void setListMsisdn(ArrayList<MessageDTO> listMsisdn) {
		this.listMsisdn = listMsisdn;
	}
	
	public int getSumSMS() {
		int count = 0;
		
		for(MessageDTO message : listMsisdn){
			if("0".equals(message.getResInfo())){
				String telco = TelcoUtils.getTelco(message.getMsisdn());
				count = count + TelcoUtils.countSMS(content, telco);
			}
		}
		
		return count;
	}
	
	public class MessageDTO{
		private String msisdn;
		private String resInfo;
		private String operator;
		private String error_desc;
		
		public MessageDTO() {
			resInfo = "0";
		}

		public String getErrorDesc() {
			return error_desc;
		}
		
		public void setErrorDesc(String error_desc) {
			this.error_desc = error_desc;
		}
		
		public String getOperator() {
			return operator;
		}
		
		public void setOperator(String operator) {
			this.operator = operator;
		}
		
		public MessageDTO(String msisdn, String resInfo) {
			super();
			this.msisdn = msisdn;
			this.resInfo = resInfo;
		}

		public String getResInfo() {
			return resInfo;
		}
		
		public void setResInfo(String resInfo) {
			this.resInfo = resInfo;
		}

		public String getMsisdn() {
			return msisdn;
		}

		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}


	}
}

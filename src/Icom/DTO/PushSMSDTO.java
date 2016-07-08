package Icom.DTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;

import Icom.API.ResourceEntity;
import Icom.DAO.ResponseUtils;
import Icom.Utils.Utils;

public class PushSMSDTO {
	private String username;
	private String password;
	private String loaiSP;
	private String brandName;
	private int status;
	private String statusInfo;
	private ArrayList<MessageDTO> listMessage;
	private String serviceID;
	private String partner;
	
	public PushSMSDTO() {
	
	}
	
	public PushSMSDTO(String username, String password, String loaiSP,
			String brandName, ArrayList<MessageDTO> listMessage, int status, String statusInfo, String serviceID) {
		super();
		this.username = username;
		this.password = password;
		this.loaiSP = loaiSP;
		this.brandName = brandName;
		this.listMessage = listMessage;
		this.status = status;
		this.statusInfo = statusInfo;
		this.serviceID = serviceID;
	}
	
	public String getPartner() {
		return partner;
	}
	
	public void setPartner(String partner) {
		this.partner = partner;
	}
	
	public int getSumSMS() {
		int count = 0;
		for(MessageDTO message : listMessage){
			if("0".equals(message.getResInfo())){
				count = count + message.getCountSMS();
			}
		}
		return count;
	}
	
	public String getServiceID() {
		return serviceID;
	}
	
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public ArrayList<MessageDTO> getListMessage() {
		return listMessage;
	}

	public void setListMessage(ArrayList<MessageDTO> listMessage) {
		this.listMessage = listMessage;
	}

	public class MessageDTO implements Comparable<MessageDTO>{
		private int priority;
		private String msgID;
		private String msisdn;
		private String content;
		private String resInfo;
		private String operator;
		private int countSMS;
		private String error_desc;
		
		public MessageDTO() {
			resInfo = "0";
		}

		public MessageDTO(int priority, String msgID, String msisdn, String content, String resInfo) {
			super();
			this.priority = priority;
			this.msgID = msgID;
			this.msisdn = msisdn;
			this.content = content;
			this.resInfo = resInfo;
		}

		public String getErrorDesc() {
			return error_desc;
		}
		
		public void setErrorDesc(String error_desc) {
			this.error_desc = error_desc;
		}
		
		public int getCountSMS() {
			return countSMS;
		}
		
		public void setCountSMS(int countSMS) {
			this.countSMS = countSMS;
		}
		
		public String getOperator() {
			return operator;
		}
		
		public void setOperator(String operator) {
			this.operator = operator;
		}
		
		public int getPriority() {
			return priority;
		}
		
		public void setPriority(int priority) {
			this.priority = priority;
		}
		
		public String getResInfo() {
			return resInfo;
		}
		
		public void setResInfo(String resInfo) {
			this.resInfo = resInfo;
		}
		
		public String getMsgID() {
			return msgID;
		}

		public void setMsgID(String msgID) {
			this.msgID = msgID;
		}

		public String getMsisdn() {
			return msisdn;
		}

		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@Override
		public int compareTo(MessageDTO o) {
			
			return -(o.getPriority() - this.priority);
		}
	}
	
}


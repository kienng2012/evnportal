package Icom.DTO;

import java.util.ArrayList;

public class CheckStatusDTO {
	private String username;
	private ArrayList<String> listMessageID;
	private ArrayList<MessageDTO> listResponse;
	
	public CheckStatusDTO() {
	
	}

	public CheckStatusDTO(String username, ArrayList<String> listMessageID) {
		super();
		this.username = username;
		this.listMessageID = listMessageID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<String> getListMessageID() {
		return listMessageID;
	}

	public void setListMessageID(ArrayList<String> listMessageID) {
		this.listMessageID = listMessageID;
	}
	
	public ArrayList<MessageDTO> getListResponse() {
		return listResponse;
	}
	
	public void setListResponse(ArrayList<MessageDTO> listResponse) {
		this.listResponse = listResponse;
	}
	
	public class MessageDTO{
		private String messageID;
		private String msisdn;
		private int status;
		
		public MessageDTO() {
		
		}

		public MessageDTO(String messageID, String msisdn, int status) {
			super();
			this.messageID = messageID;
			this.msisdn = msisdn;
			this.status = status;
		}

		public String getMessageID() {
			return messageID;
		}

		public void setMessageID(String messageID) {
			this.messageID = messageID;
		}

		public String getMsisdn() {
			return msisdn;
		}

		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
		
		
	}
	
}

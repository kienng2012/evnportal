package Icom.Entities;

import java.io.Serializable;
import java.sql.Timestamp;


public class MTQueueDTO implements Serializable {	
	
	public MTQueueDTO() {
	
	}

	private int id;
	private String userId;
	private String serviceId;
	private String mobileOperator;
	private String commandCode;
	private int contentType;
	private String info;
	private Timestamp sumitDate;
	private Timestamp doneDate;
	private int processResult;
	private int messageType;
	private String requestId;
	private String messageId;
	private int totalSegment;
	private int retriesNum;	
	private Timestamp insertDate;
	private String notes;
	private int amount;
	private int channelType;
	private int countSms;
	private String msgId;
	private String md5;
	private String cpId;
	private int amountSale;
	private int amountPurchase;
	
	public MTQueueDTO(MTQueueDTO message) {
		this.id = message.getId();
		this.userId = message.getUserId();
		this.serviceId = message.getServiceId();
		this.mobileOperator = message.getMobileOperator();
		this.commandCode = message.getCommandCode();
		this.contentType = message.getContentType();
		this.info = message.getInfo();
		this.messageType = message.getMessageType();
		this.requestId = message.getRequestId();
		this.sumitDate = message.getSumitDate();
		this.doneDate = message.getDoneDate();
		this.notes = message.getNotes();
		this.amount = message.getAmount();
		this.processResult = message.getProcessResult();
		this.channelType = message.getChannelType();
		this.insertDate = message.getInsertDate();
		this.amountSale = message.getAmountSale();
		this.amountPurchase = message.getAmountPurchase();
		this.cpId = message.getCpId();
		this.md5 = message.getMd5();
		this.countSms = message.getCountSms();
		this.msgId = message.getMsgId();
		this.messageId = message.getMessageId();
	}
	
	public int getAmountSale() {
		return amountSale;
	}
	public void setAmountSale(int amountSale) {
		this.amountSale = amountSale;
	}
	public int getAmountPurchase() {
		return amountPurchase;
	}
	public void setAmountPurchase(int amountPurchase) {
		this.amountPurchase = amountPurchase;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getMobileOperator() {
		return mobileOperator;
	}
	public void setMobileOperator(String mobileOperator) {
		this.mobileOperator = mobileOperator;
	}
	public String getCommandCode() {
		return commandCode;
	}
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Timestamp getSumitDate() {
		return sumitDate;
	}
	public void setSumitDate(Timestamp sumitDate) {
		this.sumitDate = sumitDate;
	}
	public Timestamp getDoneDate() {
		return doneDate;
	}
	public void setDoneDate(Timestamp doneDate) {
		this.doneDate = doneDate;
	}
	public int getProcessResult() {
		return processResult;
	}
	public void setProcessResult(int processResult) {
		this.processResult = processResult;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public int getTotalSegment() {
		return totalSegment;
	}
	public void setTotalSegment(int totalSegment) {
		this.totalSegment = totalSegment;
	}
	public int getRetriesNum() {
		return retriesNum;
	}
	public void setRetriesNum(int retriesNum) {
		this.retriesNum = retriesNum;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getChannelType() {
		return channelType;
	}
	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}
	public int getCountSms() {
		return countSms;
	}
	public void setCountSms(int countSms) {
		this.countSms = countSms;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
}

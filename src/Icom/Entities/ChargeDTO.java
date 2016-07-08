package Icom.Entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChargeDTO implements Serializable
{
  public String userId;
  public String serviceId;
  public String mobileOperator;
  public String commandCode;
  public String option;
  public String messageType;
  public String requestId;
  public String messageId;
  public Timestamp insertDate;
  public String serviceName;
  private String channelType;
  private String reason;
  public int amount;
  private int Promotion;
  private String Note;
  private int isPromotion;
  private int chargeResult;
  private String ErrorCode;
  private String TypeCharge;
  private int ORIGINALPRICE;
  private int IsFree;
  private int cpid;
  private Timestamp requestTimeCharge;
  public Timestamp getRequestTimeCharge() {
	return requestTimeCharge;
}

public void setRequestTimeCharge(Timestamp requestTimeCharge) {
	this.requestTimeCharge = requestTimeCharge;
}

public int getCpId()
  {
    return this.cpid;
  }
  
  public void setCpId(int cpid)
  {
    this.cpid = cpid;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public String getServiceId()
  {
    return this.serviceId;
  }
  
  public void setServiceId(String serviceId)
  {
    this.serviceId = serviceId;
  }
  
  public String getMobileOperator()
  {
    return this.mobileOperator;
  }
  
  public void setMobileOperator(String mobileOperator)
  {
    this.mobileOperator = mobileOperator;
  }
  
  public String getCommandCode()
  {
    return this.commandCode;
  }
  
  public void setCommandCode(String commandCode)
  {
    this.commandCode = commandCode;
  }
  
  public String getOption()
  {
    return this.option;
  }
  
  public void setOption(String option)
  {
    this.option = option;
  }
  
  public String getMessageType()
  {
    return this.messageType;
  }
  
  public void setMessageType(String messageType)
  {
    this.messageType = messageType;
  }
  
  public String getRequestId()
  {
    return this.requestId;
  }
  
  public void setRequestId(String requestId)
  {
    this.requestId = requestId;
  }
  
  public String getMessageId()
  {
    return this.messageId;
  }
  
  public void setMessageId(String messageId)
  {
    this.messageId = messageId;
  }
  
  public Timestamp getInsertDate()
  {
    return this.insertDate;
  }
  
  public void setInsertDate(Timestamp insertDate)
  {
    this.insertDate = insertDate;
  }
  
  public String getServiceName()
  {
    return this.serviceName;
  }
  
  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }
  
  public int getAmount()
  {
    return this.amount;
  }
  
  public void setAmount(int amount)
  {
    this.amount = amount;
  }
  
  public String getReason()
  {
    return this.reason;
  }
  
  public void setReason(String reason)
  {
    this.reason = reason;
  }
  
  public int getPromotion()
  {
    return this.Promotion;
  }
  
  public void setPromotion(int promotion)
  {
    this.Promotion = promotion;
  }
  
  public String getNote()
  {
    return this.Note;
  }
  
  public void setNote(String note)
  {
    this.Note = note;
  }
  
  public int getIsPromotion()
  {
    return this.isPromotion;
  }
  
  public void setIsPromotion(int isPromotion)
  {
    this.isPromotion = isPromotion;
  }
  
  public int getChargeResult()
  {
    return this.chargeResult;
  }
  
  public void setChargeResult(int chargeResult)
  {
    this.chargeResult = chargeResult;
  }
  
  public String getErrorCode()
  {
    return this.ErrorCode;
  }
  
  public void setErrorCode(String errorCode)
  {
    this.ErrorCode = errorCode;
  }
  
  public String getTypeCharge()
  {
    return this.TypeCharge;
  }
  
  public void setTypeCharge(String typeCharge)
  {
    this.TypeCharge = typeCharge;
  }
  
  public int getORIGINALPRICE()
  {
    return this.ORIGINALPRICE;
  }
  
  public void setORIGINALPRICE(int oRIGINALPRICE)
  {
    this.ORIGINALPRICE = oRIGINALPRICE;
  }
  
  public int getIsFree()
  {
    return this.IsFree;
  }
  
  public void setIsFree(int isFree)
  {
    this.IsFree = isFree;
  }
  
  public String getChannelType()
  {
    return this.channelType;
  }
  
  public void setChannelType(String channelType)
  {
    this.channelType = channelType;
  }
  
  public ChargeDTO(ChargeDTO chargeInfo)
  {
    this.userId = chargeInfo.getUserId();
    this.mobileOperator = chargeInfo.getMobileOperator();
    this.serviceId = chargeInfo.getServiceId();
    this.amount = chargeInfo.getAmount();
    this.isPromotion = chargeInfo.getIsPromotion();
    this.reason = chargeInfo.getReason();
    this.Note = chargeInfo.getNote();
    this.channelType = chargeInfo.getChannelType();
    this.cpid = chargeInfo.getCpId();
  }
  
  public ChargeDTO() {}
}

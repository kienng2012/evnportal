package Icom.DAO;

import java.util.Hashtable;

import Icom.Constant.StatusConstant;
import Icom.DTO.BulkSMSDTO;
import Icom.DTO.CheckStatusDTO;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.PushSMSDTO.MessageDTO;


public class ResponseUtils {
	
	public static final int SUCCESS = 1;
	public static final int SENDING_ERROR = -1;
	public static final int AUTHENTICATION_FAILURE = 100;
	public static final int AUTHENTICATION_USER_DEACTIVED = 101;
	public static final int AUTHENTICATION_USER_EXPIRED = 102;
	public static final int AUTHENTICATION_USER_LOCKED = 103;
	public static final int TEMPLATE_NOT_ACTIVED = 104;
	public static final int TEMPLATE_NOT_EXISTED = 105;
	public static final int SEND_SAME_CONTENT_IN_SHORT_TIME = 304;
	public static final int NOT_ENOUGH_MONEY = 400;
	public static final int SYSTEM_IS_ERROR = 900;
	public static final int CONTENT_TOO_LONG = 901;
	public static final int NUMBER_MSISDN_FAIL = 902;
	public static final int BRANDNAME_INACTIVE = 904;
	
	private ResponseUtils(){
		
	}
	
	
	private static ResponseUtils mInstance;
	private static Object mLock = new Object();
	
	public static ResponseUtils getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new ResponseUtils();
			}
			return mInstance;
		}
	}
	
	public String responseDefault(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<RESP>");
		sb.append("<STATUS>1</STATUS>");
		sb.append("<STATUSINFO>Sai format</STATUSINFO>");
		sb.append("</RESP>");

		return sb.toString();
	}
	
	public String responseFail(int status, String info){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<RESP>");
		sb.append("<STATUS>" + status + "</STATUS>");
		sb.append("<STATUSINFO>" + info + "</STATUSINFO>");
		sb.append("</RESP>");

		return sb.toString();
	}
	
	public String responsePushSMS(PushSMSDTO pushSMS){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<RESP>");
		sb.append("<STATUS>0</STATUS>");
		
		for(MessageDTO message : pushSMS.getListMessage()){
			sb.append("<MSG>");
			sb.append("<MSGID>" + message.getMsgID() + "</MSGID>");
			sb.append("<MSISDN>" + message.getMsisdn() + "</MSISDN>");
			String resInfo = message.getResInfo();
			if("0".equals(resInfo)){
				sb.append("<RSINFO>OK</RSINFO>");
			}else{
				sb.append("<RSINFO>" + message.getErrorDesc() + "</RSINFO>");
			}
			sb.append("</MSG>");
		}
		
		sb.append("</RESP>");

		return sb.toString();
	}
	
	public String responseBulkSMS(BulkSMSDTO bulkSMS){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<RESP>");
		sb.append("<STATUS>0</STATUS>");
		sb.append("<MSGID>" + bulkSMS.getMsgID() + "</MSGID>");
		
		for(Icom.DTO.BulkSMSDTO.MessageDTO message : bulkSMS.getListMsisdn()){
			sb.append("<MSG>");
			sb.append("<MSISDN>" + message.getMsisdn() + "</MSISDN>");
//			sb.append("<RSINFO>OK</RSINFO>");
			String resInfo = message.getResInfo();
			if("0".equals(resInfo)){
				sb.append("<RSINFO>OK</RSINFO>");
			}else{
				sb.append("<RSINFO>" + message.getErrorDesc() + "</RSINFO>");
			}
			sb.append("</MSG>");
		}
		
		sb.append("</RESP>");

		return sb.toString();

	}
	
	public String responseCheckStatus(Hashtable<String, CheckStatusDTO.MessageDTO> listResponse){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<RQST>");
		
		for(String key : listResponse.keySet()){
			CheckStatusDTO.MessageDTO message = listResponse.get(key);
			String msgID = key;
			sb.append("<MSG>");
			
			if(message == null){
				sb.append("<MSGID>" + msgID + "</MSGID>");
				sb.append("<MSISDN></MSISDN>");
				sb.append("<STATUS>" + StatusConstant.RES_STATUS_NOT_AVAILIBLE_MESSAGE_ID + "</STATUS>");
			}else{
				sb.append("<MSGID>" + message.getMessageID() + "</MSGID>");
				sb.append("<MSISDN>" + message.getMsisdn() + "</MSISDN>");
				sb.append("<STATUS>" + message.getStatus() + "</STATUS>");
			}
			
			sb.append("</MSG>");
		}
		
		sb.append("</RQST>");
		
		return sb.toString();
	}
}

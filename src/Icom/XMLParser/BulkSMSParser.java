package Icom.XMLParser;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import Icom.API.ResourceEntity;
import Icom.Constant.PushConstant;
import Icom.Constant.StatusConstant;
import Icom.DAO.RequestDAO;
import Icom.DAO.ResponseUtils;
import Icom.DAO.TelcoUtils;
import Icom.DTO.BulkSMSDTO;
import Icom.DTO.RequestDTO;
import Icom.DTO.BulkSMSDTO.MessageDTO;
import Icom.Utils.Utils;

public class BulkSMSParser extends DefaultHandler{

	   boolean isRQST = false;
	   boolean isUsername = false;
	   boolean isPassword = false;
	   boolean isLoaiSP = false;
	   boolean isBrandName = false;
	   boolean isMSG = false;
	   boolean isMSGID = false;
	   boolean isMSISDN = false;
	   boolean isContent = false;
	   
	   BulkSMSDTO result;
	   ArrayList<MessageDTO> listMsisdn;
	   MessageDTO message;
	   
	   @Override
	   public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException{
		  if (tagName.equalsIgnoreCase("RQST")) {
			   isRQST = true;
			   if(result == null){
				   result = new BulkSMSDTO();
			   }
		  }else if (tagName.equalsIgnoreCase("USERNAME")) {
		     isUsername = true;
		  } else if (tagName.equalsIgnoreCase("PASSWORD")) {
		     isPassword = true;
		  } else if (tagName.equalsIgnoreCase("LOAISP")) {
		     isLoaiSP = true;
		  } else if (tagName.equalsIgnoreCase("BRANDNAME")) {
		     isBrandName = true;
		  } else if (tagName.equalsIgnoreCase("MSG")) {
		     isMSG = true;
		     if(listMsisdn == null){
		    	 listMsisdn = new ArrayList<MessageDTO>();
		     }
		     if(result != null){
		    	 message = result.new MessageDTO();
		     }
		  } else if(tagName.equalsIgnoreCase("MSGID")){
			  isMSGID = true;
		  } else if(tagName.equalsIgnoreCase("MSISDN")){
			  isMSISDN = true;
		  } else if(tagName.equalsIgnoreCase("CONTENT")){
			  isContent = true;
		  }
		  
	   }

	   @Override
	   public void endElement(String uri, String localName, String tagName) throws SAXException {
	      if (tagName.equalsIgnoreCase("MSG")) {
	    	  listMsisdn.add(message);
	    	  isMSG = false;
	      } else if (tagName.equalsIgnoreCase("RQST")) {
	    	  if(result != null){
	    		  result.setListMsisdn(listMsisdn);
	    	  }
	    	  isRQST = false;
	      }
	   }

	   @Override
	   public void characters(char ch[], int start, int length) throws SAXException {
	      if (isUsername) {
	    	  if(result != null){
	    		  String username = new String(ch, start, length);
	    		  result.setUsername(username);
	    	  }
	    	  isUsername = false;
	      } else if (isPassword) {
	    	  if(result != null){
	    		  String password = new String(ch, start, length);
	    		  result.setPassword(password);
	    	  }
	    	  isPassword = false;
	      } else if (isLoaiSP) {
	    	  if(result != null){
	    		  String loaiSP = new String(ch, start, length);
	    		  result.setLoaiSP(loaiSP);
	    	  }
	    	  isLoaiSP = false;
	      } else if (isBrandName) {
	    	  if(result != null){
	    		  String brandName = new String(ch, start, length);
	    		  result.setBrandName(brandName);
	    	  }
	    	  isBrandName = false;
	      } else if(isMSGID){
	    	  if(result != null){
	    		  String msgID = new String(ch, start, length);
	    		  result.setMsgID(msgID);
	    	  }
	    	  isMSGID = false;
	      } else if(isMSISDN){
	    	  if(message != null){
	    		  String msisdn = new String(ch, start, length);
	    		  if(msisdn.startsWith("0")){
	    			  msisdn = "84" + msisdn.substring(1);
	    		  }
		    	  message.setMsisdn(msisdn);  
		    	  
		    	  String telco = TelcoUtils.getTelco(msisdn);
		    	  
		    	  if(telco != null){
		    		  message.setOperator(telco);
		    		  
		    		  if(ResourceEntity.blacklist.get(msisdn) != null){
		    			  message.setResInfo(StatusConstant.BLACKLIST + "");
		    			  message.setErrorDesc(StatusConstant.BLACKLIST + "|" + "Blacklist");
	    			  }else{
	    				  if(result != null){
	    					  int count = TelcoUtils.countSMS(result.getContent(), message.getOperator());
			    			  if(count != -1){
			    				  result.setCountSMS(TelcoUtils.countSMS(result.getContent(), message.getOperator()));
			    				  
			    				  RequestDTO request = new RequestDTO(result.getMsgID(), Utils.getInstance().getMD5(result.getContent()), new Timestamp(System.currentTimeMillis()), message.getMsisdn());
			    				  message.setResInfo("0");
			    				  
			    				  if(request.check15Minute()){
			    	    			  message.setResInfo(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "");
			    	    			  message.setErrorDesc(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "|" + "15 Minute");
			    	    		  }
			    	    		  
			    				  if(request.check24Hour()){
			    	    			  message.setResInfo(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "");
			    	    			  message.setErrorDesc(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "|" + "24 Hour");
			    	    		  }
			    	    		  
			    				  if("0".equals(message.getResInfo())){
			    					  ArrayList<RequestDTO> arrResquest = ResourceEntity.listMsisdn.get(message.getMsisdn());
			    					  if(arrResquest == null){
			    						  arrResquest = new ArrayList<RequestDTO>();
			    					  }
			    					  arrResquest.add(request);    					  
			    					  
//			    					  Utils.logger.info("BulkSMSParser add to ResourceEntity.listMsisdn @msisdn=" + message.getMsisdn() + ", msgid=" + request.getMsgID() + ", resinfo="+message.getResInfo());
				    				  ResourceEntity.listMsisdn.put(message.getMsisdn(), arrResquest);
				    			  }
			    				  
			    			  }else{
			    				  message.setResInfo(StatusConstant.RES_STATUS_FAILED_FORMAT + "");
				    			  message.setErrorDesc(StatusConstant.RES_STATUS_FAILED_FORMAT + "|" + "Content Too Long");
			    			  }
	    				  }
	    			  }
		    		  
		    		  
		    	  }else{
		    		  message.setResInfo(StatusConstant.RES_STATUS_FAILED_FORMAT + "");
	    			  message.setErrorDesc(StatusConstant.RES_STATUS_FAILED_FORMAT + "|" + "Invalid PhoneNumber");
		    	  }
		    	  
	    	  }
	    	  isMSISDN = false;
	      } else if(isContent){
	    	  if(result != null){
	    		  String content = new String(ch, start, length);
//	    		  content = content.replace("&amp;", "&");
	    		  
	    		  content = content.replace("[!@#$%]", "&");
	    		  
	    		  result.setContent(content);
	    		  
//	    		  String telco = message.getOperator();
//	    		  if(telco != null){
//	    			  int count = TelcoUtils.countSMS(content, message.getOperator());
//	    			  if(count != -1){
//	    				  result.setCountSMS(TelcoUtils.countSMS(content, message.getOperator()));
//	    				  
//	    				  RequestDTO request = new RequestDTO(result.getMsgID(), Utils.getInstance().getMD5(content), new Timestamp(System.currentTimeMillis()), message.getMsisdn());
//	    				  message.setResInfo("0");
//	    				  
//	    				  if(request.check15Minute()){
//	    	    			  message.setResInfo(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "");
//	    	    			  message.setErrorDesc(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "|" + "15 Minute");
//	    	    		  }
//	    	    		  
//	    				  if(request.check24Hour()){
//	    	    			  message.setResInfo(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "");
//	    	    			  message.setErrorDesc(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "|" + "24 Hour");
//	    	    		  }
//	    	    		  
//	    				  if("0".equals(message.getResInfo())){
//	    					  ArrayList<RequestDTO> arrResquest = ResourceEntity.listMsisdn.get(message.getMsisdn());
//	    					  if(arrResquest == null){
//	    						  arrResquest = new ArrayList<RequestDTO>();
//	    					  }
//	    					  arrResquest.add(request);    					  
//	    					  
////	    					  Utils.logger.info("BulkSMSParser add to ResourceEntity.listMsisdn @msisdn=" + message.getMsisdn() + ", msgid=" + request.getMsgID() + ", resinfo="+message.getResInfo());
//		    				  ResourceEntity.listMsisdn.put(message.getMsisdn(), arrResquest);
//		    			  }
//	    				  
//	    			  }else{
//	    				  message.setResInfo(StatusConstant.RES_STATUS_FAILED_FORMAT + "");
//		    			  message.setErrorDesc(StatusConstant.RES_STATUS_FAILED_FORMAT + "|" + "Content Too Long");
//	    			  }
//		    		  
//	    		  }else{
//	    			  message.setResInfo(StatusConstant.RES_STATUS_FAILED_FORMAT + "");
//	    			  message.setErrorDesc(StatusConstant.RES_STATUS_FAILED_FORMAT + "|" + "Invalid PhoneNumber");
//	    		  }
	    		  
	    	  }
	    	  isContent = false;
	      }
	      
	   }
	
	   public BulkSMSDTO getBulkSMS() {
		   return result;
	}	

}

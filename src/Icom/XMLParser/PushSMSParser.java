package Icom.XMLParser;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import Icom.API.ResourceEntity;
import Icom.Constant.StatusConstant;
import Icom.DAO.ResponseUtils;
import Icom.DAO.TelcoUtils;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.PushSMSDTO.MessageDTO;
import Icom.DTO.RequestDTO;
import Icom.Utils.Utils;

public class PushSMSParser extends DefaultHandler{
	   boolean isRQST = false;
	   boolean isUsername = false;
	   boolean isPassword = false;
	   boolean isLoaiSP = false;
	   boolean isBrandName = false;
	   boolean isMSG = false;
	   boolean isMSGID = false;
	   boolean isMSISDN = false;
	   boolean isContent = false;
	   
	   PushSMSDTO result;
	   ArrayList<MessageDTO> listMessage;
	   MessageDTO messageDTO = null;
	   
	   @Override
	   public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException {
		  if (tagName.equalsIgnoreCase("RQST")) {
			   isRQST = true;
			   if(result == null){
				   result = new PushSMSDTO();
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
	         if(listMessage == null){
	        	 listMessage = new ArrayList<MessageDTO>();
	         }
	         
	         if(result != null){
	        	 messageDTO = result.new MessageDTO();
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
	   public void endElement(String uri, String localName, String qName) throws SAXException {
	      if (qName.equalsIgnoreCase("MSG")) {
	    	  listMessage.add(messageDTO);
	    	  isMSG = false;
	      } else if (qName.equalsIgnoreCase("RQST")) {
	    	  if(result != null){
	    		  result.setListMessage(listMessage);
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
	    	  if(messageDTO != null){
	    		  String msgID = new String(ch, start, length);
	    		  int priority = 0;
	    		  String[] arrID = msgID.split("#");
	    		  if(arrID.length == 2){
	    			  priority = Integer.parseInt(arrID[0].replace("P", ""));
	    		  }
	    		  messageDTO.setPriority(priority);
	    		  messageDTO.setMsgID(msgID);
	    	  }
	    	  isMSGID = false;
	      } else if(isMSISDN){
	    	  if(messageDTO != null){
	    		  String msisdn = new String(ch, start, length);
	    		  if(msisdn.startsWith("0")){
	    			  msisdn = "84" + msisdn.substring(1);
	    		  }
	    		  messageDTO.setMsisdn(msisdn);
	    		  String telco = TelcoUtils.getTelco(msisdn);
	    		  if(telco != null){
	    			  messageDTO.setOperator(telco);
	    			  if(ResourceEntity.blacklist.get(msisdn) != null){
	    				  messageDTO.setResInfo(StatusConstant.BLACKLIST + "");
		    			  messageDTO.setErrorDesc(StatusConstant.BLACKLIST + "|" + "Blacklist");
	    			  }
	    		  }else{
	    			  messageDTO.setResInfo(StatusConstant.RES_STATUS_FAILED_FORMAT + "");
	    			  messageDTO.setErrorDesc(StatusConstant.RES_STATUS_FAILED_FORMAT + "|" + "Invalid PhoneNumber");
	    		  }
	    		  
	    		  
	    	  }
	    	  isMSISDN = false;
	      } else if(isContent){
	    	  if(messageDTO != null){
	    		  String content = new String(ch, start, length);
	    		  
//	    		  content = content.replace("&amp;", "&");
	    		  
	    		  content = content.replace("[!@#$%]", "&");
	    		  
	    		  
	    		  messageDTO.setContent(content);
	    		  String telco = messageDTO.getOperator();
	    		  if(telco != null){
	    			  int count = TelcoUtils.countSMS(content, messageDTO.getOperator());
		    		  if(count == -1){
		    			  messageDTO.setResInfo(StatusConstant.RES_STATUS_FAILED_FORMAT + "");
		    			  messageDTO.setErrorDesc(StatusConstant.RES_STATUS_FAILED_FORMAT + "|" + "Content Too Long");
//		    			  messageDTO.setResInfo(ResponseUtils.CONTENT_TOO_LONG + "");
//		    			  messageDTO.setErrorDesc(ResponseUtils.CONTENT_TOO_LONG + "|" + "Content Too Long");
		    		  }else{
		    			  messageDTO.setCountSMS(count);
		    			  
		    			  RequestDTO request = new RequestDTO(messageDTO.getMsgID(), Utils.getInstance().getMD5(content), new Timestamp(System.currentTimeMillis()), messageDTO.getMsisdn());
		    			  messageDTO.setResInfo("0");
		    			  
		    			  if(request.check15Minute()){
			    			  messageDTO.setResInfo(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "");
			    			  messageDTO.setErrorDesc(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "|" + "15 Minute");
			    		  }
			    		  
		    			  if(request.check24Hour()){
			    			  messageDTO.setResInfo(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "");
			    			  messageDTO.setErrorDesc(ResponseUtils.SEND_SAME_CONTENT_IN_SHORT_TIME + "|" + "24 Hour");
			    		  }
			    		  
		    			  if("0".equals(messageDTO.getResInfo())){
		    				  
	    					  ArrayList<RequestDTO> arrResquest = ResourceEntity.listMsisdn.get(messageDTO.getMsisdn());
	    					  
	    					  if(arrResquest == null){
	    						  arrResquest = new ArrayList<RequestDTO>();
	    					  }
	    					  
	    					  arrResquest.add(request);
	    					  
//	    					  Utils.logger.info("PushSMSParser add to ResourceEntity.listMsisdn @msisdn=" + messageDTO.getMsisdn() + ", msgid=" + request.getMsgID() + ", resinfo="+messageDTO.getResInfo());
		    				  ResourceEntity.listMsisdn.put(messageDTO.getMsisdn(), arrResquest);
		    			  }
		    			  
		    		  }
	    		  }else{
	    			  messageDTO.setResInfo(StatusConstant.RES_STATUS_FAILED_FORMAT + "");
	    			  messageDTO.setErrorDesc(StatusConstant.RES_STATUS_FAILED_FORMAT + "|" + "Invalid PhoneNumber");
	    		  }
	    		  
	    	  }
	    	  isContent = false;
	      }
	      
	      
	   }
	
	   public PushSMSDTO getPushSMS() {
		   return result;
	}	
}

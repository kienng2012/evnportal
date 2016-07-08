package Icom.XMLParser;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import Icom.DTO.CheckStatusDTO;

public class CheckStatusParser extends DefaultHandler{

	   boolean isRQST = false;
	   boolean isUsername = false;
	   boolean isMSG = false;
	   boolean isMSGID = false;
	   
	   CheckStatusDTO result;
	   ArrayList<String> listMessage;
	   String messageID;
	   
	   @Override
	   public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException {
		  if (tagName.equalsIgnoreCase("RQST")) {
			   isRQST = true;
			   if(result == null){
				   result = new CheckStatusDTO();
			   }
		  } else if (tagName.equalsIgnoreCase("USERNAME")) {
	         isUsername = true;
	      } else if (tagName.equalsIgnoreCase("MSG")) {
	         isMSG = true;
	         if(listMessage == null){
	        	 listMessage = new ArrayList<String>();
	         }
	         
	      } else if(tagName.equalsIgnoreCase("MSGID")){
	    	  isMSGID = true;
	      } 
	   }

	   @Override
	   public void endElement(String uri, String localName, String qName) throws SAXException {
	      if (qName.equalsIgnoreCase("MSG")) {
	    	  listMessage.add(messageID);
	    	  isMSG = false;
	      } else if (qName.equalsIgnoreCase("RQST")) {
	    	  if(result != null){
	    		  result.setListMessageID(listMessage);
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
	      }  else if(isMSGID){
    		  messageID = new String(ch, start, length);
    		  isMSGID = false;
	      } 
	      
	      
	   }
	
	   public CheckStatusDTO getCheckStatus() {
		   return result;
	}	

}

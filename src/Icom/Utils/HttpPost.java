package Icom.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Icom.Entities.MTQueueDTO;
import Icom.Process.Main;
import Icom.Utils.Utils;

public class HttpPost {
	private static final String USER_AGENT = "Mozilla/5.0";
	public static final int TIMEOUT = 5000;
	
	public static String getDataFromAPI(String url){
		long requestStart = System.currentTimeMillis();
		try{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		con.setRequestMethod("GET");
		con.setReadTimeout(TIMEOUT);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		return response.toString();
		
		}catch(SocketTimeoutException e){
			Utils.logger.error("Error @APIGetter: getDataFromAPI : " + e.getMessage() + ", url=" + url);
		}
		catch(Exception e){
			Utils.logger.error("Error @APIGetter: getDataFromAPI : " + e.getMessage() + ", url=" + url);
		}
		
		long endResponse = System.currentTimeMillis();
		long time = endResponse - requestStart;
		
		Utils.logger.info("Thoi gian nhan response: " + time);
		return null;
	}
	
	public static void loginVMS(){
//		VMS_URL_LOGIN=http://113.187.15.83/smsg/login.jsp?userName=[USERNAME]&password=[PASSWORD]&bindMode=[BIND_MODE]
//		VMS_USERNAME=ICOM
//		VMS_PASSWORD=12345
		try{
			
			String url = Config.getInstance().getProperty("VMS_URL_LOGIN");
			String username = Config.getInstance().getProperty("VMS_USERNAME");
			String password = Config.getInstance().getProperty("VMS_PASSWORD");
			url = url.replace("[USERNAME]", username);
			url = url.replace("[PASSWORD]", password);
			
			Utils.logger.info("Login VMS: " + url);
			
			String result = getDataFromAPI(url);
			
			Utils.logger.info("Login VMS: " + result);
			
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(result);
			String status = (String) jsonObj.get("status");
			String error_desc = null;
			if("200".equalsIgnoreCase(status)){
				String sid = (String) jsonObj.get("sid");
				Main.VMS_SESSION_ID = sid;
				error_desc = "Login success";
			}else{
				error_desc = (String) jsonObj.get("message");
				Utils.logger.info("Login Fail: " + error_desc);
			}
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
	}
	
	public static String[] sendRequestVMSNew(String fromNumber, String toNumber, String content){
		//VMS_URL_SEND_MT=http://113.187.15.83/smsg/send.jsp?sid=[SID]&sender=[SENDER]&recipient=[RECIPIENT]&content=[CONTENT]
		
		try{
			if(Main.VMS_SESSION_ID != null){
				
				String[] result = new String[2];
				
				content = content.replace(" ", "+");
				
				String url = Config.getInstance().getProperty("VMS_URL_SEND_MT");
				
				url = url.replace("[SID]", Main.VMS_SESSION_ID);
				url = url.replace("[SENDER]", fromNumber);
				url = url.replace("[RECIPIENT]", toNumber);
				url = url.replace("[CONTENT]", content);
				
				Utils.logger.info("Send VMS: SendMT: " + url);
				
				String response = getDataFromAPI(url);
				
				Utils.logger.info("Send VMS: Result: " + result);
				
				if(response.contains("NotLoggedIn") || response.contains("SessionExpired")){
					Utils.logger.info("ReLogin VMS");
					Main.VMS_SESSION_ID = null;
					loginVMS();
					sendRequestVMSNew(fromNumber, toNumber, content);
				}else{
					try{
						JSONObject jsonObj = (JSONObject) new JSONParser().parse(response);
						
						String status = (String) jsonObj.get("status");
						
						String error_desc = (String) jsonObj.get("message");
						
						result[0] = status;
						result[1] = error_desc;
						
					}catch(Exception e){
						Utils.logger.printStackTrace(e);
					}
				}
				
				return result;
				
			}else{
				Utils.logger.info("Login VMS");
				loginVMS();
				sendRequestVMSNew(fromNumber, toNumber, content);
			}
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return null;
	}
	
	public static String sendVmsRequest(String url, MTQueueDTO obj) throws Exception
	{
		    String template = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" + 
		    
			"<soapenv:Header/>" +
		    "<soapenv:Body>" +
		       "<tem:BulkSendSms>" +
		          
		          "<tem:msisdn>"+obj.getUserId()+"</tem:msisdn>" +
		         
		          "<tem:alias>"+obj.getServiceId()+"</tem:alias><tem:message>"+obj.getInfo()+"</tem:message>" +
		         
		          "<tem:sendTime>"+Utils.getInstance().formatDateDDMMYY(obj.getSumitDate())+"</tem:sendTime>" +
		          
		          "<tem:authenticateUser>ICOM</tem:authenticateUser><tem:authenticatePass>123456</tem:authenticatePass>" +
		       "</tem:BulkSendSms>" +
		    "</soapenv:Body>" +
		 "</soapenv:Envelope>";
		    
		    Utils.logger.info("sendVmsRequest:" + template);
		   
		    String xmlReturn = httpPostData(url, template);
		    Utils.logger.info("xmlReturn:" + xmlReturn);
		    String sReturn = getValueTagStart(xmlReturn, "BulkSendSmsResult");
		    Utils.logger.info("sendVmsRequest @sReturn: " + sReturn);
		    return sReturn;
	}	
	
	public static String sendVmgRequest(String url, MTQueueDTO obj, String userName, String passWord) throws Exception
	{
		    String template = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" + 
		    
			"<soapenv:Header/>" +
		    "<soapenv:Body>" +
		       "<tem:SendBulkSms>" +
		          
		          "<tem:msisdn>"+obj.getUserId()+"</tem:msisdn>" +
		         
		          "<tem:alias>"+obj.getServiceId()+"</tem:alias>" +
		          
		          "<tem:message>"+obj.getInfo()+"</tem:message>" +
		          
				  "<tem:contentType>"+obj.getContentType()+"</tem:contentType>" +
		         
		          "<tem:sendTime>"+Utils.getInstance().formatDateDDMMYY(obj.getSumitDate())+"</tem:sendTime>" +
		          
		          "<tem:authenticateUser>"+userName+"</tem:authenticateUser><tem:authenticatePass>"+passWord+"</tem:authenticatePass>" +
		       "</tem:SendBulkSms>" +
		    "</soapenv:Body>" +
		 "</soapenv:Envelope>";
		    
		    Utils.logger.info("sendVmgRequest @xmlRequest:" + template);
		   
		    String xmlReturn = httpPostData(url, template);
		    Utils.logger.info("sendVmgRequest msisdn="+obj.getUserId()+" @xmlResponse:" + xmlReturn);
		    String sReturn = getValueTagStart(xmlReturn, "SendBulkSmsResult");
		    Utils.logger.info("sendVmgRequest @sReturn: " + sReturn);
		    return sReturn;
	}
	
	/**
	 * api SEND VIA BRANDNAME VMG NEW 20160706
	 * @param url
	 * @param obj
	 * @param userName
	 * @param passWord
	 * @return
	 * @throws Exception
	 */
	public static String sendVmgRequestNew(String url, MTQueueDTO obj, String userName, String passWord) throws Exception
	{
		//TRUE XML : URL : http://brandsms.vn:8018/VMGAPI.asmx
		/*
		<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/">
	    <Body>
	        <BulkSendSms xmlns="http://tempuri.org/">
	            <msisdn>0967891610</msisdn>
	            <alias>CSKH.EVNNPC</alias>
	            <message>Test sms</message>
	            <sendTime>07/07/2016 10:20</sendTime>
	            <authenticateUser>icom</authenticateUser>
	            <authenticatePass>vmg123456</authenticatePass>
	        </BulkSendSms>
	    </Body>
	</Envelope>
	*/

		
		
		/*
		<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/">
	    <Body>
	        <BulkSendSms xmlns="http://tempuri.org/">
	            <msisdn>[string?]</msisdn>
	            <alias>[string?]</alias>
	            <message>[string?]</message>
	            <sendTime>[string?]</sendTime>
	            <authenticateUser>[string?]</authenticateUser>
	            <authenticatePass>[string?]</authenticatePass>
	        </BulkSendSms>
	    </Body>
	</Envelope>
	        */
		    String template = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" + 
			"<soapenv:Header/>" +
			    "<soapenv:Body>" +
			       "<tem:BulkSendSms>" +   
			          "<tem:msisdn>"+obj.getUserId()+"</tem:msisdn>" +	         
			          "<tem:alias>"+obj.getServiceId()+"</tem:alias>" +	          
			          "<tem:message>"+obj.getInfo()+"</tem:message>" +		         
			          "<tem:sendTime>"+Utils.getInstance().formatDateDDMMYY(obj.getSumitDate())+"</tem:sendTime>" +     
			          "<tem:authenticateUser>"+userName+"</tem:authenticateUser>"+
			          "<tem:authenticatePass>"+passWord+"</tem:authenticatePass>" +   
			          "</tem:BulkSendSms>" +
			    "</soapenv:Body>" +
		    "</soapenv:Envelope>";
		    
		    Utils.logger.info("sendVmgRequest @xmlRequest:" + template);
		   
		    String xmlReturn = httpPostData(url, template);
		    Utils.logger.info("sendVmgRequest msisdn="+obj.getUserId()+" @xmlResponse:" + xmlReturn);
		    String sReturn = getValueTagStart(xmlReturn, "error_code");
		    Utils.logger.info("sendVmgRequest @sReturn: " + sReturn);
		    
		    return sReturn;
	}
	
	public static String sendEVNResponse(String url, String response) throws Exception
	{
		    String template = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">"
		    + "<soapenv:Header/>"
		    + "<soapenv:Body>"
		       + "<tem:Push_res>"
		       	  + "<tem:user>" + Constants.EVN_PASSWORD_WS + "</tem:user>"
		          + "<tem:pass>" + Constants.EVN_ACCOUNT_WS + "</tem:pass>"
		          + "<tem:cb>" + response + "</tem:cb>"
		          
		       + "</tem:Push_res>"
		    + "</soapenv:Body>"
		 + "</soapenv:Envelope>";
		    
		    Utils.logger.info("sendEVNRequest:" + template);
		   
		    String xmlReturn = httpPostData(url, template);
		    Utils.logger.info("xmlReturn:" + xmlReturn);
		    String sReturn = getValueTagStart(xmlReturn, "Push_resResult");
		    Utils.logger.info("sendEVNRequest @sReturn: " + sReturn);
		    return sReturn;
	}
	
	public static String httpPostData(String url, String xml) throws Exception {

		HttpClient client = new HttpClient();
		HttpState state = client.getState();
		client.setState(state);
		client.setTimeout(5 * 60 * 1000);
		PostMethod method = new PostMethod(url);
		method.setDoAuthentication(true);
		method.getHostAuthState().setAuthAttempted(true);
		method.getHostAuthState().setAuthRequested(true);
		method.getHostAuthState().setPreemptive();
		method.addRequestHeader("Content-Type", "text/xml");
		// method.addRequestHeader("SOAPAction", "UserLoginRequest");
		try {
			method.setRequestBody(xml);

		} catch (Exception e) {
			try {
				ByteArrayRequestEntity entity = new ByteArrayRequestEntity(
						xml.getBytes("UTF-8"));
				method.setRequestEntity(entity);

			} catch (Exception e1) {
				throw new Exception("Impossible to set the xml in the post");
			}
		}
		long timeRequest = System.currentTimeMillis();
		int iRes = 0;
		try {
			iRes = client.executeMethod(method);
			long timeResponse = System.currentTimeMillis();
			Utils.logger.info("Thoi gian xu ly lenh: "
					+ (timeResponse - timeRequest) / 1000);
		} catch (RemoteException ex) {
			long timeResponse = System.currentTimeMillis();
			Utils.logger.info("Thoi gian xu ly lenh: "
					+ (timeResponse - timeRequest) / 1000);
			Utils.logger.printStackTrace(ex);
		}

		InputStream inputStream = method.getResponseBodyAsStream();
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
		} finally {
		}
		String textResponse = sb.toString();

		//String[] toReturn = { "" + iRes, textResponse };
		Utils.logger.info("ket qua:" + textResponse+"");
		//Utils.logger.info(textResponse);

		return textResponse;
	}
	
	public static String makeRequest(String url) throws Exception{
    	URL obj = new URL(url);
    	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    	// optional default is GET
    	con.setRequestMethod("GET");
    	//add request header
    	con.setRequestProperty("User-Agent", USER_AGENT);    	 
    	int responseCode = con.getResponseCode();
    	//System.out.println("\nSending 'GET' request to URL : " + url);
    	Utils.logger.info("\nSending 'GET' request to URL : " + url);
    	//System.out.println("Response Code : " + responseCode);
    	Utils.logger.info("Response Code : " + responseCode);
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	String inputLine;
    	StringBuffer response = new StringBuffer();
    	 
    	while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    	}
    	in.close();
    	//print result
    	System.out.println(response.toString());
    	Utils.logger.info(response.toString());
    	return getValueTagStart(response.toString(),"int");
    }
	
	public static String sendRequest(String url) throws Exception{
    	URL obj = new URL(url);
    	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    	// optional default is GET
    	con.setRequestMethod("GET");
    	//add request header
    	con.setRequestProperty("User-Agent", USER_AGENT);    	 
    	int responseCode = con.getResponseCode();
    	//System.out.println("\nSending 'GET' request to URL : " + url);
    	Utils.logger.info("\nSending 'GET' request to URL : " + url);
    	//System.out.println("Response Code : " + responseCode);
    	Utils.logger.info("Response Code : " + responseCode);
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	String inputLine;
    	StringBuffer response = new StringBuffer();
    	 
    	while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    	}
    	in.close();
    	//print result
    	System.out.println(response.toString());
    	Utils.logger.info(response.toString());
    	
    	return response.toString();
    }
    
	public static String getValueTagStart(String xml, String tagName) {
		  String openTag = "<" + tagName + "";
		  String closeTag = "</" + tagName + ">";
		  
		  int f = xml.indexOf(openTag) + xml.substring(xml.indexOf("<"+tagName)).indexOf(">")+1;
		  int l = xml.indexOf(closeTag);

		  return (f > l) ? "" : xml.substring(f, l);
	}
	
/////////////////////////////////////////////////////////////
	public static String getResult(String str, String tag_Start, String tag_end) {
		String result = str.substring(
				str.indexOf(tag_Start) + tag_Start.length(),
				str.indexOf(tag_end));
		return result;
	}
	/////////////////////////////////////////////////////////////	
	public static void getResult(String result, StringBuffer erro, StringBuffer msg, String tag_eror1,String tag_message2) {
		 
	
		String index_error1 ="<" +tag_eror1.trim() +">";
		String index_erorr2 ="</" +tag_eror1.trim() +">";
		String index_message1 = "<"+tag_message2.trim()+">";
		String index_message2 ="</" +tag_message2.trim() +">";
		Utils.logger.info("::::"+result+":::::");
		Utils.logger.info(index_error1+":"+index_erorr2+":"+index_message1+":"+index_message2);
		if(result.length()>0){
		erro.append(result.substring(
				result.indexOf(index_error1) + index_error1.length(),
				result.indexOf(index_erorr2)));
		if("0".equals(erro.toString())) {
			msg.append(result.substring(
					result.indexOf(index_message1) + index_message1.length(),
					result.indexOf(index_message2)));
		}
		}
	}
	
	public static String getXMLfile(String path) {
		Utils.logger.info(path);
		File file = new File(path);
		StringBuilder contents = new StringBuilder();
		BufferedReader reader = null;
	
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
	
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				contents.append(text);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Utils.logger.error(e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				Utils.logger.error(e.getMessage());
			}
		}
		// System.out.print(contents.toString());
		return contents.toString();
	}
	///////////////////////////////////////////////////////////////////
	}

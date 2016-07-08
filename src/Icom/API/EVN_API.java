package Icom.API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import org.apache.axis2.context.MessageContext;

import Icom.Constant.BulkConstant;
import Icom.Constant.PushConstant;
import Icom.Constant.RequestType;
import Icom.Constant.StatusConstant;
import Icom.DAO.BlacklistDAO;
import Icom.DAO.MTQueueDAO;
import Icom.DAO.PartnerDAO;
import Icom.DAO.RequestDAO;
import Icom.DAO.ResponseUtils;
import Icom.DTO.BulkSMSDTO;
import Icom.DTO.CheckStatusDTO;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.PushSMSDTO.MessageDTO;
import Icom.DTO.RequestFailDTO;
import Icom.Utils.Constants;
import Icom.Utils.Utils;
import Icom.XMLParser.EVNParser;

public class EVN_API {

	public String pushSms(String xml){
		
		String response = null;
		
		try{
			Utils.logger.info("Request: " + xml);
			
			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			
				MessageContext curContext = MessageContext.getCurrentMessageContext();
				String remoteIp = "Unknown";

				if (curContext != null) {
					Object ipProperty = curContext.getProperty(MessageContext.REMOTE_ADDR);
					remoteIp = ipProperty.toString();
				}
				
				Utils.logger.info("Remote IP: " + remoteIp);
				
//				boolean checkIPForward = Constants.IP.indexOf(remoteIp) >= 0 ? true : false;
				
				boolean checkIP = ResourceEntity.checkAuthentication(remoteIp);
				
				RequestFailDTO requestFail = new RequestFailDTO();
				requestFail.setIP(remoteIp);
				requestFail.setRequest(xml);
				requestFail.setRequestType(RequestType.PUSH_SMS);
				
				if(!checkIP){
					response = ResponseUtils.getInstance().responseFail(PushConstant.RES_ERROR_AUTH, "IP khong hop le");
					
					requestFail.setError(PushConstant.RES_ERROR_AUTH);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_AUTH_DESC);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
		        
//				xml = xml.replace("&", "&amp;");
				
				xml = xml.replace("&", "[!@#$%]");
				
				ResourceEntity.blacklist = BlacklistDAO.getInstance().getBlackList();
				
				response = ResponseUtils.getInstance().responseDefault();
				
				PushSMSDTO pushSMS = EVNParser.getInstance().getPushSMS(xml);
				
				if(pushSMS == null){
					response = ResponseUtils.getInstance().responseFail(PushConstant.RES_ERROR_FORMAT, PushConstant.RES_ERROR_FORMAT_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_FORMAT);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_FORMAT_DESC);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				String username = pushSMS.getUsername();
				String password = pushSMS.getPassword();
				String brandname = pushSMS.getBrandName();
				String loaisp = pushSMS.getLoaiSP();
				ArrayList<MessageDTO> listMessage = pushSMS.getListMessage();
				
				if(!ResourceEntity.checkAuthentication(remoteIp, username, password)){
					
					response = ResponseUtils.getInstance().responseFail(PushConstant.RES_ERROR_AUTH, PushConstant.RES_ERROR_AUTH_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_AUTH);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_AUTH_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				if(PushConstant.LOAISP.indexOf(";" + loaisp + ";") < 0){
					response = ResponseUtils.getInstance().responseFail(PushConstant.RES_ERROR_LOAISP, PushConstant.RES_ERROR_LOAISP_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_LOAISP);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_LOAISP_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				if("2".equals(loaisp) && !ResourceEntity.checkBrandName(remoteIp, brandname)){
					response = ResponseUtils.getInstance().responseFail(PushConstant.RES_ERROR_BRANDNAME, PushConstant.RES_ERROR_BRANDNAME_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_BRANDNAME);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_BRANDNAME_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					requestFail.setBrandname(brandname);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
//				int totalSPR = pushSMS.getSumSMS();
				
//				if(listMessage.size() > Constants.MAX_NPR || totalSPR > Constants.MAX_NPR){
				if(listMessage.size() > Constants.MAX_NPR){
					response = ResponseUtils.getInstance().responseFail(PushConstant.RES_ERROR_MAXNPR, PushConstant.RES_ERROR_MAXNPR_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_MAXNPR);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_MAXNPR_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					requestFail.setBrandname(brandname);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				String serviceID = Constants.SERVICE_ID;
				
				if("2".equals(loaisp)){
					serviceID = brandname;
				}
				
				pushSMS.setServiceID(serviceID);
				pushSMS.setPartner(ResourceEntity.listPartners.get(remoteIp).getName());
				
				Collections.sort(pushSMS.getListMessage());
				
				boolean pushMT = MTQueueDAO.getInstance().insertMTQueue(pushSMS);
				boolean requestLog = RequestDAO.getInstance().insertRequestPushSMS(pushSMS);
				
				if(!pushMT || !requestLog){
					response = ResponseUtils.getInstance().responseFail(PushConstant.RES_ERROR_DATABASE, PushConstant.RES_ERROR_DATABASE_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_DATABASE);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_DATABASE_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					requestFail.setBrandname(serviceID);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				response = ResponseUtils.getInstance().responsePushSMS(pushSMS);
				
				Utils.logger.info("Response: " + response);
					
				end = System.currentTimeMillis();
				Utils.logger.info("Time executed: " + (end - start) + " ms");
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return response;
		
	}
	
	public String bulkSms(String xml){
		
		String response = null;
		
		try{
			
			Utils.logger.info("Request: " + xml);
			
			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			
			
			
				MessageContext curContext = MessageContext.getCurrentMessageContext();
				String remoteIp = "Unknown";

				if (curContext != null) {
					Object ipProperty = curContext.getProperty(MessageContext.REMOTE_ADDR);
					remoteIp = ipProperty.toString();
				}
				
				Utils.logger.info("Remote IP: " + remoteIp);
				
//				boolean checkIPForward = Constants.IP.indexOf(remoteIp) >= 0 ? true : false;
				
				boolean checkIP = ResourceEntity.checkAuthentication(remoteIp);
				
				RequestFailDTO requestFail = new RequestFailDTO();
				requestFail.setIP(remoteIp);
				requestFail.setRequest(xml);
				requestFail.setRequestType(RequestType.BULK_SMS);
				
				if(!checkIP){
					response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_AUTH, "IP khong hop le");
					
					requestFail.setError(PushConstant.RES_ERROR_AUTH);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_AUTH_DESC);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				xml = xml.replace("&", "&amp;");
				
				ResourceEntity.blacklist = BlacklistDAO.getInstance().getBlackList();
				
				response = ResponseUtils.getInstance().responseDefault();
				
				BulkSMSDTO bulkSMS = EVNParser.getInstance().getBulkSMS(xml);
				
				if(bulkSMS == null){
					response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_FORMAT, BulkConstant.RES_ERROR_FORMAT_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_FORMAT);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_FORMAT_DESC);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				String username = bulkSMS.getUsername();
				String password = bulkSMS.getPassword();
				String brandname = bulkSMS.getBrandName();
				String loaisp = bulkSMS.getLoaiSP();
				
				ArrayList<Icom.DTO.BulkSMSDTO.MessageDTO> listMessage = bulkSMS.getListMsisdn();
				
				if(!ResourceEntity.checkAuthentication(remoteIp, username, password)){
					response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_AUTH, BulkConstant.RES_ERROR_AUTH_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_AUTH);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_AUTH_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				if(BulkConstant.LOAISP.indexOf(";" + loaisp + ";") < 0){
					response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_LOAISP, BulkConstant.RES_ERROR_LOAISP_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_LOAISP);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_LOAISP_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				if("2".equals(loaisp) && !ResourceEntity.checkBrandName(remoteIp, brandname)){
					response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_BRANDNAME, BulkConstant.RES_ERROR_BRANDNAME_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_BRANDNAME);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_BRANDNAME_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					requestFail.setBrandname(brandname);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
//				int remainSMSBrandname = PartnerDAO.getInstance().getCountSMSBrandname(brandname);
//				int totalSPR = bulkSMS.getSumSMS();
				
//				if(listMessage.size() > Constants.MAX_NPR || totalSPR > Constants.MAX_NPR || totalSPR > remainSMSBrandname){
//				if(listMessage.size() > Constants.MAX_NPR || totalSPR > Constants.MAX_NPR){
				if(listMessage.size() > Constants.MAX_NPR){
					response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_MAXNPR, BulkConstant.RES_ERROR_MAXNPR_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_MAXNPR);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_MAXNPR_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					requestFail.setBrandname(brandname);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				String serviceID = Constants.SERVICE_ID;
				
				if("2".equals(loaisp)){
					serviceID = brandname;
				}
				
				bulkSMS.setServiceID(serviceID);
				bulkSMS.setPartner(ResourceEntity.listPartners.get(remoteIp).getName());
				
				boolean pushMT = MTQueueDAO.getInstance().insertMTQueue(bulkSMS);
				
				boolean requestLog = RequestDAO.getInstance().insertRequestBulkSMS(bulkSMS);
				
				if(!pushMT || !requestLog){
					response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_DATABASE, BulkConstant.RES_ERROR_DATABASE_DESC);
					
					requestFail.setError(PushConstant.RES_ERROR_DATABASE);
					requestFail.setErrorDesc(PushConstant.RES_ERROR_DATABASE_DESC);
					requestFail.setUsername(username);
					requestFail.setPassword(password);
					requestFail.setBrandname(serviceID);
					
					RequestDAO.getInstance().insertRequestFail(requestFail);
					
					Utils.logger.info("Response: " + response);
					
					end = System.currentTimeMillis();
					Utils.logger.info("Time executed: " + (end - start) + " ms");
					
					return response;
				}
				
				response = ResponseUtils.getInstance().responseBulkSMS(bulkSMS);
				
				Utils.logger.info("Response: " + response);
				
				end = System.currentTimeMillis();
				Utils.logger.info("Time executed: " + (end - start) + " ms");
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
			
		return response;
		
	}
	
	public String checkstatus(String xml){
		
		String response = null;
		
		try{
			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			
			MessageContext curContext = MessageContext.getCurrentMessageContext();
			String remoteIp = "Unknown";

			if (curContext != null) {
				Object ipProperty = curContext.getProperty(MessageContext.REMOTE_ADDR);
				remoteIp = ipProperty.toString();
			}
			
			Utils.logger.info("Remote IP: " + remoteIp);
			
//			boolean checkIPForward = Constants.IP.indexOf(remoteIp) >= 0 ? true : false;
			
			boolean checkIP = ResourceEntity.checkAuthentication(remoteIp);
			
			if(!checkIP){
				response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_AUTH, BulkConstant.RES_ERROR_AUTH_DESC);
				
				end = System.currentTimeMillis();
				Utils.logger.info("Time executed: " + (end - start) + " ms");
				
				return response;
			}
			
			xml = xml.replace("&", "&amp;");
			
			response = ResponseUtils.getInstance().responseDefault();
			
			CheckStatusDTO checkstatus = EVNParser.getInstance().getCheckStatus(xml);
			
			if(checkstatus == null){
				response = ResponseUtils.getInstance().responseFail(StatusConstant.RES_ERROR_FORMAT, StatusConstant.RES_ERROR_FORMAT_DESC);
				
				Utils.logger.info("Response: " + response);
				
				end = System.currentTimeMillis();
				Utils.logger.info("Time executed: " + (end - start) + " ms");
				
				return response;
			}
			
			String username = checkstatus.getUsername();

			ArrayList<String> listMessage = checkstatus.getListMessageID();
			
			if(!ResourceEntity.checkAuthentication(username)){
				response = ResponseUtils.getInstance().responseFail(BulkConstant.RES_ERROR_AUTH, BulkConstant.RES_ERROR_AUTH_DESC);
				
				Utils.logger.info("Response: " + response);

				end = System.currentTimeMillis();
				Utils.logger.info("Time executed: " + (end - start) + " ms");
				
				return response;
			}
			
			if(listMessage.size() > Constants.MAX_NPR){
				response = ResponseUtils.getInstance().responseFail(StatusConstant.RES_ERROR_MAXNPR, StatusConstant.RES_ERROR_MAXNPR_DESC);
				
				Utils.logger.info("Response: " + response);
				
				end = System.currentTimeMillis();
				Utils.logger.info("Time executed: " + (end - start) + " ms");
				
				return response;
			}
			
			Hashtable<String, CheckStatusDTO.MessageDTO> listResponse = new Hashtable<String, CheckStatusDTO.MessageDTO>();
			listResponse = RequestDAO.getInstance().checkStatus(listMessage);
			
			response = ResponseUtils.getInstance().responseCheckStatus(listResponse);
			
			Utils.logger.info("Response: " + response);
			
			end = System.currentTimeMillis();
			Utils.logger.info("Time executed: " + (end - start) + " ms");
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return response;
		
	}
	
	public String updateData(String xml){
		
		try{
				Utils.logger.info("Request: " + xml);
			
				MessageContext curContext = MessageContext.getCurrentMessageContext();
				String remoteIp = "Unknown";

				if (curContext != null) {
					Object ipProperty = curContext.getProperty(MessageContext.REMOTE_ADDR);
					remoteIp = ipProperty.toString();
				}
				
				Utils.logger.info("Remote IP: " + remoteIp);
				
				boolean checkIP = false;
				
				if(Constants.IP.equals(remoteIp)){
					checkIP = true;
				}
				
				if(!checkIP){
					return "0";
				}
		        
				String username = Utils.getValueTagStart(xml, "USERNAME");
				String password = Utils.getValueTagStart(xml, "PASSWORD");
				
				if(username.equals(Constants.USERNAME) && password.equals(Constants.PASSWORD)){
					ResourceEntity.reloadResource();
					return "1";
				}
				
				
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return "0";
		
	}
	
	public static void main(String[] args) {
//		String xml = "<ICOM>"
//					+	"<USERNAME>icom</USERNAME>"
//					+	"<PASSWORD>icom@123</PASSWORD>"
//					+  "</ICOM>";
//		String username = Utils.getValueTagStart(xml, "USERNAME");
//		String password = Utils.getValueTagStart(xml, "PASSWORD");
//		
//		System.out.println(username + "-" + password);
		String msisdn = "84966080875";
		String brandname = "PCNamDinh,PCPhuTho,PC.QNinh,PC.TNguyen,PCBacGiang,PCThanhHoa,PCThaiBinh,PCYenBai,PCLangSon,PCBacNinh,PC.TQuang,PCNgheAn,PCCaoBang,PCSonLa,PCHaTinh,PCHoaBinh,PCLaoCai,PCDienBien,PCHaGiang,PCBacCan,EVNNPC,PCHaNam,PCHaiDuong,PCHaiPhong,PCLaiChau,PCNinhBinh,PCHungYen,PCVinhPhuc,CSKH.EVNNPC";
		
		for(String userid : msisdn.split(",")){
			for(String brad : brandname.split(",")){
				String sql = "insert into mt_queue(USER_ID,SERVICE_ID,MOBILE_OPERATOR,CONTENT_TYPE,COMMAND_CODE,INFO,SUBMIT_DATE,DONE_DATE, MESSAGE_TYPE) values ('" + userid + "', '" + brad +"', 'VIETTEL', 0, 'EVN', 'Test brandname " + brad + "', now(), now(), 0);";
				System.out.println(sql);
			}
		}
		
	}
}

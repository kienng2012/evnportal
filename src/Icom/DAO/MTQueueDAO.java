package Icom.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import Icom.API.ResourceEntity;
import Icom.DTO.BulkSMSDTO;
import Icom.DTO.PushMTPartnerDTO;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.RequestDTO;
import Icom.DTO.PushSMSDTO.MessageDTO;
import Icom.Utils.Constants;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class MTQueueDAO {
	private MTQueueDAO(){
		
	}
	
	private static MTQueueDAO mInstance;
	private static Object mLock = new Object();
	
	public static MTQueueDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new MTQueueDAO();
			}
			return mInstance;
		}
	}

	public boolean insertMTQueue(PushSMSDTO pushSMS){
		
		Hashtable<String, PushMTPartnerDTO> arrTableName = new Hashtable<String, PushMTPartnerDTO>();
		arrTableName = PushMTPartnerDAO.getInstance().getTableName();
		
		Hashtable<String, String> arrSQL = new Hashtable<String, String>();
		
		String sql = "INSERT INTO $TABLE_NAME$ (`USER_ID`, `SERVICE_ID`, `MOBILE_OPERATOR`, `COMMAND_CODE`, `CONTENT_TYPE`, `INFO`, `SUBMIT_DATE`, `DONE_DATE`, `PROCESS_RESULT`, `MESSAGE_TYPE`, `REQUEST_ID`, `MSGID`, `COUNT_SMS`, `MD5`, `AMOUNT_SALE`, `AMOUNT_PURCHASE`) VALUES ";
		
		String responseTable = "response" + new SimpleDateFormat(Constants.RESPONSE_TABLE_FORMAT).format(new Date());
		
		String responseSQL = "INSERT INTO " + responseTable + " (`USER_ID`, `SERVICE_ID`, `MOBILE_OPERATOR`, `COMMAND_CODE`, `CONTENT_TYPE`, `INFO`, `SUBMIT_DATE`, `DONE_DATE`, `PROCESS_RESULT`, `MESSAGE_TYPE`, `REQUEST_ID`, `MSGID`, `COUNT_SMS`, `MD5`, `AMOUNT_SALE`, `AMOUNT_PURCHASE`) VALUES ";
		
		String mtlog = "mt" + new SimpleDateFormat(Constants.MTLOG_TABLE_FORMAT).format(new Date());
		
		String logSQL = "INSERT INTO " + mtlog + " (`USER_ID`, `SERVICE_ID`, `MOBILE_OPERATOR`, `COMMAND_CODE`, `CONTENT_TYPE`, `INFO`, `SUBMIT_DATE`, `DONE_DATE`, `PROCESS_RESULT`, `MESSAGE_TYPE`, `REQUEST_ID`, `MSGID`, `COUNT_SMS`, `MD5`, `AMOUNT_SALE`, `AMOUNT_PURCHASE`) VALUES ";
		
		ArrayList<MessageDTO> listMessage = pushSMS.getListMessage();
		
		String serviceID = pushSMS.getServiceID();
		String command_code = pushSMS.getPartner();
		
		int amount_sale = ResourceEntity.listBrandnames.get(";" + pushSMS.getPartner() + ";" + serviceID + ";").getAmountSale(); 
		int amount_purchase = ResourceEntity.listBrandnames.get(";" + pushSMS.getPartner() + ";" + serviceID + ";").getAmountPurchase();
		
		boolean checkLog = false;
		boolean check = false;
			
			for(MessageDTO message : listMessage){
				String resInfo = message.getResInfo();
				String msisdn = message.getMsisdn();
				String info = message.getContent();
				String requestID = new Date().getTime() + "";
				String messageID = message.getMsgID();
				String operator = message.getOperator();
				int countSMS = message.getCountSMS();
				String md5 = Utils.getInstance().getMD5(info);
				
				System.out.println(resInfo);
				
				//tin nhan sach, okie co the push di
				if("0".equals(resInfo)){
					check = true;
					
					String mysql = arrSQL.get(operator);
					
					
					//SWITCH KENH TU BANG MT-PARTNER (bảng này để switch kênh xem Gửi SMS theo đường nào ? VMG or GW VTE ( GW VTE chỉ để gửi tin nhắn mạng VTE )
					if(mysql == null){
						String table_name = arrTableName.get(operator).getMt_queue();
						mysql = sql.replace("$TABLE_NAME$", table_name);
						mysql = mysql + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
						
					}else{
						mysql = mysql + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
					}
					
					responseSQL = responseSQL + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
					//TODO : Loc MT trung nhau
					arrSQL.put(operator, mysql);
					
				}else{
					// tin nhan lap, sai format,...
					checkLog = true;
					logSQL = logSQL + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '" + resInfo + "', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
				}
				 
			}
			
			for(String key : arrSQL.keySet()){
				String mysql = arrSQL.get(key);
				mysql = mysql.substring(0, mysql.length() - 1);
				arrSQL.put(key, mysql);
				Utils.logger.info(mysql);
			}
			
			responseSQL = responseSQL.substring(0, responseSQL.length() - 1);
			
			logSQL = logSQL.substring(0, logSQL.length() - 1);
			
			Utils.logger.info("responseSQL: " + responseSQL);
			Utils.logger.info("logSQL: " + logSQL);
			Connection con = null;
			Statement pstm = null;
			
			try {
				if(con == null){
					con = DBPool.getInstance().getConnectionSub();
					con.setAutoCommit(false);
					
					pstm = con.createStatement();
					
					if(check){
						for(String key : arrSQL.keySet()){
							String mysql = arrSQL.get(key);
							if(mysql != null){
								pstm.addBatch(mysql);
							}
						}
						
						pstm.addBatch(responseSQL);
					}
					
					if(checkLog){
						pstm.addBatch(logSQL);
					}
					
					pstm.executeBatch();
					
					con.commit();
					
					return true;
				}
			} catch (SQLException e) {
				
				try {
					con.rollback();
				} catch (SQLException e1) {
					Utils.logger.printStackTrace(e1);
				}
				
//				ResourceEntity.listMsisdn = new Hashtable<String, ArrayList<RequestDTO>>(ResourceEntity.listMsisdnBackup);
				
				ResourceEntity.listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>(ResourceEntity.listMsisdnBackup);
				
				Utils.logger.printStackTrace(e);
			} catch (Exception e) {
				Utils.logger.printStackTrace(e);
			} finally {
				DBPool.getInstance().cleanup(pstm);
				DBPool.getInstance().cleanup(con);
			}
		
		
		return false;
	}
	
	public boolean insertMTQueue(BulkSMSDTO bulkSMS){
		
		Hashtable<String, PushMTPartnerDTO> arrTableName = new Hashtable<String, PushMTPartnerDTO>();
		arrTableName = PushMTPartnerDAO.getInstance().getTableName();
		
		Hashtable<String, String> arrSQL = new Hashtable<String, String>();
		
		String sql = "INSERT INTO $TABLE_NAME$ (`USER_ID`, `SERVICE_ID`, `MOBILE_OPERATOR`, `COMMAND_CODE`, `CONTENT_TYPE`, `INFO`, `SUBMIT_DATE`, `DONE_DATE`, `PROCESS_RESULT`, `MESSAGE_TYPE`, `REQUEST_ID`, `MSGID`, `COUNT_SMS`, `MD5`, `AMOUNT_SALE`, `AMOUNT_PURCHASE`) VALUES ";
		
		String responseTable = "response" + new SimpleDateFormat(Constants.RESPONSE_TABLE_FORMAT).format(new Date());
		
		String responseSQL = "INSERT INTO " + responseTable + " (`USER_ID`, `SERVICE_ID`, `MOBILE_OPERATOR`, `COMMAND_CODE`, `CONTENT_TYPE`, `INFO`, `SUBMIT_DATE`, `DONE_DATE`, `PROCESS_RESULT`, `MESSAGE_TYPE`, `REQUEST_ID`, `MSGID`, `COUNT_SMS`, `MD5`, `AMOUNT_SALE`, `AMOUNT_PURCHASE`) VALUES ";
		
		String mtlog = "mt" + new SimpleDateFormat(Constants.MTLOG_TABLE_FORMAT).format(new Date());
		
		String logSQL = "INSERT INTO " + mtlog + " (`USER_ID`, `SERVICE_ID`, `MOBILE_OPERATOR`, `COMMAND_CODE`, `CONTENT_TYPE`, `INFO`, `SUBMIT_DATE`, `DONE_DATE`, `PROCESS_RESULT`, `MESSAGE_TYPE`, `REQUEST_ID`, `MSGID`, `COUNT_SMS`, `MD5`, `AMOUNT_SALE`, `AMOUNT_PURCHASE`) VALUES ";
		
		ArrayList<Icom.DTO.BulkSMSDTO.MessageDTO> listMessage = bulkSMS.getListMsisdn();
		
		String serviceID = bulkSMS.getServiceID();
		String command_code = bulkSMS.getPartner();
		String info = bulkSMS.getContent();
		String messageID = bulkSMS.getMsgID();
		int countSMS = bulkSMS.getCountSMS();
		String md5 = Utils.getInstance().getMD5(info);
		
		int amount_sale = ResourceEntity.listBrandnames.get(";" + bulkSMS.getPartner() + ";" + serviceID + ";").getAmountSale();
		int amount_purchase = ResourceEntity.listBrandnames.get(";" + bulkSMS.getPartner() + ";" + serviceID + ";").getAmountPurchase();
		
		boolean checkLog = false;
		boolean check = false;
		
		for(Icom.DTO.BulkSMSDTO.MessageDTO message : listMessage){
			String resInfo = message.getResInfo();
			
			String msisdn = message.getMsisdn();
			String requestID = new Date().getTime() + "";
			String operator = message.getOperator();
			
			
			if("0".equals(resInfo)){
				
				check = true;
				
				String mysql = arrSQL.get(operator);
				
				if(mysql == null){
					String table_name = arrTableName.get(operator).getMt_queue();
					mysql = sql.replace("$TABLE_NAME$", table_name);
					mysql = mysql + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
				}else{
					mysql = mysql + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
				}
				
				arrSQL.put(operator, mysql);
				
				responseSQL = responseSQL + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
				
			}else{
				checkLog = true;
				logSQL = logSQL + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', '" + command_code + "', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '" + resInfo + "', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','" + amount_sale + "','" + amount_purchase + "'),";
			}
			
			 
		}
		
		for(String key : arrSQL.keySet()){
			String mysql = arrSQL.get(key);
			mysql = mysql.substring(0, mysql.length() - 1);
			arrSQL.put(key, mysql);
		}
		
		responseSQL = responseSQL.substring(0, responseSQL.length() - 1);
		
		logSQL = logSQL.substring(0, logSQL.length() - 1);
		
		Utils.logger.info("responseSQL: " + responseSQL);
		Utils.logger.info("logSQL: " + logSQL);
		
		Connection con = null;
		Statement pstm = null;
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				con.setAutoCommit(false);
				
				pstm = con.createStatement();
				
				if(check){
//					pstm.addBatch(sql);
					for(String key : arrSQL.keySet()){
						String mysql = arrSQL.get(key);
						if(mysql != null){
							pstm.addBatch(mysql);
//							Utils.logger.info("logSQL: " + mysql);
						}
					}
					
					pstm.addBatch(responseSQL);
				}
				
				if(checkLog){
					pstm.addBatch(logSQL);
				}
				
				pstm.executeBatch();
				
				con.commit();
				return true;
			}
		} catch (SQLException e) {
			
			try {
				con.rollback();
			} catch (SQLException e1) {
				Utils.logger.printStackTrace(e1);
			}
			
			ResourceEntity.listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>(ResourceEntity.listMsisdnBackup);
			
			Utils.logger.printStackTrace(e);
		} catch (Exception e) {
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		
		return false;
	}
}

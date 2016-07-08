package Icom.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import sun.security.krb5.Config;
import Icom.Constant.RequestType;
import Icom.DTO.BulkSMSDTO;
import Icom.DTO.CheckStatusDTO;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.PushSMSDTO.MessageDTO;
import Icom.DTO.RequestFailDTO;
import Icom.Utils.Constants;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class RequestDAO {
	private RequestDAO(){
		
	}
	
	private static RequestDAO mInstance;
	private static Object mLock = new Object();
	
	public static RequestDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new RequestDAO();
			}
			return mInstance;
		}
	}
	
//	+--------------+--------------+------+-----+-------------------+----------------+
//	| Field        | Type         | Null | Key | Default           | Extra          |
//	+--------------+--------------+------+-----+-------------------+----------------+
//	| id           | bigint(20)   | NO   | PRI | NULL              | auto_increment |
//	| msisdn       | varchar(100) | YES  | MUL | NULL              |                |
//	| service_id   | varchar(100) | YES  | MUL | NULL              |                |
//	| msgid        | varchar(100) | YES  | MUL | NULL              |                |
//	| content      | text         | YES  |     | NULL              |                |
//	| loaisp       | int(11)      | YES  |     | NULL              |                |
//	| request_type | int(11)      | YES  | MUL | NULL              |                |
//	| insert_date  | timestamp    | NO   | MUL | CURRENT_TIMESTAMP |                |
//	+--------------+--------------+------+-----+-------------------+----------------+
	
	public boolean insertRequestPushSMS(PushSMSDTO pushSMS){
//		String tableName = Constants.REQUEST_TABLE + new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		String tableName = Constants.REQUEST_TABLE + new SimpleDateFormat(Icom.Utils.Config.getInstance().getProperty("request_table_format")).format(new Date());
		String sql = "INSERT INTO " + tableName + " (`loaisp`, `service_id`, `msgid`, `msisdn`, `content`, `request_type`, `operator`, `res_info`) VALUES ";
		
		ArrayList<MessageDTO> listMessage = pushSMS.getListMessage();
		
		String serviceID = pushSMS.getServiceID();
		String username = pushSMS.getUsername();
		String password = pushSMS.getPassword();
		String loaisp = pushSMS.getLoaiSP();
		int requestType = RequestType.PUSH_SMS;
		
		for(MessageDTO message : listMessage){
			String msisdn = message.getMsisdn();
			String info = message.getContent();
			String messageID = message.getMsgID();
			String operator = message.getOperator();
			String resInfo = message.getResInfo();
			
			sql = sql + "('" + loaisp + "', '" + serviceID + "', '" + messageID +"', '" + msisdn + "','" + info + "','" + requestType + "','" + operator + "','" + resInfo + "'),"; 
		}
		
		sql = sql.substring(0, sql.length() - 1);
		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				con.setAutoCommit(false);
				
				pstm = con.prepareStatement(sql);
				pstm.execute();
				
				con.commit();
				return true;
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				Utils.logger.printStackTrace(e1);
			}
			Utils.logger.printStackTrace(e);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		
		return false;
	}
	
	public boolean insertRequestBulkSMS(BulkSMSDTO bulkSMS){
//		String tableName = Constants.REQUEST_TABLE + new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		String tableName = Constants.REQUEST_TABLE + new SimpleDateFormat(Icom.Utils.Config.getInstance().getProperty("request_table_format")).format(new Date());
		
		String sql = "INSERT INTO " + tableName + " (`loaisp`, `service_id`, `msgid`, `msisdn`, `content`, `request_type`, `operator`, `res_info`) VALUES ";
		
		ArrayList<Icom.DTO.BulkSMSDTO.MessageDTO> listMessage = bulkSMS.getListMsisdn();
		
		String serviceID = bulkSMS.getServiceID();
		String loaisp = bulkSMS.getLoaiSP();
		String info = bulkSMS.getContent();
		String messageID = bulkSMS.getMsgID();
		
		int requestType = RequestType.BULK_SMS;
		
		for(Icom.DTO.BulkSMSDTO.MessageDTO message : listMessage){
			String msisdn = message.getMsisdn();
			String operator = message.getOperator();
			String resInfo = message.getResInfo();
			
			sql = sql + "('" + loaisp + "', '" + serviceID + "', '" + messageID +"', '" + msisdn + "','" + info + "','" + requestType + "','" + operator + "','" + resInfo + "'),"; 
		}
		
		sql = sql.substring(0, sql.length() - 1);
		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				con.setAutoCommit(false);
				
				pstm = con.prepareStatement(sql);
				pstm.execute();
				
				con.commit();
				return true;
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				Utils.logger.printStackTrace(e1);
			}
			Utils.logger.printStackTrace(e);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		
		return false;
	}
	
//	+--------------+--------------+------+-----+---------+----------------+
//	| Field        | Type         | Null | Key | Default | Extra          |
//	+--------------+--------------+------+-----+---------+----------------+
//	| id           | bigint(11)   | NO   | PRI | NULL    | auto_increment |
//	| request      | text         | YES  |     | NULL    |                |
//	| ip           | varchar(200) | YES  |     | NULL    |                |
//	| username     | varchar(200) | YES  |     | NULL    |                |
//	| password     | varchar(200) | YES  |     | NULL    |                |
//	| request_type | int(11)      | NO   |     | 0       |                |
//	| error        | int(11)      | YES  |     | NULL    |                |
//	+--------------+--------------+------+-----+---------+----------------+
	
	public boolean insertRequestFail(RequestFailDTO request){
		
		String tableName = Constants.REQUEST_FAIL_TABLE + "_" + new SimpleDateFormat("yyyyMM").format(new Date());
		
		String sql = "Insert into " + tableName + " (request, ip, username, password, request_type, error, error_desc) VALUES ('" + request.getRequest() + "','" + request.getIP() + "','" + request.getUsername() + "','" + request.getPassword() + "','" + request.getRequestType() + "','" + request.getError() + "','" + request.getErrorDesc() + "')";
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				con.setAutoCommit(false);
				pstm = con.prepareStatement(sql);
				pstm.execute();
				con.commit();
				return true;
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				Utils.logger.printStackTrace(e1);
			}
			Utils.logger.printStackTrace(e);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		
		return false;
	}
	
	public Hashtable<String, CheckStatusDTO.MessageDTO> checkStatus(ArrayList<String> listMessageID){
		
		Hashtable<String, String> listTable = new Hashtable<String, String>();
		
		Hashtable<String, CheckStatusDTO.MessageDTO> result = null;
		CheckStatusDTO statusDTO = null;
		
		for(String msgID : listMessageID){
			String[] params = msgID.split("[|]");
			String yyyyMM = params[params.length - 1];
			
			String year = yyyyMM.substring(0, 4);
			String month = yyyyMM.substring(4, yyyyMM.length());
			
			if(month.length() == 1 && Integer.parseInt(month) < 10){
				month = "0" + month;
			}
			
			yyyyMM = year + month;
			
			if(listTable.get(yyyyMM) == null){
				listTable.put(yyyyMM, "'" + msgID + "'");
			}else{
				String listMSGID = listTable.get(yyyyMM); 
				listTable.put(yyyyMM, listMSGID + ",'" + msgID + "'");
			}
		}
			for(String key : listTable.keySet()){
				String condition = listTable.get(key);
				listTable.put(key, condition);
			}
			
			String sql = "";
			
			for(String key : listTable.keySet()){
				
				String table = "mt" + key;
				String condition = listTable.get(key);
				sql = sql + "Select MSGID, USER_ID, PROCESS_RESULT from " + table + " where MSGID in (" + condition + ")";
				sql = sql + " UNION ALL ";
			}
			
			sql = sql.substring(0, sql.lastIndexOf("UNION ALL"));
			sql = sql.trim();
			
			System.out.println(sql);
			
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				
				while(rs.next()){
					if(result == null){
						result = new Hashtable<String, CheckStatusDTO.MessageDTO>();
					}
					
					if(statusDTO == null){
						statusDTO = new CheckStatusDTO();
					}
					
					CheckStatusDTO.MessageDTO message = statusDTO.new MessageDTO();
					message.setMessageID(rs.getString("MSGID"));
					message.setMsisdn(rs.getString("USER_ID"));
					message.setStatus(rs.getInt("PROCESS_RESULT"));
					
					result.put(message.getMessageID(), message);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(rs);
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		
		return result;
	}
	
	/**
	 * 1 thue bao khong duoc nhan  cung 1 content, cung 1 messageID trong 24h
	 * @param content
	 * @param messageID
	 * @return
	 */
	public static boolean check24Hour(String content, String messageID){
//		String[] params = messageID.split("[|]");
//		String yyyyMM = params[params.length - 1];
		String md5 = Utils.getInstance().getMD5(content);
		
		String yyyyMM = new SimpleDateFormat("yyyyMM").format(new Date());
		String dd = new SimpleDateFormat("dd").format(new Date());
		
		String mysql = null;
		
		if("01".equals(dd)){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			String tablename = "mt" + new SimpleDateFormat("yyyyMM").format(c.getTime()); 
			mysql = "Select id from " + tablename + " where date(SUBMIT_DATE) = date(date_sub(now(), interval 1 day)) and md5 = '" + md5 + "' and MSGID = '" + messageID + "' and SUBMIT_DATE >= date_sub(now(), interval 24 hour)";
		}
		
		String sql = "Select id from mt" + yyyyMM + " where md5 = '" + md5 + "' and MSGID = '" + messageID + "' and SUBMIT_DATE >= date_sub(now(), interval 24 hour)";
		if(mysql != null){
			sql = sql + " UNION ALL " + mysql;
		}
		
		
		Utils.logger.info("Request check24Hour::SQL:: " + sql);
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				if(rs.next()){
					return true;
				}
			}
		} catch (Exception e) {
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(rs);
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		return false;
	}
	
	/**
	 * 1 thue bao khong duoc nhan  cung 1 content, khac messageID trong 15 minute
	 * @param content
	 * @param messageID
	 * @return
	 */
	public static boolean check15Minute(String content, String messageID){
//		String[] params = messageID.split("[|]");
//		String yyyyMM = params[params.length - 1];
		
		String md5 = Utils.getInstance().getMD5(content);
		
		String yyyyMM = new SimpleDateFormat("yyyyMM").format(new Date());
		String dd = new SimpleDateFormat("dd").format(new Date());
		
		String mysql = null;
		
		if("01".equals(dd)){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			String tablename = "mt" + new SimpleDateFormat("yyyyMM").format(c.getTime()); 
			mysql = "Select id from " + tablename + " where date(SUBMIT_DATE) = date(date_sub(now(), interval 1 day)) and md5 = '" + md5 + "' and MSGID <> '" + messageID + "' and SUBMIT_DATE >= date_sub(now(), interval 15 minute)";
		}
		
		String sql = "Select id from mt" + yyyyMM + " where md5 = '" + md5 + "' and MSGID <> '" + messageID + "' and SUBMIT_DATE >= date_sub(now(), interval 15 minute)";
		if(mysql != null){
			sql = sql + " UNION ALL " + mysql;
		}
		
		Utils.logger.info("Request check15Minute::SQL:: " + sql);
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				if(rs.next()){
					return true;
				}
			}
		} catch (Exception e) {
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(rs);
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		return false;
	}
	
	public static void main(String[] args) {
//		ArrayList<String> listMessageID = new ArrayList<String>();
//		listMessageID.add("123|201601");
//		listMessageID.add("456|201602");
//		listMessageID.add("789|201601");
//		listMessageID.add("123|201603");
//		listMessageID.add("123|201601");
//		listMessageID.add("123|201602");
//		listMessageID.add("123|201604");
//		
//		RequestDAO.getInstance().checkStatus(listMessageID);
		
//		RequestDAO.getInstance().check15Minute("Anh Tung dep zai", "abc123egh");
		String date = "201610";
		String year = date.substring(0, 4);
		String month = date.substring(4, date.length());
		
		System.out.println(year);
		System.out.println(month);
	}
	
}

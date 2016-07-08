package Icom.XMLParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.activemq.network.ConduitBridge;

import Icom.API.ResourceEntity;
import Icom.DAO.PushMTPartnerDAO;
import Icom.DAO.TelcoUtils;
import Icom.DTO.PushMTPartnerDTO;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.RequestDTO;
import Icom.DTO.PushSMSDTO.MessageDTO;
import Icom.Utils.Config;
import Icom.Utils.Constants;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class FileLogParser {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
//		String line = "";
		try{
			
//			Utils.getInstance().loadLogger();
//			Config.getInstance().load();	
//			Constants.loadConfig();
//			DBPool.getInstance().ConfigDB();
			
			
			String[] arrFile = new String[]{"C:/Users/luonglv/Desktop/new23.txt"};
//			File fileOut = new File("C:/Users/luonglv/Desktop/request_new.txt");
//			ConcurrentHashMap<String, ArrayList<RequestDTO>> listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
//			
//			for(String filePath : arrFile){
//				if(!fileOut.exists()){
//					fileOut.createNewFile();
//				}
//				
//				FileWriter fw = new FileWriter(fileOut);
//				FileOutputStream fos = new FileOutputStream(fileOut);
				
//				try{
//					listMsisdn = (ConcurrentHashMap<String, ArrayList<RequestDTO>>) Utils.getInstance().loadData(filePath);
//					if(listMsisdn == null){
//						listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
//					}
//				}catch(Exception e){
//					if(listMsisdn == null){
//						listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
//					}
//					
//					Utils.logger.printStackTrace(e);
//				}
				
//				for(String key : listMsisdn.keySet()){
//					ArrayList<RequestDTO> arrRequest = listMsisdn.get(key);
//					for(RequestDTO request : arrRequest){
//						String line = key + "-" + request.getMsgID() + "-" + request.getMd5() + "-" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(request.getTimestamp().getTime()));
//						fw.write(line + "\n");
//					}
//				}
//				
//			}
			
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(arrFile[0]))));
				String result = "";
				String line;
				while((line = br.readLine()) != null){
//					if(line.startsWith("0")){
//						line = "84" + line.substring(1);
//					}
//					
//					if(!line.startsWith("84")){
//						line = "84" + line;
//					}
					
					result = result + "'" + line + "',";
				}
				
				result = result.substring(0, result.length() - 1);
				System.out.println(result);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			System.out.println("success");
			

		}catch(Exception e){
			e.printStackTrace();
//			System.out.println(line);
		}
		
	}

	private static void insertDB(ArrayList<MessageDTO> listMessage) {
		// You are going to succeed !

		
		String sql = "INSERT INTO mt_queue1 (`USER_ID`, `SERVICE_ID`, `MOBILE_OPERATOR`, `COMMAND_CODE`, `CONTENT_TYPE`, `INFO`, `SUBMIT_DATE`, `DONE_DATE`, `PROCESS_RESULT`, `MESSAGE_TYPE`, `REQUEST_ID`, `MSGID`, `COUNT_SMS`, `MD5`, `AMOUNT_SALE`, `AMOUNT_PURCHASE`) VALUES ";
			
			for(MessageDTO message : listMessage){
				String msisdn = message.getMsisdn();
				String info = message.getContent();
				String requestID = new Date().getTime() + "";
				String messageID = message.getMsgID();
				String operator = message.getOperator();
				int countSMS = message.getCountSMS();
				String md5 = Utils.getInstance().getMD5(info);
				String serviceID = message.getMsgID();
				
				sql = sql + "('" + msisdn + "', '" + serviceID + "', '" + operator + "', 'EVN', '0', '" + info + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', '0', '" + requestID + "', '" + messageID + "','" + countSMS + "','" + md5 + "','350','300'),";
					
				 
			}
			
			sql = sql.substring(0, sql.length() - 1);
			
			System.out.println(sql);
			
			Connection con = null;
			PreparedStatement pstm = null;
			
			try {
				if(con == null){
					con = DBPool.getInstance().getConnectionSub();
					con.setAutoCommit(false);
					
					pstm = con.prepareStatement(sql);
					
					pstm.execute();
					
					con.commit();
					
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
		
	
	}
}

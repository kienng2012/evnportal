package Icom.Process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Icom.Entities.MTQueueDTO;
import Icom.Utils.DBPool;
import Icom.Utils.HttpPost;
import Icom.Utils.TextConverter;
import Icom.Utils.Utils;

public class Test {
	public static void main(String[] args){
		
		String abc = "Dien luc Tien Du du kien ngung cap dien DCLN.La?c Ve?-DZ 373-E74 tu 5h0_14/07/2016 den 15h0_14/07/2016 de cai tao luoi dien . Tran trong";
		String xyz = TextConverter.unAccent(abc);
		//xyz = xyz.replaceAll("?", "");
		System.out.println(xyz);
		/*Utils.getInstance().loadConfig();	
		MTQueueDTO obj = new MTQueueDTO();
		obj.setUserId("84901755376");
		obj.setServiceId("PC.TNguyen");
		obj.setInfo("Test send VMS BrandName PCNamDinh");
		obj.setSumitDate(Utils.getInstance().getCurrentTime());
		String url = "http://smsbrandname.mobifone.com.vn:8080/PartnerAPI.asmx?wsdl";
		try {
			HttpPost.sendVmsRequest(url, obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*java.sql.Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
			
		try {
			conn = DBPool.getConnectionSucriber();
			if (conn != null) {
				String SQL_LOAD = "select * from brandname_test";
				stmt = conn.prepareStatement(SQL_LOAD, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

				if (stmt.execute()) {
					rs = stmt.getResultSet();
					while (rs.next()) {
						String brandName = rs.getString("brand_name");
						String aa = "INSERT INTO `mt_queue`(`USER_ID`,`SERVICE_ID`,`MOBILE_OPERATOR`,`COMMAND_CODE`,`CONTENT_TYPE`,`INFO`,`SUBMIT_DATE`,`DONE_DATE`,`PROCESS_RESULT`,`MESSAGE_TYPE`,`REQUEST_ID`,`MESSAGE_ID`,`TOTAL_SEGMENTS`,`RETRIES_NUM`,`insert_date`,`CPId`,`CHANNEL_TYPE`,`COUNT_SMS`,`MSGID`,`MD5`) select '84979850583','"+brandName+"','VIETTEL','EVN','0', CONCAT('Test gui qua brandname ', '"+brandName+"', NOW()), NOW(),NOW(),'0','0',DATE_FORMAT(NOW(), \"%Y%l%d%H%m%s\"),'0','0','0',CURRENT_TIMESTAMP, NULL,'0','0','0', '' from brandname_test;select sleep(10);";
						
						System.out.println(aa);
					}
				}
			} else {
				Utils.logger.error("@LoadMTOnline.LoadMT. Connection is null.");
			}
		} catch (SQLException ex3) {
			Utils.logger.error("@INProcess@LoadMT.SQLException:" + ex3.toString());
		} catch (Exception ex3) {
			Utils.logger.error("@INProcess@LoadMT.Exception:" + ex3.toString());
		} finally {
			DBPool.cleanup(rs);
			DBPool.cleanup(conn, stmt);
		}*/
	}
}

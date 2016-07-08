package Icom.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class TestlistDAO {
	
	public TestlistDAO(){
		
	}
	
	public static TestlistDAO mInstance;
	public static Object mLock = new Object();
	
	public static TestlistDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new TestlistDAO();
			}
			
			return mInstance;
		}
	}
	
	public ConcurrentHashMap<String, String> getTestList(){
		Connection cnn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		ConcurrentHashMap<String, String> lstBrandName = new ConcurrentHashMap<String, String>();
		try
		{			
			String	sqlSelect="SELECT * from testlist";	
			Utils.logger.info("getTestList @sql="+ sqlSelect);
			cnn=DBPool.getConnectionSub();
			stmt=cnn.prepareStatement(sqlSelect);		
			rs=stmt.executeQuery();
			while(rs.next())
			{
				String msisdn = rs.getString("user_id");
				
				lstBrandName.put(msisdn, msisdn);
				
			}			
		}
		catch(Exception ex)
		{
			Utils.logger.error("Error at get customer_testlist :"+ex.getMessage());
			Utils.logger.printStackTrace(ex);
		}
		finally
		{
			DBPool.cleanup(rs);
			DBPool.cleanup(cnn,stmt);
		}
		return lstBrandName;
	}
	
	public boolean checkTestlist(String msisdn, Hashtable<String, String> lstBlackList){
		boolean check = false;
		try {
			String blacklist = lstBlackList.get(msisdn);
			if(blacklist != null){
				check = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			check = false;
		}
		
		return check;
	}
	
	public boolean checkBlacklist(String msisdn, String service_id, Hashtable<String, String> lstBlackList){
		boolean check = false;
		try {
			String blacklist = lstBlackList.get(msisdn);
			if(blacklist != null){
				check = true;
			}else{
				blacklist = lstBlackList.get(msisdn + service_id);
				if(blacklist != null){
					check = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			check = false;
		}
		
		return check;
	}
}

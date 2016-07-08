package Icom.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class BlacklistDAO {
	
	public BlacklistDAO(){
		
	}
	
	public static BlacklistDAO mInstance;
	public static Object mLock = new Object();
	
	public static BlacklistDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new BlacklistDAO();
			}
			
			return mInstance;
		}
	}
	
	public Hashtable<String, String> getBlackList(){
		Connection cnn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		Hashtable<String, String> lstBrandName = new Hashtable<String, String>();
		try
		{			
			String	sqlSelect="SELECT * from customer_blacklist";	
			Utils.logger.info("getBlackList @sql="+ sqlSelect);
			cnn=DBPool.getConnectionSub();
			stmt=cnn.prepareStatement(sqlSelect);		
			rs=stmt.executeQuery();
			while(rs.next())
			{
//				lstBrandName.put(rs.getString("user_id"), rs.getString("user_id"));
				String msisdn = rs.getString("user_id");
				String brandname = rs.getString("brandname");
				
				if(brandname == null){
					brandname = "";
				}
				
				lstBrandName.put(msisdn + brandname, msisdn);
				
			}			
		}
		catch(Exception ex)
		{
			Utils.logger.error("Error at get customer_blacklist :"+ex.getMessage());
			Utils.logger.printStackTrace(ex);
		}
		finally
		{
			DBPool.cleanup(rs);
			DBPool.cleanup(cnn,stmt);
		}
		return lstBrandName;
	}
	
	public boolean checkBlacklist(String msisdn, Hashtable<String, String> lstBlackList){
		boolean check = false;
		try {
			String blacklist = lstBlackList.get(msisdn);
			if(blacklist.length() > 0){
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

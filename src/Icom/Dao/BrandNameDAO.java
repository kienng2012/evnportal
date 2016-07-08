package Icom.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

import Icom.Entities.MTQueueDTO;
import Icom.Process.Main;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class BrandNameDAO {
	public BrandNameDAO(){
		
	}
	
	public static BrandNameDAO mInstance;
	public static Object mLock = new Object();
	public static BrandNameDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new BrandNameDAO();
			}
			return mInstance;
		}
	}
	public Hashtable<String, String> getListBrandName(){
		Connection cnn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		Hashtable<String, String> lstBrandName = new Hashtable<String, String>();
		try
		{			
			String	sqlSelect="SELECT * from brandname_test";	
			cnn=DBPool.getConnectionSub();
			stmt=cnn.prepareStatement(sqlSelect);		
			rs=stmt.executeQuery();
			while(rs.next())
			{
				lstBrandName.put(rs.getString("brand_name"), rs.getString("brand_name_viettel"));
				
			}			
		}
		catch(Exception ex)
		{
			Utils.logger.error("Error at get free list :"+ex.getMessage());
			Utils.logger.printStackTrace(ex);
		}
		finally
		{
			DBPool.cleanup(rs);
			DBPool.cleanup(cnn,stmt);
		}
		return lstBrandName;
	}
	
	public Hashtable<String, AtomicInteger> getListQuota(){
		Connection cnn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		Hashtable<String, AtomicInteger> lstBrandName = new Hashtable<String, AtomicInteger>();
		try
		{			
			String	sqlSelect="SELECT * from brandname_telco";	
			cnn=DBPool.getConnectionSub();
			stmt=cnn.prepareStatement(sqlSelect);		
			rs=stmt.executeQuery();
			while(rs.next())
			{
				lstBrandName.put(";" + rs.getString("brt_telco_name") + ";" + rs.getString("brt_brandname") + ";", new AtomicInteger(rs.getInt("brt_quota_remain")));
			}			
		}
		catch(Exception ex)
		{
			Utils.logger.error("Error at get free list :"+ex.getMessage());
			Utils.logger.printStackTrace(ex);
		}
		finally
		{
			DBPool.cleanup(rs);
			DBPool.cleanup(cnn,stmt);
		}
		return lstBrandName;
	}
	
	public String getBrandName(String brandName, Hashtable<String, String> lstBrandName){
		String result = brandName;
		try {
			result = lstBrandName.get(brandName);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public Hashtable<String, Timestamp> getListUserSend(){
		Connection cnn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		Hashtable<String, Timestamp> lstUserSend = new Hashtable<String, Timestamp>();
		try
		{	
			DateFormat simple=new SimpleDateFormat("yyyyMM");
			String tableName = "mt" + simple.format(new Date());
			
			String	sqlSelect="select USER_ID, MD5, SUBMIT_DATE from "+tableName+" where date(SUBMIT_DATE)=date(now()) group by user_id, md5 order by ID desc ";	
			cnn=DBPool.getConnectionSub();
			stmt=cnn.prepareStatement(sqlSelect);		
			rs=stmt.executeQuery();
			while(rs.next())
			{
				lstUserSend.put(rs.getString("USER_ID")+rs.getString("MD5"), rs.getTimestamp("SUBMIT_DATE"));    
			}			
		}
		catch(Exception ex)
		{
			Utils.logger.error("Error at get free list :"+ex.getMessage());
			Utils.logger.printStackTrace(ex);
		}
		finally
		{
			DBPool.cleanup(rs);
			DBPool.cleanup(cnn,stmt);
		}
		return lstUserSend;
	}
	
	public boolean checkDuplicateMt(MTQueueDTO obj)
	{		
		long compareTime = 10000;
		boolean check = false;
		String key = obj.getUserId() + obj.getMd5();
		Utils.logger.info("checkDuplicateMt @key="+key);
		if(Main.listUserSend != null){
			synchronized (Main.listUserSend) {
				
				Timestamp lastSendMt = null;
				try{			
					lastSendMt = Main.listUserSend.get(key);			
				}
				catch(Exception ex){			
				}		
				
				if(lastSendMt != null){
					compareTime = Utils.compareToDate(Utils.getInstance().getCurrentTime().toString(), lastSendMt.toString());
					
				}
				//Util.logger.info("-->msisdn: " + msisdn +", commandCode: "+commandCode+" compareTime: " + compareTime);
				//900000 = 15*60*1000
				if(compareTime > 900000){
					Main.listUserSend.put(key, Utils.getInstance().getCurrentTime());
					check = true;
				}
				return check;
			}
		}
		else{
			check = true;
			Main.listUserSend.put(key, Utils.getInstance().getCurrentTime());
			return check;
		}			
	}
}

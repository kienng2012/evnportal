package Icom.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import Icom.API.ResourceEntity;
import Icom.DTO.BrandnameDTO;
import Icom.DTO.PartnerDTO;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;


public class BlacklistDAO {
	private BlacklistDAO(){
		
	}
	
	private static BlacklistDAO mInstance;
	private static Object mLock = new Object();
	
	public static BlacklistDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new BlacklistDAO();
			}
			return mInstance;
		}
	}
	
	public Hashtable<String, String> getBlackList(){
		Hashtable<String, String> result = null;
		
		String sql = "Select user_id from customer_blacklist";
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Utils.logger.info("BlacklistDAO::getBlackList:: " + sql);
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				while (rs.next()) {
					if(result == null){
						result = new Hashtable<String, String>();
					}
					
					String msisdn = rs.getString("user_id");
					
					result.put(msisdn, msisdn);
				}
			}
		} catch (Exception e) {
			Utils.logger.printStackTrace(e);
		} finally {
			DBPool.getInstance().cleanup(rs);
			DBPool.getInstance().cleanup(pstm);
			DBPool.getInstance().cleanup(con);
		}
		
		return result;
	}
}

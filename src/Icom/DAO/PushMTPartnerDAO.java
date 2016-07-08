package Icom.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import Icom.DTO.PartnerDTO;
import Icom.DTO.PushMTPartnerDTO;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class PushMTPartnerDAO {
	private PushMTPartnerDAO(){
		
	}
	
	private static PushMTPartnerDAO mInstance;
	private static Object mLock = new Object();
	
	public static PushMTPartnerDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new PushMTPartnerDAO();
			}
			return mInstance;
		}
	}
	
	//Lay danh sach table cho tung nha mang
	//Switch kenh
	
	public Hashtable<String, PushMTPartnerDTO> getTableName(){
		Hashtable<String, PushMTPartnerDTO> result = null;
		
		String sql = "Select operator, partner, mt_queue, status from mt_partner where status = 1";
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Utils.logger.info("PushMTPartnerDAO::getTableName:: " + sql);
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				while (rs.next()) {
					if(result == null){
						result = new Hashtable<String, PushMTPartnerDTO>();
					}
					
					PushMTPartnerDTO partner = new PushMTPartnerDTO();
					
					String operator = rs.getString("operator");
					String partner1 = rs.getString("partner");
					String mt_queue = rs.getString("mt_queue");
					int status = rs.getInt("status");
					
					partner.setOperator(operator);
					partner.setPartner(partner1);
					partner.setMt_queue(mt_queue);
					partner.setStatus(status);
					
					result.put(operator, partner);
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

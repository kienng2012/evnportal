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


public class PartnerDAO {
	private PartnerDAO(){
		
	}
	
	private static PartnerDAO mInstance;
	private static Object mLock = new Object();
	
	public static PartnerDAO getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new PartnerDAO();
			}
			return mInstance;
		}
	}
	
	public Hashtable<String, PartnerDTO> getPartners(){
		Hashtable<String, PartnerDTO> result = null;
		
		String sql = "Select id, username, password, ip, name, active from partner where active = 1";
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Utils.logger.info("PartnerDAO::getPartners:: " + sql);
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				while (rs.next()) {
					if(result == null){
						result = new Hashtable<String, PartnerDTO>();
					}
					
					PartnerDTO partner = new PartnerDTO();
					
					partner.setID(rs.getInt("id"));
					partner.setUsername(rs.getString("username"));
					partner.setPassword(rs.getString("password"));
					partner.setIP(rs.getString("ip"));
					partner.setName(rs.getString("name"));
					partner.setActive(rs.getInt("active"));
					
					result.put(partner.getIP(), partner);
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
	
	public Hashtable<String, BrandnameDTO> getBrandnames(){
		Hashtable<String, BrandnameDTO> result = null;
		
		String condition = "";
		
		for(String key : ResourceEntity.listPartners.keySet()){
			PartnerDTO partner = ResourceEntity.listPartners.get(key);
			condition = condition + "'" + partner.getName() + "',";
		}
		
		condition = condition.substring(0, condition.length() - 1);
		
		String sql = "Select brt_brandname, brt_quota_remain, brt_price_sale, brt_price_purchase, brt_partner_name from brandname_telco where brt_partner_name in (" + condition + ")";
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Utils.logger.info("PartnerDAO::getBrandnames:: " + sql);
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				while (rs.next()) {
					if(result == null){
						result = new Hashtable<String, BrandnameDTO>();
					}
					
					BrandnameDTO brandname = new BrandnameDTO();
					
					brandname.setBrandname(rs.getString("brt_brandname"));
					brandname.setCountSMS(rs.getInt("brt_quota_remain"));
					brandname.setAmountSale(rs.getInt("brt_price_sale"));
					brandname.setAmountPurchase(rs.getInt("brt_price_purchase"));
					brandname.setPartnerName(rs.getString("brt_partner_name"));
//					brandname.setPartnerID(rs.getInt("PARTNER_ID"));
					
//					brandname.setBrandname(rs.getString("BRANDNAME"));
//					brandname.setCountSMS(rs.getInt("COUNT_SMS"));
//					brandname.setAmountSale(rs.getInt("AMOUNT_SALE"));
//					brandname.setAmountPurchase(rs.getInt("AMOUNT_PURCHASE"));
//					brandname.setPartnerName(rs.getString("PARTNER_NAME"));
//					brandname.setPartnerID(rs.getInt("PARTNER_ID"));
					
					result.put(";" + brandname.getPartnerName() + ";" +brandname.getBrandname() + ";", brandname);
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
	
	public int getCountSMSBrandname(String brandname){
		int result = 0;
		
		String sql = "Select COUNT_SMS from brandname where active = 1 and BRANDNAME = '" + brandname + "'";
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Utils.logger.info("PartnerDAO::getCountSMSBrandname:: " + sql);
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				while (rs.next()) {
					result = rs.getInt("COUNT_SMS");
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
	
	public int updateCountSMSBrandname(String brandname, String subSMS){
		int result = 0;
		
		String sql = "Update brandname set COUNT_SMS = COUNT_SMS - " + subSMS + "where active = 1 and BRANDNAME = '" + brandname + "'";
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Utils.logger.info("PartnerDAO::getCountSMSBrandname:: " + sql);
		
		try {
			if(con == null){
				con = DBPool.getInstance().getConnectionSub();
				pstm = con.prepareStatement(sql);
				rs = pstm.executeQuery();
				while (rs.next()) {
					result = rs.getInt("COUNT_SMS");
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

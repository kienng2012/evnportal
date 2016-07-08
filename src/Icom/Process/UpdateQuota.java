package Icom.Process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class UpdateQuota extends Thread{
	
	public UpdateQuota() {
	
	}
	
	@Override
	public void run() {
		// You are going to succeed !
		updateQuota();
	}

	private static void updateQuota() {
		// You are going to succeed !
		while(Main.getData){
			
				String sCase = "";
				String condition = "";
				for(String key : Main.remainQuota.keySet()){
					System.out.println(key.length());
					String telconame = key.split(";")[1];
					String brandname = key.split(";")[2];
					
					AtomicInteger remain = Main.remainQuota.get(key);
					
					sCase = sCase + "when brt_telco_name = '" + telconame + "' and brt_brandname = '" + brandname + "' then " + remain.get() + " ";
					condition = condition + "('" + telconame + "','" + brandname + "'),";
				}
				
				condition = condition.substring(0, condition.length() - 1);
				
				String sql = "Update brandname_telco set brt_quota_remain = (CASE " + sCase + " END) where (brt_telco_name, brt_brandname) in (" + condition + ")" ;
				
//				System.out.println(sql);
				
				Connection con = null;
				PreparedStatement pstm = null;
				
			try {
				if(con == null){
					con = DBPool.getConnectionSub();
					pstm = con.prepareStatement(sql);
					pstm.execute();
				}
			} catch (Exception e) {
				Utils.logger.printStackTrace(e);
			} finally {
				DBPool.cleanup(pstm);
				DBPool.cleanup(con);
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Hashtable<String, AtomicInteger> remain = new Hashtable<String, AtomicInteger>();
		remain.put(";VIETTEL;PCNamDinh;", new AtomicInteger(1000));
		remain.put(";VIETTEL;PCPhuTho;", new AtomicInteger(1000));
		remain.put(";VIETTEL;PC.QNinh;", new AtomicInteger(1000));
		
		updateQuota();
	}
}

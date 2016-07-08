package Icom.Process;

import Icom.Dao.BlacklistDAO;
import Icom.Utils.Utils;

public class ReloadConfig extends Thread{
	
	public ReloadConfig(){}
	public void run(){
		while (Main.getData) {
			try {
				Main.lstBlackList = BlacklistDAO.getInstance().getBlackList();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				Thread.sleep(5*60*1000);
			} catch (InterruptedException e) {
				// TODO: handle exception
				Utils.logger.printStackTrace(e);
			}
		}
	}

}

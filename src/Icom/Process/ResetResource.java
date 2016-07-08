package Icom.Process;

import java.text.SimpleDateFormat;
import java.util.Date;

import Icom.Utils.Constants;
import Icom.Utils.Utils;

public class ResetResource extends Thread{
	
	public ResetResource() {
		Utils.logger.info("Thread Reset Resource started !!!");
	}
	
	public void run() {
		reset();
	}

	private void reset() {
		while(Main.getData){
			
			try{
				String current_date = new SimpleDateFormat("HH:mm").format(new Date());
				String time_reset = Constants.RESET_TIME;
				
				if(time_reset.indexOf(current_date) >= 0){
					Utils.logger.info("Reset Resource start current_date="+current_date+" !!!");
					
					Main.listMsisdn.clear();
					
					Utils.logger.info("Reset Resource success !!!");
				}
			}catch(Exception e){
				Utils.logger.printStackTrace(e);
			}
			
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
}

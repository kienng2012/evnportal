package Icom.API;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import Icom.Utils.Config;
import Icom.Utils.Utils;

public class ResetResource extends Thread{
	
	public ResetResource() {
		Utils.logger.info("Thread Reset Resource started !!!");
	}
	
	public void run() {
		reset();
	}

	private void reset() {
		while(ResourceEntity.isRunning){
			
			try{
				String current_date = new SimpleDateFormat("HH:mm").format(new Date());
				String time_reset = Config.getInstance().getProperty("reset_time", "00:00");
				
				if(time_reset.indexOf(current_date) >= 0){
					Utils.logger.info("Reset Resource start !!!");
					
					ResourceEntity.listMsisdn.clear();
					ResourceEntity.listMsisdnBackup.clear();
					
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
	
	public static void main(String[] args) {
		String s = "trường lớp";

		String s1 = Normalizer.normalize(s, Normalizer.Form.NFD);
		System.out.println(s1);
	}
}

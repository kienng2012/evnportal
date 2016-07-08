package Icom.API;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import Icom.DAO.BlacklistDAO;
import Icom.DAO.MTQueueDAO;
import Icom.DAO.PartnerDAO;
import Icom.DAO.TelcoUtils;
import Icom.DTO.BrandnameDTO;
import Icom.DTO.BulkSMSDTO;
import Icom.DTO.PartnerDTO;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.RequestDTO;
import Icom.Utils.Config;
import Icom.Utils.Constants;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;

public class ResourceEntity {
	public static Hashtable<String, PartnerDTO> listPartners = new Hashtable<String, PartnerDTO>();
	public static Hashtable<String, BrandnameDTO> listBrandnames = new Hashtable<String, BrandnameDTO>();
	
	public static Hashtable<String, String> blacklist = new Hashtable<String, String>();
	
	public static boolean isRunning = true;
	
	public static ConcurrentHashMap<String, ArrayList<RequestDTO>> listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
	public static ConcurrentHashMap<String, ArrayList<RequestDTO>> listMsisdnBackup = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
	
//	public static Hashtable<String, ArrayList<RequestDTO>> listMsisdnHT = new Hashtable<String, ArrayList<RequestDTO>>();
//	public static Hashtable<String, ArrayList<RequestDTO>> listMsisdnBackupHT = new Hashtable<String, ArrayList<RequestDTO>>();
	
	public static void loadConfig(){
		
		Utils.getInstance().loadLogger();
		Config.getInstance().load();
		Utils.getInstance().setLogDirectionPath(Config.getInstance().getProperty("LOG_PATH"));
		
		Constants.loadConfig();
		DBPool.getInstance().ConfigDB();
		
	}
	
	@SuppressWarnings("unchecked")
	public static void initResource(){
		listPartners = PartnerDAO.getInstance().getPartners();
		listBrandnames = PartnerDAO.getInstance().getBrandnames();
		blacklist = BlacklistDAO.getInstance().getBlackList();
		
		if(blacklist == null){
			blacklist = new Hashtable<String, String>();
		}
		
		try{
			listMsisdn = (ConcurrentHashMap<String, ArrayList<RequestDTO>>) Utils.getInstance().loadData(Config.getInstance().getProperty("list_msisdn"));
			if(listMsisdn == null){
				listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
			}
		}catch(Exception e){
			if(listMsisdn == null){
				listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
			}
			
			Utils.logger.printStackTrace(e);
		}
		
		try{
			listMsisdnBackup = (ConcurrentHashMap<String, ArrayList<RequestDTO>>) Utils.getInstance().loadData(Config.getInstance().getProperty("list_msisdn_backup"));
			if(listMsisdnBackup == null){
				listMsisdnBackup = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
			}
		}catch(Exception e){
			if(listMsisdnBackup == null){
				listMsisdnBackup = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
			}
			
			Utils.logger.printStackTrace(e);
		}
		
//		try{
//			listMsisdnHT = (Hashtable<String, ArrayList<RequestDTO>>) Utils.getInstance().loadData(Config.getInstance().getProperty("list_msisdn"));
//			if(listMsisdnHT == null){
//				listMsisdnHT = new Hashtable<String, ArrayList<RequestDTO>>();
//			}
//		}catch(Exception e){
//			if(listMsisdnHT == null){
//				listMsisdnHT = new Hashtable<String, ArrayList<RequestDTO>>();
//			}
//			
//			Utils.logger.printStackTrace(e);
//		}
//		
//		listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>(listMsisdnHT);
//		
//		try{
//			listMsisdnBackupHT = (Hashtable<String, ArrayList<RequestDTO>>) Utils.getInstance().loadData(Config.getInstance().getProperty("list_msisdn_backup"));
//			if(listMsisdnBackupHT == null){
//				listMsisdnBackupHT = new Hashtable<String, ArrayList<RequestDTO>>();
//			}
//		}catch(Exception e){
//			if(listMsisdnBackupHT == null){
//				listMsisdnBackupHT = new Hashtable<String, ArrayList<RequestDTO>>();
//			}
//			
//			Utils.logger.printStackTrace(e);
//		}
//		
//		listMsisdnBackup = new ConcurrentHashMap<String, ArrayList<RequestDTO>>(listMsisdnBackupHT);
		
		ResetResource reset = new ResetResource();
		reset.setPriority(Thread.MAX_PRIORITY);
		reset.start();
		
	}
	
	public static void reloadResource(){
		listPartners = PartnerDAO.getInstance().getPartners();
		listBrandnames = PartnerDAO.getInstance().getBrandnames();
	}
	
	public static void saveResource(){
		Utils.getInstance().saveData(listMsisdn, Config.getInstance().getProperty("list_msisdn"));
		isRunning = false;
	}
	
	public static boolean checkAuthentication(String ip, String username, String password){
		PartnerDTO partner = ResourceEntity.listPartners.get(ip);
		
		if(partner != null){
			if(partner.getUsername().equals(username) && partner.getPassword().equals(password)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean checkAuthentication(String ip){
		PartnerDTO partner = ResourceEntity.listPartners.get(ip);
		
		if(partner == null){
			return false;
		}
		
		return true;
	}
	
	public static boolean checkBrandName(String ip, String brandname){
		try{
			PartnerDTO partner = ResourceEntity.listPartners.get(ip);
			
			if(partner != null){
				Utils.logger.info("checkBrandName: " + partner.getName());
				
				BrandnameDTO brandNameObj = ResourceEntity.listBrandnames.get(";" + partner.getName() + ";" + brandname + ";");
				if(brandNameObj != null){
					if(brandNameObj.getBrandname().equals(brandname)){
						return true;
					}
				}
			}
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return false;
	}
	
	public static boolean checkUsername(String ip, String username){
		PartnerDTO partner = ResourceEntity.listPartners.get(ip);
		if(partner != null){
			if(partner.getUsername().equals(username)){
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		Hashtable<String, String> abc = new Hashtable<String, String>();
		abc.put(";abc;abc;", "abc");
		abc.put(";atc;gbc;", "def");
		abc.put(";aq;sc;", "asd");
		
		ConcurrentHashMap<String, String> acs = new ConcurrentHashMap<String, String>(abc);
		
		for(String key : acs.keySet()){
			System.out.println(acs.get(key));
		}
		
	}
}

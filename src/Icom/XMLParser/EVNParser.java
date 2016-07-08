package Icom.XMLParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import Icom.API.ResourceEntity;
import Icom.DAO.MTQueueDAO;
import Icom.DTO.BulkSMSDTO;
import Icom.DTO.CheckStatusDTO;
import Icom.DTO.PushSMSDTO;
import Icom.DTO.RequestDTO;
import Icom.DTO.PushSMSDTO.MessageDTO;
import Icom.Utils.Utils;

public class EVNParser {
	private EVNParser(){
		
	}
	
	private static EVNParser mInstance;
	private static Object mLock = new Object();
	
	public static EVNParser getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new EVNParser();
			}
			return mInstance;
		}
	}
	
	public PushSMSDTO getPushSMS(String xml){
		try{
			
//			ResourceEntity.listMsisdnBackup = new Hashtable<String, ArrayList<RequestDTO>>(ResourceEntity.listMsisdn);
			
			ResourceEntity.listMsisdnBackup = new ConcurrentHashMap<String, ArrayList<RequestDTO>>(ResourceEntity.listMsisdn);
			
			PushSMSDTO result = null;
			
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        SAXParser saxParser = factory.newSAXParser();
	        PushSMSParser pushHandler = new PushSMSParser();
	        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	        saxParser.parse(is, pushHandler);
	        
	        result = pushHandler.getPushSMS();
	        
			return result;
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return null;
	}
	
	public BulkSMSDTO getBulkSMS(String xml){
		try{
			
//			ResourceEntity.listMsisdnBackup = new Hashtable<String, ArrayList<RequestDTO>>(ResourceEntity.listMsisdn);
			
			ResourceEntity.listMsisdnBackup = new ConcurrentHashMap<String, ArrayList<RequestDTO>>(ResourceEntity.listMsisdn);
			
			BulkSMSDTO result = null;
			
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        SAXParser saxParser = factory.newSAXParser();
	        BulkSMSParser bulkHandler = new BulkSMSParser();
	        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	        saxParser.parse(is, bulkHandler);
	        
	        result = bulkHandler.getBulkSMS();
	        
			return result;
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return null;
	}
	
	public CheckStatusDTO getCheckStatus(String xml){
		try{
			
			CheckStatusDTO result = null;
			
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        SAXParser saxParser = factory.newSAXParser();
	        CheckStatusParser checkStatus = new CheckStatusParser();
	        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	        saxParser.parse(is, checkStatus);
	        
	        result = checkStatus.getCheckStatus();
	        
			return result;
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		
		ResourceEntity.loadConfig();
		String xml = "<RQST><USERNAME>evn</USERNAME><PASSWORD>evn@123</PASSWORD><LOAISP>2</LOAISP><BRANDNAME>PCHaiDuong</BRANDNAME><MSG><MSGID>TTICOM42449657|20164</MSGID><MSISDN>84983256319</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910001812, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 53520 kWh, So tien 97994424 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449658|20164</MSGID><MSISDN>84987130857</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910001609, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 20640 kWh, So tien 40026448 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449659|20164</MSGID><MSISDN>84983256034</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910001575, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 801203 kWh, So tien 1107172047 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449660|20164</MSGID><MSISDN>84904875476</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910001508, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 77280 kWh, So tien 156253856 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449661|20164</MSGID><MSISDN>84913269667</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910001227, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 47800 kWh, So tien 87760090 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449662|20164</MSGID><MSISDN>841263107498</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910000282, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 35 kWh, So tien 74507 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449663|20164</MSGID><MSISDN>84938057314</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910000103, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 71520 kWh, So tien 130161812 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449664|20164</MSGID><MSISDN>84912402895</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000045414, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 6720 kWh, So tien 13683182 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449665|20164</MSGID><MSISDN>84912402895</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000045413, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 15700 kWh, So tien 32953657 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449666|20164</MSGID><MSISDN>84983697089</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000045217, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 14000 kWh, So tien 27974056 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449667|20164</MSGID><MSISDN>841634694781</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000044722, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 26800 kWh, So tien 55123310 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449668|20164</MSGID><MSISDN>84934358138, 0988</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000044452, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 306944 kWh, So tien 572780023 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449669|20164</MSGID><MSISDN>84904429698</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000044223, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 37920 kWh, So tien 68232912 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449670|20164</MSGID><MSISDN>84913256009</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000043914, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 518725 kWh, So tien 858657021 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449671|20164</MSGID><MSISDN>84985320732</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000043803, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 70800 kWh, So tien 130499688 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449672|20164</MSGID><MSISDN>84904875476</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000043569, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 99200 kWh, So tien 192500000 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449673|20164</MSGID><MSISDN>84913245605</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000041735, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 57078 kWh, So tien 112956533 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449674|20164</MSGID><MSISDN>84906105357</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000041734, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 1740 kWh, So tien 4709892 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449675|20164</MSGID><MSISDN>84974583191</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000039253, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 212345 kWh, So tien 334078360 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449676|20164</MSGID><MSISDN>84913291685</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000038063, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 37893 kWh, So tien 70952542 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449677|20164</MSGID><MSISDN>84946176356</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000038062, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 66255 kWh, So tien 129964458 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449678|20164</MSGID><MSISDN>84912531331</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000038061, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 59788 kWh, So tien 117666998 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449679|20164</MSGID><MSISDN>84938172189</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000035377, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 46000 kWh, So tien 83819736 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449680|20164</MSGID><MSISDN>84904429698</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000035297, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 156160 kWh, So tien 280123932 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449681|20164</MSGID><MSISDN>84912341128</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000035294, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 50430 kWh, So tien 92043204 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449682|20164</MSGID><MSISDN>84982936168</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000031082, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 54540 kWh, So tien 102834204 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449683|20164</MSGID><MSISDN>84987130857</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000023236, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 15840 kWh, So tien 30813420 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449684|20164</MSGID><MSISDN>84904529781</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000023229, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 33780 kWh, So tien 69971352 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449685|20164</MSGID><MSISDN>84974007696</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000023028, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 101340 kWh, So tien 216615146 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449686|20164</MSGID><MSISDN>84912546402</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000023011, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 86400 kWh, So tien 186901176 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449687|20164</MSGID><MSISDN>84975451982</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000022972, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 23900 kWh, So tien 46479510 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449688|20164</MSGID><MSISDN>84986081833</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000022971, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 52900 kWh, So tien 112696760 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449689|20164</MSGID><MSISDN>84973263080</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000022955, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 44400 kWh, So tien 89400300 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449690|20164</MSGID><MSISDN>84963463498</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000000044, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 8960 kWh, So tien 16046360 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449691|20164</MSGID><MSISDN>84915065848</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000000041, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 64860 kWh, So tien 122156760 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449692|20164</MSGID><MSISDN>84913913625</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000000039, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 88320 kWh, So tien 165030272 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449693|20164</MSGID><MSISDN>84913269667</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910001227, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 27500 kWh, So tien 3027724 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449694|20164</MSGID><MSISDN>841263107498</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11910000282, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 21 kWh, So tien 3465 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449695|20164</MSGID><MSISDN>84912402895</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000045414, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 4480 kWh, So tien 976979 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449696|20164</MSGID><MSISDN>84912402895</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000045413, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 10200 kWh, So tien 2352891 dong. Xin cam on.</CONTENT></MSG><MSG><MSGID>TTICOM42449697|20164</MSGID><MSISDN>84946176356</MSISDN><CONTENT>Cty DL Hai Duong xin TB: Ma khach hang PM11000038062, ky ghi chi so ngay 01, thang 4, nam 2016, tieu thu 34976 kWh, So tien 2950193 dong. Xin cam on.</CONTENT></MSG></RQST>";
		String xml1 = xml.replace("&", "[!@#$%]");
		PushSMSDTO push = EVNParser.getInstance().getPushSMS(xml1);
		
		for(MessageDTO msg : push.getListMessage()){
//			System.out.println(msg.getContent());
			if(msg.getContent().contains("[!@#$%]")){
				System.out.println(msg.getContent());
				System.out.println(msg.getContent().replace("[!@#$%]", "&"));
			}
			
			if(msg.getMsisdn().length() > 15){
				System.out.println(msg.getMsisdn());
			}
		}
//		String xml2 = xml1.replace("&amp;", "&");
//		if(xml.equals(xml2)){
//			System.out.println("true");
//		}else{
//			System.out.println("false");
//		}
	}
}

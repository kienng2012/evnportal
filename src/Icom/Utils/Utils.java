package Icom.Utils;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.httpclient.methods.GetMethod;

public class Utils {
	
	private Utils(){
		
	}
	
	private static Utils mInstance;
	private static Object mLock = new Object();
	
	public static Utils getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new Utils();
			}
			return mInstance;
		}
	}
	
	public static Logger logger = null;
	
	public void loadLogger()
	{
		logger = new Logger();
		try
		{
			logger.setLogWriter("/home/oracle/tomcat8082/webapps/BRANDNAME_API/log/${yyyy-MM-dd}process.log");
//			logger.setLogWriter("/home/oracle/VietnamMobile/Play365/tomcat/webapps/BRANDNAME_API/log/${yyyy-MM-dd}process.log");
			
		}
		catch (IOException ex)
		{
		}
		logger.setLogLevel(Constants.LOGLEVEL);
		logger.info("Start :" );
	}

	public void setLogDirectionPath(String pathDirection)
	{
		if(logger == null){
			logger = new Logger();
		}
		
		try
		{
			logger.setLogWriter(pathDirection + "/${yyyy-MM-dd}process.log");
			
		}
		catch (IOException ex)
		{
		}
		logger.setLogLevel(Constants.LOGLEVEL);
		logger.info("Start :" );
	}
	
	
	public Utils(String logfile, String loglevel) {
		logger = new Logger();
		try {
			logger.setLogWriter(logfile);
		} catch (IOException ex) {
		}
		logger.setLogLevel(loglevel);

	}
	
	public boolean checkValidDate(String format, String date){
		boolean result = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			sdf.parse(date);
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return result;
	}
	
	public String formatDate(String format, Timestamp time){
		
		try{
			return new Timestamp(new Date().getTime()) + "";
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return null;
	}
	
	
	public Timestamp getTimestamp(String format, String sDate){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(sDate);
			return new Timestamp(date.getTime());
		}catch(Exception e){
			Utils.logger.printStackTrace(e);
		}
		
		return null;
	}
	
	public String getValue(String xml, String tagName) {
		String openTag = "<" + tagName + ">";
		String closeTag = "</" + tagName + ">";

		int f = xml.indexOf(openTag) + openTag.length();
		int l = xml.indexOf(closeTag);

		return (f > l) ? "" : xml.substring(f, l);
	}

	public String formatDate()
	{
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format1.format(new Date());
	}
	
	public String formatDate(Timestamp date)
	{
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format1.format(date);
	}
	
	public Timestamp getCurrentTime()
	 {
		Date date=new Date();
		Timestamp time=new Timestamp(date.getTime());
		return time;
	 }
	
	public Timestamp getNextDateTime(int day)
	 {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Now use today date.
		c.add(Calendar.DATE, day); // Adding day days
		Timestamp nextChargeDate = new Timestamp(c.getTimeInMillis());
		
		SimpleDateFormat ft= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Utils.logger.info("CurrentDate="+ft.format(c.getTime())+", Date Add: " + day + ", nextChargeDate: " + ft.format(nextChargeDate));
		
		return nextChargeDate;
	 }
	
	public String getBeforeDate(int i) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) -i);
		return new java.text.SimpleDateFormat("dd/MM/yyyy").format(now.getTime());
	}
	
	
	
	public String getCurrYearMonth(){
		DateFormat formatter = new SimpleDateFormat("yyyyMM");
		java.util.Date today = new java.util.Date();			
		String currHour = formatter.format(today);
		return currHour;
	}
	
	public String formatString(String input)
	{
		String info=input;
		info = info.replace("(","");
		info = info.replace(")","");
		info = info.replace("<", "");
		info = info.replace(">", "");
		info = info.replace(" ","");
		return info;
	}
	
	public String getStringFromTimestamp(Timestamp lastUpdate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(lastUpdate!=null)
		return sdf.format(lastUpdate);
		else return null;
	}
	
	public String getString_Timestamp(Timestamp lastUpdate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if(lastUpdate!=null)
		return sdf.format(lastUpdate);
		else return null;
	}

	public boolean compareDateGreatToday(Timestamp datetime1) {
		Date date = new Date();
		Timestamp curdate = new Timestamp(date.getTime());
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate1 = format.format(datetime1);
		String strCurr = format.format(curdate);
		
		try {
			Date date1 = format.parse(strDate1);
			Date curr = format.parse(strCurr);
			if (date1.compareTo(curr) > 0){
				return true;
			}				
		} catch (ParseException e) {			
			e.printStackTrace();
			return false;
		}

		return false;
	}
	
	public String getDataFromAPI(String url){
		long requestStart = System.currentTimeMillis();
		try{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		con.setRequestMethod("GET");
		con.setReadTimeout(1000);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		return response.toString();
		
		}catch(SocketTimeoutException e){
			Utils.logger.error("Error @APIGetter: getDataFromAPI : " + e.getMessage() + ", url=" + url);
		}
		catch(Exception e){
			Utils.logger.error("Error @APIGetter: getDataFromAPI : " + e.getMessage() + ", url=" + url);
		}
		
		long endResponse = System.currentTimeMillis();
		long time = endResponse - requestStart;
		
		Utils.logger.info("Thoi gian nhan response: " + time);
		return null;
	}
	
	public void saveData(Object obj, String filename){
    	FileOutputStream fos = null;
    	ObjectOutputStream oos = null;
    	File file = null;
    	try{
    		file = new File(filename);
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		fos = new FileOutputStream(file, false);
    		oos = new ObjectOutputStream(fos);
    		oos.writeObject(obj);
    		oos.flush();
    	}catch(Exception e){
    		Utils.logger.printStackTrace(e);
    	}finally{
    		try{
    			if(fos != null){
    				fos.close();
    			}
    			
    			if(oos != null){
    				oos.close();
    			}
    		}catch(Exception e){
    			Utils.logger.printStackTrace(e);
    		}
    	}
    }
    
	public Object loadData(String filename){
    	Object data = null;
    	
    	FileInputStream fos = null;
    	ObjectInputStream oos = null;
    	File file = null;
    	try{
    		file = new File(filename);
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		fos = new FileInputStream(file);
    		oos = new ObjectInputStream(fos);
    		data = oos.readObject();
    	}catch(EOFException e){
    		
    	} catch (IOException e) {
    		Utils.logger.printStackTrace(e);
		} catch (ClassNotFoundException e) {
			Utils.logger.printStackTrace(e);
		}finally{
    		try{
    			if(fos != null){
    				fos.close();
    			}
    			
    			if(oos != null){
    				oos.close();
    			}
    		}catch(Exception e){
    			Utils.logger.printStackTrace(e);
    		}
    	}
    	
    	return data;
    }	
	
	
	public byte[] getBytes(Object obj) throws java.io.IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}

	// Fast convert a byte array to a hex string
	// with possible leading zero.
	public String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			// look up high nibble char
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			// look up low nibble char
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	// table to convert a nibble to a hex char.
	static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Convert a hex string to a byte array. Permits upper or lower case hex.
	 * 
	 * @param s
	 *            String must have even number of characters. and be formed only
	 *            of digits 0-9 A-F or a-f. No spaces, minus or plus signs.
	 * @return corresponding byte array.
	 */
	public byte[] fromHexString(String s) {
		int stringLength = s.length();
		if ((stringLength & 0x1) != 0) {
			throw new IllegalArgumentException(
					"fromHexString requires an even number of hex characters");
		}
		byte[] b = new byte[stringLength / 2];

		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(s.charAt(i));
			int low = charToNibble(s.charAt(i + 1));
			b[j] = (byte) ((high << 4) | low);
		}
		return b;
	}

	/**
	 * convert a single char to corresponding nibble.
	 * 
	 * @param c
	 *            char to convert. must be 0-9 a-f A-F, no spaces, plus or minus
	 *            signs.
	 * 
	 * @return corresponding integer
	 */
	private int charToNibble(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if ('a' <= c && c <= 'f') {
			return c - 'a' + 0xa;
		} else if ('A' <= c && c <= 'F') {
			return c - 'A' + 0xa;
		} else {
			throw new IllegalArgumentException("Invalid hex character: " + c);
		}
	}

	public double roundEx(double value, int decimalPlace) {
		BigDecimal bigValue = new BigDecimal(value);
		bigValue = bigValue.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bigValue.doubleValue();
	}
	
	public String getMD5(String content) {
		  try{
			  //content = content.toLowerCase();
			  byte[] fis = content.getBytes();
			  String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
			  
			  return md5;
		  }catch(Exception e){
			  Utils.logger.printStackTrace(e);
		  }
		 
				
		return null;
	}
	
	public static String getValueTagStart(String xml, String tagName) {
		  String openTag = "<" + tagName + "";
		  String closeTag = "</" + tagName + ">";
		  
		  int f = xml.indexOf(openTag) + xml.substring(xml.indexOf("<"+tagName)).indexOf(">")+1;
		  int l = xml.indexOf(closeTag);

		  return (f > l) ? "" : xml.substring(f, l);
	}
	
	public static void main(String[] args) {
//		String md5 = Utils.getInstance().getMD5("DLCX Tran trong thong bao: Tam ngung cap dien quy khach hang. Du kien tu 08h00 den 13h00 ngay 17/3/2016 de cai tao va nang cap luoi dien!");
//		String md51 = Utils.getInstance().getMD5("DLCX Tran trong thong bao: Tam ngung cap dien quy khach hang. Du kien tu 08h00 den 13h00 ngay 17/3/2016 de cai tao va nang cap luoi dien!");
//		System.out.println(md5);
//		System.out.println(md51);
	}
}

package Icom.Utils;

public class Constants {

	
	
	public static String LOGLEVEL = "info,warn,error,crisis";
	
	public static String OPERATOR;
	public static String SERVICE_ID;
	public static int MAX_NPR = 1000;
	public static String MT_TABLE="mt_queue";
	public static String REQUEST_TABLE="evn_request";
	public static String REQUEST_FAIL_TABLE="evn_request_fail";
	public static String IP="evn_request_fail";
	public static String BRANDNAME="evn_request_fail";
	public static String USERNAME="evn_request_fail";
	public static String PASSWORD="evn_request_fail";
	public static String EVN_RESPONSE_TABLE = "evn_response";
	public static String MTLOG_TABLE_FORMAT = "yyyyMM";
	public static String RESPONSE_TABLE_FORMAT = "yyyyMM";
	public static void loadConfig() {
		try {

			Utils.logger.info("Load cac thong so thread");
			
			Constants.MAX_NPR = Integer.parseInt(Config.getInstance().getProperty("MAX_NPR", MAX_NPR + ""));
			Constants.OPERATOR = Config.getInstance().getProperty("OPERATOR",	Constants.OPERATOR);
			Constants.SERVICE_ID = Config.getInstance().getProperty("SERVICE_ID",Constants.SERVICE_ID);
			Constants.MT_TABLE = Config.getInstance().getProperty("MT_TABLE",Constants.MT_TABLE);
			Constants.REQUEST_TABLE = Config.getInstance().getProperty("REQUEST_TABLE",Constants.REQUEST_TABLE);
			Constants.REQUEST_FAIL_TABLE = Config.getInstance().getProperty("REQUEST_FAIL_TABLE",Constants.REQUEST_FAIL_TABLE);
			Constants.IP = Config.getInstance().getProperty("IP",Constants.IP);
			Constants.BRANDNAME = Config.getInstance().getProperty("BRANDNAME",Constants.BRANDNAME);
			Constants.USERNAME = Config.getInstance().getProperty("USERNAME",Constants.USERNAME);
			Constants.PASSWORD = Config.getInstance().getProperty("PASSWORD",Constants.PASSWORD);
			Constants.EVN_RESPONSE_TABLE = Config.getInstance().getProperty("EVN_RESPONSE_TABLE",Constants.EVN_RESPONSE_TABLE);
			Constants.MTLOG_TABLE_FORMAT = Config.getInstance().getProperty("mtlog_table_format",Constants.MTLOG_TABLE_FORMAT);
			Constants.RESPONSE_TABLE_FORMAT = Config.getInstance().getProperty("response_table_format",Constants.RESPONSE_TABLE_FORMAT);
			
			Utils.logger.info("Load config success");
			
		} catch (Exception ex) {
			Utils.logger.error("Error at load constant " + ex.getMessage());
			Utils.logger.printStackTrace(ex);
		}

	}

}

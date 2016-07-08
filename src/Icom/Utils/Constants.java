package Icom.Utils;

public class Constants {
	public static String LOGLEVEL = "info,warn,error,crisis";
	public static String MOBILE_OPERATOR = "GPC";
	// public static String COMMAND_CODE = "A";
	public static int CONTENT_TYPE = 0;
	public static int MESSAGE_TYPE = 1;
	public static int CHANNEL_TYPE = 15;
	public static int AMOUNT = 2000;
	public static int DAYFREE = 0;
	public static String IPACCESS;
	public static int OPTIONCHECKPASSWORD = 0;
	public static String SERVICE_ID = "8x51";

	public static String EVN_TABLE_RESPONSE = "response_webservice";
	public static String TABLE_MO_QUEUE = "mo_queue";
	public static String TABLE_MT_TABLE = "mt_queue";
	public static String TABLE_MT_TABLE_GW = "mt_queue";
	public static String TABLE_MT_TABLE_GW_2 = "mt_queue1";
	public static String TABLE_MT_WAITTING = "mt_queue_watting";
	public static String OPTION_CHECK_IP = "0";

	public static int NUM_THREAD_LOADMT = 2;
	public static int NUM_THREAD_LOADMT_GW = 2;
	public static int NUM_THREAD_LOG = 2;
	public static int NUM_THREAD_PUSHMT = 2;
	public static int NUM_THREAD_PUSHMT_GW = 2;
	public static int TIME_SLEEP_PUSH_MTQUEUE = 1000;

	public static String MT_CHARGING = "1";
	public static String MT_NOCHARGE = "0";
	public static String MT_PUSH = "3";
	public static int PACKAGE_SERVICE = 1;

	public static String POOLNAME_LOG = "log";
	public static String POOLNAME_SUB = "sub";
	public static String POOLNAME_GATEWAY = "gateway";

	public static String WS_SEPARATION = "|";
	public static String SERVICE_NOT_REGISTER = "2";
	public static String SERVICE_REGISTER = "1";
	public static String ERROR = "3";
	public static int CHARGETYPE = 0;
	// public static String VMG_URL_BRANDNAME="http://brandsms.vn:8329/BrandSMSAPI.asmx"; //OLD API
	public static String VMG_URL_BRANDNAME = "http://brandsms.vn:8018/VMGAPI.asmx"; // NEW	API
	public static String VMS_URL_BRANDNAME = "http://smsbrandname.mobifone.com.vn:8080/PartnerAPI.asmx?wsdl";
	public static String ACCOUNT = "icom";
	public static String PASSWORD = "vmg123456";

	public static String VMG_ACCOUNT_VIETTEL = "icomviettel";
	public static String VMG_PASSWORD_VIETTEL = "vmg123456";

	public static String EVN_ACCOUNT_WS;
	public static String EVN_PASSWORD_WS;
	public static String EVN_URL_WS;
	public static int EVN_LIMIT = 1000;
	public static int EVN_NUM_THREAD_LOAD_RESPONSE = 2;
	public static int ITERATION_COUNT_TOGW = 25;
	public static String BRANDNAME = "VMGtest";

	public static String RESET_TIME = "00:00";
	// public static Hashtable<String, GamePackage> gamePackage = new
	// Hashtable<String, GamePackage>();

	public static void LoadConstants() {

		IPACCESS = Config.getInstance().getProperty("IPACCESS");
		MOBILE_OPERATOR = Config.getInstance().getProperty("MOBILE_OPERATOR");
		SERVICE_ID = Config.getInstance().getProperty("SERVICE_ID");
		CHANNEL_TYPE = Integer.parseInt(Config.getInstance().getProperty("CHANNEL_TYPE"));
		OPTION_CHECK_IP = Config.getInstance().getProperty("OPTION_CHECK_IP");

		AMOUNT = Integer.parseInt(Config.getInstance().getProperty("AMOUNT"));

		VMG_URL_BRANDNAME = Config.getInstance().getProperty("VMG_URL_BRANDNAME");
		VMS_URL_BRANDNAME = Config.getInstance().getProperty("VMS_URL_BRANDNAME");
		ACCOUNT = Config.getInstance().getProperty("ACCOUNT");
		PASSWORD = Config.getInstance().getProperty("PASSWORD");
		TABLE_MT_TABLE = Config.getInstance().getProperty("TABLE_MT_TABLE");
		MOBILE_OPERATOR = Config.getInstance().getProperty("MOBILE_OPERATOR");
		NUM_THREAD_LOADMT = Config.getInstance().getIntProperty("NUM_THREAD_LOADMT");
		NUM_THREAD_LOG = Config.getInstance().getIntProperty("NUM_THREAD_LOG");
		NUM_THREAD_PUSHMT = Config.getInstance().getIntProperty("NUM_THREAD_PUSHMT");

		VMG_ACCOUNT_VIETTEL = Config.getInstance().getProperty("VMG_ACCOUNT_VIETTEL");
		VMG_PASSWORD_VIETTEL = Config.getInstance().getProperty("VMG_PASSWORD_VIETTEL");
		NUM_THREAD_LOADMT_GW = Config.getInstance().getIntProperty("NUM_THREAD_LOADMT_GW");
		NUM_THREAD_PUSHMT_GW = Config.getInstance().getIntProperty("NUM_THREAD_PUSHMT_GW");
		TABLE_MT_TABLE_GW = Config.getInstance().getProperty("TABLE_MT_TABLE_GW");
		TABLE_MT_TABLE_GW_2 = Config.getInstance().getProperty("TABLE_MT_TABLE_GW_2");
		TABLE_MT_WAITTING = Config.getInstance().getProperty("TABLE_MT_WAITTING");
		TIME_SLEEP_PUSH_MTQUEUE = Config.getInstance().getIntProperty("TIME_SLEEP_PUSH_MTQUEUE");

		EVN_ACCOUNT_WS = Config.getInstance().getProperty("EVN_ACCOUNT_WS");
		EVN_PASSWORD_WS = Config.getInstance().getProperty("EVN_PASSWORD_WS");
		EVN_URL_WS = Config.getInstance().getProperty("EVN_URL_WS");
		EVN_LIMIT = Integer.parseInt(Config.getInstance().getProperty("EVN_LIMIT"));
		EVN_TABLE_RESPONSE = Config.getInstance().getProperty("EVN_TABLE_RESPONSE");
		EVN_NUM_THREAD_LOAD_RESPONSE = Integer
				.parseInt(Config.getInstance().getProperty("EVN_NUM_THREAD_LOAD_RESPONSE"));
		ITERATION_COUNT_TOGW = Config.getInstance().getIntProperty("ITERATION_COUNT_TOGW");
		RESET_TIME = Config.getInstance().getProperty("reset_time", "00:00");
		Utils.logger.info("Load config success.");
	}
}
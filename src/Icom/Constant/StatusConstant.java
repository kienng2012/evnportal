package Icom.Constant;

public class StatusConstant {
	/**
	 * Sai format
	 */
	public static final int RES_ERROR_FORMAT = 1;
	public static final String RES_ERROR_FORMAT_DESC = "Sai format";
	/**
	 * Vuot nguong so tin nhan trong mot request
	 */
	public static final int RES_ERROR_MAXNPR = 4;
	public static final String RES_ERROR_MAXNPR_DESC = "Vuot nguong so tin nhan trong mot request";
	/**
	 * Loi ket noi co so du lieu
	 */
	public static final int RES_ERROR_DATABASE = 99;
	public static final String RES_ERROR_DATABASE_DESC = "Loi ket noi co so du lieu";
	
	public static final int RES_STATUS_WAITING = 0;
	public static final int RES_STATUS_SENDED_TO_SMSC = 1;
	public static final int RES_STATUS_SENDING = 2 ;
	public static final int RES_STATUS_FAILSED_SENDING = 3;
	public static final int RES_STATUS_TIME_OUT = 4;
	public static final int RES_STATUS_NOT_AVAILIBLE_MESSAGE_ID  = 5;
	public static final int RES_STATUS_FAILED_FORMAT = 6;
	
	public static final int RES_STATUS_24HOUR = 7;
	public static final int RES_STATUS_15MINUTE = 8;
	
	public static final int BLACKLIST = 9;
	
}

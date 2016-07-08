package Icom.Constant;

public class PushConstant {
	/**
	 * Loai tin nhan:
		1: tin CSKH gui tu dau so
		2: tin CSKH gui tu Brandname
	 */
	public static final String LOAISP = ";1;2;";
	/**
	 * Request duoc tiep nhan thanh cong,
	 * ket qua chi tiet cua moi tin nhan (so dien thoai, ket qua thuc hien) duoc dat trong mot khoi MSG
	 */
	public static final int RES_OK = 0;
	public static final String RES_OK_DESC = "OK";
	/**
	 * Sai format
	 */
	public static final int RES_ERROR_FORMAT = 1;
	public static final String RES_ERROR_FORMAT_DESC = "Sai format";
	/**
	 * Loai tin nhan khong dung
	 */
	public static final int RES_ERROR_LOAISP = 2;
	public static final String RES_ERROR_LOAISP_DESC = "Loai tin nhan khong dung";
	/**
	 * Sai brandname hoac brandname chua duoc kich hoat
	 */
	public static final int RES_ERROR_BRANDNAME = 3;
	public static final String RES_ERROR_BRANDNAME_DESC = "Sai brandname hoac brandname chua duoc kich hoat";
	/**
	 * Vuot nguong so tin nhan trong mot request
	 */
	public static final int RES_ERROR_MAXNPR = 4;
	public static final String RES_ERROR_MAXNPR_DESC = "Vuot nguong so tin nhan trong mot request";
	/**
	 * Loi xac thuc (Username hoac password khong dung …)
	 */
	public static final int RES_ERROR_AUTH = 6;
	public static final String RES_ERROR_AUTH_DESC = "Loi xac thuc (Username hoac password khong dung …)";
	/**
	 * Loi ket noi co so du lieu
	 */
	public static final int RES_ERROR_DATABASE = 99;
	public static final String RES_ERROR_DATABASE_DESC = "Loi ket noi co so du lieu";
	
}

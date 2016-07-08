package Icom.DAO;

public class TelcoUtils {
	public static final String VIETTEL = "VIETTEL";
	public static final String VINAPHONE = "GPC";
	public static final String MOBIFONE = "VMS";
	public static final String GMOBILE = "GMOBILE";
	public static final String VNMOBILE = "VNM";
	
	public static final int AMOUNT_VIETTEL = 100;
	public static final int AMOUNT_VINA = 100;
	public static final int AMOUNT_MOBI = 100;
	public static final int AMOUNT_GMOBILE = 100;
	public static final int AMOUNT_VNM = 100;
	
	public static int getAmount(String telco){
		int amount = 0;
		if(VIETTEL.equals(telco)){
			amount = AMOUNT_VIETTEL;
		} else if(VINAPHONE.equals(telco)){
			amount = AMOUNT_VINA;
		} else if(MOBIFONE.equals(telco)){
			amount = AMOUNT_MOBI;
		} else if(GMOBILE.equals(telco)){
			amount = AMOUNT_GMOBILE;
		} else if(VNMOBILE.equals(telco)){
			amount = AMOUNT_VNM;
		}
		
		return amount;
	}
	
	public static String getTelco(String phoneNum){
		if(phoneNum.startsWith("0")){
			phoneNum = "84" + phoneNum.substring(1);
		}
		
        if((phoneNum.startsWith("8491") && phoneNum.length() == 11)
        	|| (phoneNum.startsWith("8488") && phoneNum.length() == 11)
        	|| (phoneNum.startsWith("8494") && phoneNum.length() == 11)
            || (phoneNum.startsWith("84123") && phoneNum.length() == 12)
            || (phoneNum.startsWith("84124") && phoneNum.length() == 12)
            || (phoneNum.startsWith("84125") && phoneNum.length() == 12)
            || (phoneNum.startsWith("84127") && phoneNum.length() == 12)
            || (phoneNum.startsWith("84129") && phoneNum.length() == 12)
            ) {
            	return VINAPHONE;
        }
	   else if((phoneNum.startsWith("8490") && phoneNum.length() == 11)
			    || (phoneNum.startsWith("8489") && phoneNum.length() == 11)
			    || (phoneNum.startsWith("8493") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("84120") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84121") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84122") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84126") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84128") && phoneNum.length() == 12)
	            ) {
		   		return MOBIFONE;
	        }
	   else if((phoneNum.startsWith("8496") && phoneNum.length() == 11)
			    || (phoneNum.startsWith("8486") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("8497") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("8498") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("84162") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84163") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84164") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84165") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84166") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84167") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84168") && phoneNum.length() == 12)
	            || (phoneNum.startsWith("84169") && phoneNum.length() == 12)
	            ) {
	            return VIETTEL;
	        }
	    else if((phoneNum.startsWith("84993") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("84994") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("84995") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("84996") && phoneNum.length() == 11)
	            || (phoneNum.startsWith("84199") && phoneNum.length() == 12)                    
	            ) {
	            return GMOBILE;
	        }
	    else if((phoneNum.startsWith("8492") && phoneNum.length() == 11)                    
	            || (phoneNum.startsWith("84188") && phoneNum.length() == 12)                    
	            ) {
	            return VNMOBILE;
	        }
	        
	    return null;
	}
	
	/**
	 Do dai khong vuot qua 480 ky tu doi voi huong Viettel va khong vuot qua 918 ky tu doi voi cac huong mang khac.
	Tieng Viet khong dau, khong chua ky tu dac biet.
	do dai tin <= 160 ky tu la 1 ban tin, 160< do dai tin <= 306 sang ban tin thu 2, 306< do dai tin <=459 tinh ban tin thu 3.
	 */
	
	public static int countSMS(String content, String telco){
		int count = -1;
		int length = content.length();
		
		if(telco.equalsIgnoreCase(TelcoUtils.VIETTEL)){
			if(length > 480){
				return count;
			}
			
		}else{
			if(length > 918){
				return count;
			}
		}
		
		if(length <= 160){
			count = 1;
		}else if(length <= 306){
			count = 2;
		}else if(length <= 459){
			count = 3;
		}else if(length <= 612){
			count = 4;
		}else if(length <= 765){
			count = 5;
		}else if(length <= 918){
			count = 6;
		}else if(length <= 1071){
			count = 7;
		}
		
		return count;
	}
	
	public static void main(String[] args) {
		System.out.println(TelcoUtils.getTelco("841627401920"));
	}
}

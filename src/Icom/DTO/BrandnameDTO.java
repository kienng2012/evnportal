package Icom.DTO;

public class BrandnameDTO {
	private int id;
	private String name;
	private String brandname;
	private int countSMS;
	private int amount_sale;
	private int amount_purchase;
	private String partner_name;
	private int partner_id;
	
	public BrandnameDTO() {
		
		super();
	}
	public BrandnameDTO(int id, String name, String brandname, int countSMS,
			int amount_sale, int amount_purchase, String partner_name,
			int partner_id) {
		super();
		this.id = id;
		this.name = name;
		this.brandname = brandname;
		this.countSMS = countSMS;
		this.amount_sale = amount_sale;
		this.amount_purchase = amount_purchase;
		this.partner_name = partner_name;
		this.partner_id = partner_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrandname() {
		return brandname;
	}
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	public int getCountSMS() {
		return countSMS;
	}
	public void setCountSMS(int countSMS) {
		this.countSMS = countSMS;
	}
	public int getAmountSale() {
		return amount_sale;
	}
	public void setAmountSale(int amount_sale) {
		this.amount_sale = amount_sale;
	}
	public int getAmountPurchase() {
		return amount_purchase;
	}
	public void setAmountPurchase(int amount_purchase) {
		this.amount_purchase = amount_purchase;
	}
	public String getPartnerName() {
		return partner_name;
	}
	public void setPartnerName(String partner_name) {
		this.partner_name = partner_name;
	}
	public int getPartnerID() {
		return partner_id;
	}
	public void setPartnerID(int partner_id) {
		this.partner_id = partner_id;
	}
	
	
}

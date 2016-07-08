package Icom.DTO;

public class PushMTPartnerDTO {
	private String operator;
	private String partner;
	private String mt_queue;
	private int status;
	public PushMTPartnerDTO() {
		
		super();
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getMt_queue() {
		return mt_queue;
	}
	public void setMt_queue(String mt_queue) {
		this.mt_queue = mt_queue;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public PushMTPartnerDTO(String operator, String partner, String mt_queue,
			int status) {
		super();
		this.operator = operator;
		this.partner = partner;
		this.mt_queue = mt_queue;
		this.status = status;
	}
	
	
}

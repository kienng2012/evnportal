package Icom.DTO;

public class RequestFailDTO {
	private int id;
	private String IP;
	private String request;
	private String username;
	private String password;
	private int request_type;
	private int error;
	private String error_desc;
	private String brandname;
	
	public RequestFailDTO() {
		this.IP = "";
		this.request = "";
		this.username = "";
		this.password = "";
		this.request_type = 0;
		this.error = 0;
		this.error_desc = "";
		this.brandname = "";
	}

	public RequestFailDTO(int id, String iP, String request, String username,
			String password, int request_type, int error) {
		super();
		this.id = id;
		this.IP = iP;
		this.request = request;
		this.username = username;
		this.password = password;
		this.request_type = request_type;
		this.error = error;
	}

	public String getBrandname() {
		return brandname;
	}
	
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	
	public String getErrorDesc() {
		return error_desc;
	}
	
	public void setErrorDesc(String error_desc) {
		this.error_desc = error_desc;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRequestType() {
		return request_type;
	}

	public void setRequestType(int request_type) {
		this.request_type = request_type;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}
	
	
}

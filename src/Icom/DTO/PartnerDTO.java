package Icom.DTO;

public class PartnerDTO {
	private int id;
	private String username;
	private String password;
	private String ip;
	private String name;
	private int active;
	
	public PartnerDTO() {
	
	}

	public PartnerDTO(int id, String username, String password, String ip,
			String name, int active) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.ip = ip;
		this.name = name;
		this.active = active;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
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

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	
	
	
}

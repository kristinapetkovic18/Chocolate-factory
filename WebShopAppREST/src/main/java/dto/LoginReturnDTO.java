
package dto;

import enums.Role;

public class LoginReturnDTO {

	private String username;
	private String id;

	private int role;
	private boolean success;
	
	public LoginReturnDTO(String username, String id, int role, boolean success) {
		super();
		this.username = username;
		this.id = id;
		this.role = role;
		this.success = success;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public LoginReturnDTO(String username, int role, boolean success) {
		super();
		this.username = username;
		this.role = role;
		this.success = success;
	}

	public LoginReturnDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
}

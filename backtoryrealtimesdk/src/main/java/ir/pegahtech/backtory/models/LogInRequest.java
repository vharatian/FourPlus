package ir.pegahtech.backtory.models;


public class LogInRequest {

	private String username;
	private String password;
	
	
	public String getUsername() {
		return username;
	}
	public LogInRequest setUsername(String username) {
		this.username = username;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public LogInRequest setPassword(String password) {
		this.password = password;
		return this;
	}

}

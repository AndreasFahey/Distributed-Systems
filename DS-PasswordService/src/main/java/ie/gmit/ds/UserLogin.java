package ie.gmit.ds;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name= "users")
public class UserLogin {
	
	// XML ain't working when tested in Postman
	
	// Variables
	private int userId;
	private String password;
	
	
	public UserLogin(int userId, String password) {
		this.userId = userId;
		this.password = password;
	}
	
	public UserLogin() {
		super();
	}

	// Getters & Setters for Login XML & JSON
	@XmlElement
	@JsonProperty
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@XmlElement
	@JsonProperty
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}// User Login

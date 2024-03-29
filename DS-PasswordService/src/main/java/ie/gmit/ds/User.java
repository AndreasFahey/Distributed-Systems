package ie.gmit.ds;

import java.util.Arrays;
import ie.gmit.ds.utilities.Passwords;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name= "users")
public class User {
	
	// XML ain't working when tested in Postman
	
	// Variables
	private int userId;
	private String userName;
	private String email;
	private String password;
	private String hashedPassword;
	private String salt;
	
	// Byte Array
	byte[] saltByte = Passwords.getNextSalt();
	
	// Default Constructor
	public User() {
		
	}
	
	// User Details Output
	public User(int userId, String userName, String email, String hashedPassword, String salt) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}
	
	// User Input Fields
	public User(int userId, String userName, String email, String password) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	// User Login Fields
	public User(int userId, String password) {
		this.userId = userId;
		this.password = password;
	}
	
	// Getters & Setters for XML and JSON

	@XmlElement
	@JsonProperty
	public int getUserId() {
		return userId;
	}
	@XmlElement
	@JsonProperty
	public String getUserName() {
		return userName;
	}
	@XmlElement
	@JsonProperty
	public String getEmail() {
		return email;
	}
	@XmlElement
	@JsonProperty
	public String getHashedPassword() {
		return hashedPassword;
	}
	@XmlElement
	@JsonProperty
	public String getSalt() {
		return salt;
	}
	@XmlElement
	@JsonProperty
	public String getPassword() {
		return password;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setSaltByte(byte[] saltByte) {
		this.saltByte = saltByte;
	}

	// Return value
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", hashedPassword=" + hashedPassword + ", salt=" + salt + ", saltByte=" + Arrays.toString(saltByte)
				+ "]";
	}

}// User

package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "users")
public class HashedAccount {
	
	// XML ain't working when tested in Postman

	// Variables
	private int userId;
	private String username;
	private String email;
	private String hashedPassword;
	private String salt;
	
	public HashedAccount() {
		super();
	}
	
	public HashedAccount(int userId, String username, String email, String hashedPassword, String salt) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}
	
	// Getters & Setters

	@JsonProperty
	@XmlElement
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@JsonProperty
	@XmlElement
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty
	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty
	@XmlElement
	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	@JsonProperty
	@XmlElement
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}// Hashed Account

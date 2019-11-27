package ie.gmit.ds.utilities;

public class PasswordConstants {

	public final static int PASSWORD_LENGTH = 5;
	public static final int SLEEP_TIME = 3;
	// Encoding Types
	public final static String ISO = "ISO-8859-1";
	
	// Response Messages
	public final static String LOGIN_SUCCESS = "[200] LOGIN SUCCESSFUL: ";
	public final static String LOGIN_FAILED = "[400] LOGIN FAILED: ";
	public final static String USER_DOESNT_EXIST = "[404] USER DOESNT EXIST: ";
	public final static String USER_ALREADY_EXISTS = "[409] USER ALREADY EXISTS: ";
	
	private PasswordConstants() {
	}

} // PasswordConstants

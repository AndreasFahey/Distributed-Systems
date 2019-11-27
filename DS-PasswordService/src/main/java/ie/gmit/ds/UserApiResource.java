package ie.gmit.ds;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.protobuf.ByteString;
import ie.gmit.ds.utilities.PasswordConstants;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserApiResource {
	
	// Store User with Hash Map
	private HashMap<Integer, HashedAccount> accountMap = new HashMap<>();

	// New Instance of the Client Class
	PasswordClient client = new PasswordClient("localhost", 45620);
	
	// Login - Boolean to check details
	boolean log;

	// Test User Account with Hashed Version also
	public UserApiResource() throws UnsupportedEncodingException {
		User accTest = new User(1, "andreas", "g00@gmail.com", "afahey11");
		client.addPassword(accTest.getUserId(), accTest.getPassword());

		String hashed = new String(client.getHashedPassword().toByteArray(), PasswordConstants.ISO);
		String salt = new String(client.getSalt().toByteArray(), PasswordConstants.ISO);

		HashedAccount accHashed = new HashedAccount(accTest.getUserId(), accTest.getUserName(), accTest.getEmail(),
				hashed, salt);

		accountMap.put(accTest.getUserId(), accHashed);
	}

	// Get to show stored users
	@GET
	public List<HashedAccount> getAccounts() {
		return new ArrayList<HashedAccount>(accountMap.values());
	}

	// Get for specific users
	@GET
	@Path("/{userId}")
	public Response getAccountByID(@PathParam("userId") int userId) {
		if (!checkHashedAccount(accountMap.get(userId)))
			return Response.status(Response.Status.NOT_FOUND).entity(PasswordConstants.USER_DOESNT_EXIST + userId).build();
		return Response.ok(accountMap.get(userId)).build();
	}

	// Post for new users
	@POST
	public Response addAccount(User acc) throws UnsupportedEncodingException {
		if (checkUserAccount(acc.getUserId()))
			return Response.status(Response.Status.CONFLICT).entity(PasswordConstants.USER_ALREADY_EXISTS + acc.getUserId()).build();
		client.addPassword(acc.getUserId(), acc.getPassword());
		System.out.println("Hash for new user:" + client.getHashedPassword());
		System.out.println("Salt for new user:" + client.getSalt());

		String hashed = new String(client.getHashedPassword().toByteArray(), PasswordConstants.ISO);
		String salt = new String(client.getSalt().toByteArray(), PasswordConstants.ISO);

		HashedAccount hashedAccount = new HashedAccount(acc.getUserId(), acc.getUserName(), acc.getEmail(), hashed,
				salt);

		accountMap.put(hashedAccount.getUserId(), hashedAccount);

		return Response.ok(acc).build();
	}

	// Put to update user details - remove at userID then add user back at userID
	@PUT
	@Path("/{userId}")
	public Response updateUser(User acc, @PathParam("userId") int userId) throws UnsupportedEncodingException {
		if (!checkUserAccount(userId))
			return Response.status(Response.Status.NOT_FOUND).entity(PasswordConstants.USER_DOESNT_EXIST + userId).build();

		accountMap.remove(userId);

		client.addPassword(acc.getUserId(), acc.getPassword());

		String hashed = new String(client.getHashedPassword().toByteArray(), PasswordConstants.ISO);
		String salt = new String(client.getSalt().toByteArray(), PasswordConstants.ISO);

		HashedAccount hashedAccount = new HashedAccount(acc.getUserId(), acc.getUserName(), acc.getEmail(), hashed,
				salt);

		accountMap.put(hashedAccount.getUserId(), hashedAccount);

		return Response.ok(acc).build();
	}

	// Delete to remove user from service
	@DELETE
	@Path("/{userId}")
	public Response deleteUser(User acc, @PathParam("userId") int userId) {
		if (!checkUserAccount(userId))
			return Response.status(Response.Status.NOT_FOUND).entity(PasswordConstants.USER_DOESNT_EXIST + userId).build();

		accountMap.remove(userId);
		return Response.ok().build();
	}

	// Post to login user
	@POST
	@Path("/login/")
	public Response login(UserLogin login) throws UnsupportedEncodingException {
		
		if (!checkUserAccount(login.getUserId()))
			return Response.status(Response.Status.PRECONDITION_FAILED).entity(PasswordConstants.LOGIN_FAILED + login.getUserId()).build();
		

			HashedAccount hashedUser = accountMap.get(login.getUserId());
		
			byte[] hashedByte = hashedUser.getHashedPassword().getBytes(PasswordConstants.ISO);
			byte[] saltByte = hashedUser.getSalt().getBytes(PasswordConstants.ISO);
		
			ByteString hashed = ByteString.copyFrom(hashedByte);
			ByteString salt = ByteString.copyFrom(saltByte);
		
			System.out.println(hashedByte);
			System.out.println(salt);
			
			log = client.validatePassword(login.getPassword(), hashed, salt);
			

		if(!log)
			return Response.status(Response.Status.PRECONDITION_FAILED).entity(PasswordConstants.LOGIN_FAILED + login.getUserId()).build();

		return Response.status(Response.Status.ACCEPTED).entity(PasswordConstants.LOGIN_SUCCESS + login.getUserId()).build();
	}

	// check User Account to see if User Account exists
	public Boolean checkUserAccount(int userId) {
		if (accountMap.containsKey(userId))
			return true;
		return false;
	}

	// check Hashed Account to see if Hashed Account exists
	public Boolean checkHashedAccount(HashedAccount acc) {
		if (acc != null)
			return true;
		return false;
	}

}

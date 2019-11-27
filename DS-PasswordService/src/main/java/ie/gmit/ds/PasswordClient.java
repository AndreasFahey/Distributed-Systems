package ie.gmit.ds;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import ie.gmit.ds.utilities.PasswordConstants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class PasswordClient {

	// Byte Array
	static byte[] saltByte;
	
	// Variables
	private ByteString salt;
	private ByteString hashedPassword;
	
	// Getters
	public ByteString getSalt() {
		return salt;
	}

	public ByteString getHashedPassword() {
		return hashedPassword;
	}

	// Logger
	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

	
	public PasswordClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
	}

	// Shutdown 
	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	
	// Create New User
	public byte[] hpw;
	public String hpwString;
	public byte[] sby;
	public String sbyString;
	public User newUser;
	
	public void addPassword(int userId, String password) {
		StreamObserver<PasswordHashResponse> responseObserver = new StreamObserver<PasswordHashResponse>() {

			@Override
			public void onNext(PasswordHashResponse value) {
				hashedPassword = value.getHashedPassword();
				salt = value.getSalt();
				

				hpw = hashedPassword.toByteArray();
				hpwString = Arrays.toString(hpw);

				sby = salt.toByteArray();
				sbyString = Arrays.toString(sby);
			}

			@Override
			public void onError(Throwable t) {
				Status status = Status.fromThrowable(t);
				logger.log(Level.WARNING, "RPC ERROR: {0}", status);
			}

			@Override
			public void onCompleted() {}
		};
				
		try {
			PasswordHashRequest request = PasswordHashRequest.newBuilder().setUserId(userId).setPassword(password).build();
			asyncPasswordService.hash(request, responseObserver);
			TimeUnit.SECONDS.sleep(PasswordConstants.SLEEP_TIME);
		} catch (StatusRuntimeException | InterruptedException ex) {
			logger.log(Level.WARNING, "RPC FAILED: {0}", ex.fillInStackTrace());
		}

		logger.info("Hashing Finished..");
		return;
	}

	// Password Validation
	public boolean validatePassword(String password, ByteString hashed, ByteString salt) {
		try {
			System.out.println("Client: validatePassword");
			BoolValue value = syncPasswordService.validate(PasswordValidateRequest.newBuilder().setPassword(password)
					.setHashedPassword(hashed).setSalt(salt).build());
			
			return(value.getValue());
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC FAILED: {0}", e.getStatus());
			return false;
		}
	}
	
} // PasswordClient

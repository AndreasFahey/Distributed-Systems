package ie.gmit.ds;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.TimeUnit;
import com.google.protobuf.ByteString;
import com.google.protobuf.BoolValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.StatusRuntimeException;
import ie.gmit.ds.utilities.Passwords;

public class PasswordClient {

	// Scanner for user input
	Scanner userIn = new Scanner(System.in);

	// Variables
	private int userId;
	private String userPw;

	byte[] saltByte;
	ByteString salt;
	ByteString hashedPw;
	boolean input = false;

	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	private final ManagedChannel channel;
	// **Generate sources in maven**
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

	public PasswordClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);

	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	// User input to add password
	@SuppressWarnings("unused")
	public void addPw() {

		System.out.println("Please Enter User ID: ");
		userId = userIn.nextInt();

		while (!input) {
			System.out.println("\nPlease Enter User Password (Must Contain Min=2 Letters and Min=2 Numbers): ");
			userPw = userIn.next();

			if (Passwords.isValid(userPw.toCharArray()))
				input = true;
			else
				System.out.println("The Password You Entered is Invalid ! ");
		}

		saltByte = Passwords.getNextSalt();
		salt = ByteString.copyFrom(saltByte);
		hashedPw = ByteString.copyFrom(Passwords.hash(userPw.toCharArray(), saltByte));

		PasswordHashRequest password = PasswordHashRequest.newBuilder().setUserId(userId).setPassword(userPw).build();
		PasswordHashResponse pwResponse = PasswordHashResponse.newBuilder().setUserId(userId)
				.setHashedPassword(hashedPw).setSalt(salt).build();

		try {
			pwResponse = syncPasswordService.hash(password);
		} catch (StatusRuntimeException exception) {
			logger.log(Level.WARNING, "GRPC FAILED: (0)", exception.getStatus());
			return;
		}
	}

	// Validate if password matches requirements
	public void validatePw() {

		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {
			@Override
			public void onNext(BoolValue value) {
				if (value.getValue())
					System.out.println("The Password Entered is Correct !");
				else
					System.out.println("The Password Entered is Invalid !");
			}

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable t) {
			}

		};

		try {
			asyncPasswordService.validate(PasswordValidateRequest.newBuilder().setPassword(userPw)
					.setHashedPassword(hashedPw).setSalt(salt).build(), responseObserver);
		} catch (StatusRuntimeException exception1) {
			logger.log(Level.WARNING, "GRPC FAILED: (0)", exception1.getStatus());
		}
	}

	public static void main(String[] args) throws Exception {
		PasswordClient client = new PasswordClient("localhost", 45620);
		try {
			client.addPw();
			client.validatePw();
		} finally {
			// Do not terminate for async response
			Thread.currentThread().join();
		}
	}

} // PasswordClient

package ie.gmit.ds;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import ie.gmit.ds.utilities.Passwords;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {
	// Logger
	private static final Logger logger = Logger.getLogger(PasswordServiceImpl.class.getName());

	public PasswordServiceImpl() {
		
	}

	@Override
	public void hash(PasswordHashRequest request, StreamObserver<PasswordHashResponse> responseObserver) {
		logger.info("Hash Method In");

			char[] password = request.getPassword().toCharArray();
			byte[] salt = Passwords.getNextSalt();
			byte[] hashedPassword = Passwords.hash(password, salt);
			
			ByteString hashedPw1 = ByteString.copyFrom(hashedPassword);
			ByteString salt1 = ByteString.copyFrom(salt);
					
			PasswordHashResponse response = PasswordHashResponse.newBuilder()
					.setUserId(request.getUserId())
					.setHashedPassword(hashedPw1)
					.setSalt(salt1)
					.build();
			
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	// Validate Request
	@Override
	public void validate(PasswordValidateRequest request, StreamObserver<BoolValue> responseObserver) {
		try {
			char[] password = request.getPassword().toCharArray();
			byte[] salt = request.getSalt().toByteArray();
			byte[] expectedHash = request.getHashedPassword().toByteArray();
	
			if(Passwords.isExpectedPassword(password, salt, expectedHash))
				responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
			else
				responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
			
			responseObserver.onCompleted();
			
		} catch (StatusRuntimeException e) {
		      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
	           return;
		}
	}
} // PasswordServiceImpl

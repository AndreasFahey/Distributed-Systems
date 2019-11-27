package ie.gmit.ds;

import java.io.IOException;
import java.util.logging.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class PasswordServer {
	
	//***Run This Class before you run the PasswordClient.java class.***

	// Variables
	private Server gRPCServer;
	private static final Logger logger = Logger.getLogger(PasswordServer.class.getName());
	private static final int PORT = 45620;

	// Start the server
	private void start() throws IOException {
		gRPCServer = ServerBuilder.forPort(PORT).addService(new PasswordServiceImpl()).build().start();
		logger.info("Server Started on Port: " + PORT);
	}

	@SuppressWarnings("unused")
	private void stop() {
		if (gRPCServer != null) {
			gRPCServer.shutdown();
		}
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (gRPCServer != null) {
			gRPCServer.awaitTermination();
		}
	}

	// Runs to allow PasswordClient.java to register new Password
	public static void main(String[] args) throws IOException, InterruptedException {
		final PasswordServer passwordServer = new PasswordServer();
		passwordServer.start();
		passwordServer.blockUntilShutdown();
	}

} // PasswordServer

package ie.gmit.ds;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserApiApplication extends Application<UserApiConfig> {
	public static void main(String[] args) throws Exception {
        new UserApiApplication().run(args);
    }

	@Override
	public void run(UserApiConfig configuration, Environment environment) throws Exception {
		
        final UserApiResource resource = new UserApiResource();
        
        // Health Check 
        final ExampleHealthCheck healthCheck = new ExampleHealthCheck();
		environment.healthChecks().register("example", healthCheck);

        environment.jersey().register(resource);
	}
}// UserApiApplication

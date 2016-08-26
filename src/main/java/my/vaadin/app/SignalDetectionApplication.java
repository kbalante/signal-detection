package my.vaadin.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Spring Boot Application Setup
 */
@SpringBootApplication
public class SignalDetectionApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SignalDetectionApplication.class);
	}

	/**
	 * Main method for the spring boot application
	 * @param args
     */
	public static void main(String[] args) {
		SpringApplication.run(SignalDetectionApplication.class, args);
	}
}

package ca.mcgill.ecse321.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class RestApplication {

	public static void main(String[] args) {



		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(".env.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String databaseUrl = properties.getProperty("DATABASE_URL");
		String databaseUsername = properties.getProperty("DATABASE_USERNAME");
		String databasePassword = properties.getProperty("DATABASE_PASSWORD");

		// Use the configuration values as needed in your application



		SpringApplication.run(RestApplication.class, args);


	}

}

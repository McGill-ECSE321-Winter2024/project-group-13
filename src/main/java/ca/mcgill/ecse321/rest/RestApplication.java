
package ca.mcgill.ecse321.rest;

import ca.mcgill.ecse321.rest.dao.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EntityScan(basePackages = {"ca.mcgill.ecse321.rest.models"})
public class RestApplication {

  private static final Logger log = LoggerFactory.getLogger(RestApplication.class);

  @Autowired
  private PersonRepository personRepository;

  public static void main(String[] args) {

    SpringApplication.run(RestApplication.class);

    // upsert admin

  }
}

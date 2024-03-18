package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CourseRepository courseRepository;


    @AfterEach
    public void clearDatabase() {
        courseRepository.deleteAll();
    }

}

package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.OwnerRepository;
import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private AuthenticationService authenticationService;

    private final String sportCenterName="HealthPlus";
    private final String ownerEmail="owner@gmail.com";
    private final String instructorEmail="instructor@gmail.com";
    private final String customerEmail="customer@gmail.com";

    @BeforeAll
    public void set_up(){
        String personPassword = "password";
        String personName = "Dave";
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        sportCenter.setName(sportCenterName);
        owner.setSportCenter(sportCenter);
        instructor.setSportCenter(sportCenter);
        customer.setSportCenter(sportCenter);
        createPerson(owner,ownerEmail,"123-456-7890",personName,personPassword);
        createPerson(instructor,instructorEmail,"456-123-7890",personName,personPassword);
        createPerson(customer,customerEmail,"789-456-0123",personName,personPassword);

        personRepository.save(owner);
        personRepository.save(customer);
        personRepository.save(instructor);
        sportCenterRepository.save(sportCenter);

    }

    @AfterEach
    public void clearCourses() {
        courseRepository.deleteAll();
    }
    @AfterAll
    public void clearDatabase() {
        personRepository.deleteAll();

        sportCenterRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void createInvalidCourseTest(){
        // Set up
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail);
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail);
        String name = "Yoga";
        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        CourseDTO requestBody = new CourseDTO("",sportCenter.getId());
        CourseDTO requestBody1 = new CourseDTO(name,"");
        CourseDTO requestBody2 = new CourseDTO(name,sportCenter.getId());

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<CourseDTO> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<HTTPDTO> response = client.postForEntity("/courses", request,HTTPDTO.class);

        HttpEntity<CourseDTO> request1 = new HttpEntity<>(requestBody1, headers);
        ResponseEntity<HTTPDTO> response1 = client.postForEntity("/courses", request1,HTTPDTO.class);

        headers.setBearerAuth(customerAuthentication);
        HttpEntity<CourseDTO> request2 = new HttpEntity<>(requestBody2, headers);
        ResponseEntity<HTTPDTO> response2 = client.postForEntity("/courses", request2, HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Course requires name to be created", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals("Invalid sport's center id", response1.getBody().getMessage());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
        assertEquals("Must be an owner or instructor", response2.getBody().getMessage());
    }
    @Test
    @Order(2)
    public void createCourseTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
        String name = "Yoga";
        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        CourseDTO requestBody = new CourseDTO(name,sportCenter.getId());


        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<CourseDTO> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<?> response = client.postForEntity("/courses", request,CourseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseDTO createdCourse= new  CourseDTO(courseRepository.findCourseByName(name));
        assertNotNull(createdCourse);
        assertEquals(name, createdCourse.getName());
        assertNotNull(createdCourse.getId());
        assertEquals("AwaitingApproval", createdCourse.getCourseState());
    }

    public static void createPerson(Person person, String email, String phoneNumber,String name,String password){
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

}

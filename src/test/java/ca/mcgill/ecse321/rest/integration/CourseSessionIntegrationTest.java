package ca.mcgill.ecse321.rest.integration;


import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseSessionIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CourseSessionRepository courseSessionRepository;

    private final String sportCenterName="MySportsCenter";
    private final String ownerEmail="TheOwner@gmail.com";
    private final String instructorEmail="TheInstructor@gmail.com";
    private final String customerEmail="TheCustomer@gmail.com";
    private String courseID;

    @BeforeAll
    public void set_up(){
        String personPassword = "Password1";
        String personName = "Jordan";
        String name = "Weights";
        Timestamp courseStartDate = new Timestamp(2024, 2, 15, 8, 10, 15, 0);
        Timestamp courseEndDate = new Timestamp(2024, 4, 1, 10, 10, 15, 0);
        Course course= new Course();
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        sportCenter.setName(sportCenterName);
        course.setSportCenter(sportCenter);
        courseID = course.getId();
        course.setName(name);
        course.setCourseState("Approved");
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);
        owner.setSportCenter(sportCenter);
        instructor.setSportCenter(sportCenter);
        customer.setSportCenter(sportCenter);
        createPerson(owner,ownerEmail,"222-456-7890",personName,personPassword);
        createPerson(instructor,instructorEmail,"444-123-7890",personName,personPassword);
        createPerson(customer,customerEmail,"777-456-0123",personName,personPassword);
        Schedule schedule= new Schedule();
        schedule.setMondayStart(new Time(8, 0, 0));
        schedule.setMondayEnd(new Time(18, 0, 0));
        schedule.setSaturdayStart(new Time(8, 0, 0));
        schedule.setSaturdayEnd(new Time(18, 0, 0));
        schedule.setWednesdayStart(new Time(8, 0, 0));
        schedule.setWednesdayEnd(new Time(18, 0, 0));
        scheduleRepository.save(schedule);
        course.setSchedule(schedule);

        personRepository.save(owner);
        personRepository.save(customer);
        personRepository.save(instructor);
        sportCenterRepository.save(sportCenter);
        courseRepository.save(course);
    }

    @AfterAll
    public void clearDatabase() {
        roomRepository.deleteAll();
        courseSessionRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
        scheduleRepository.deleteAll();
    }
    @Test
    @Order(1)
    public void createCourseSessionTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
        courseID = courseRepository.findCourseByName("Weights").getId();

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url= "/courses/"+ courseID+"/sessions";
        ResponseEntity<HTTPDTO> response = client.postForEntity(url, request,HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course session created successfully", response.getBody().getMessage());

    }
    @Test
    @Order(2)
    public void createSessionsPerCourseTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        Course course= courseRepository.findCourseByName("Weights");
        String url= "/courses/"+course.getId()+"/sessions/create";
        ResponseEntity<HTTPDTO> response = client.postForEntity(url, request,HTTPDTO.class);
        System.out.println(response.getBody().getMessage());
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course session created successfully", response.getBody().getMessage());
    }
//    @Order(3)
//    public void getCourseSession(){
//        // Set up
//        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
//        courseID = courseRepository.findCourseByName("Weights").getId();
//
//        // Act
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authentication);
//        HttpEntity<String> request = new HttpEntity<>(headers);
//        String url= "/courses/"+ courseID+"/sessions";
//        ResponseEntity<HTTPDTO> response = client.postForEntity(url, request,HTTPDTO.class);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Course session created successfully", response.getBody().getMessage());
//
//    }
    public static void createPerson(
            Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

}

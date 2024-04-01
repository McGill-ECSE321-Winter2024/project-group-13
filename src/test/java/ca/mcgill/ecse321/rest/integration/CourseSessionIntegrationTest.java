package ca.mcgill.ecse321.rest.integration;


import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseSessionDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

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
    private String sessionID;

    @BeforeAll
    public void set_up(){
        String personPassword = "Password1";
        String personName = "Jordan";
        String name = "Weights";
        Timestamp courseStartDate = new Timestamp(2024, 2, 15, 8, 10, 15, 0);
        Timestamp courseEndDate = new Timestamp(2025, 4, 1, 10, 10, 15, 0);
        Course course= new Course();
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        sportCenter.setName(sportCenterName);
        course.setSportCenter(sportCenter);
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
        schedule.setMondayEnd(new Time(9, 0, 0));
        scheduleRepository.save(schedule);
        course.setSchedule(schedule);

        personRepository.save(owner);
        personRepository.save(customer);
        personRepository.save(instructor);
        sportCenterRepository.save(sportCenter);
        courseRepository.save(course);
        courseID = courseRepository.findCourseByName("Weights").getId();

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

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(courseID,headers);
        String url= "/sessions";
        ResponseEntity<HTTPDTO> response = client.postForEntity(url, request,HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course session created successfully", response.getBody().getMessage());

    }
    @Test
    @Order(2)
    public void getCourseSessionTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);

        sessionID= courseSessionRepository.findCourseSessionsByCourse(courseRepository.findCourseById(courseID)).get(0).getId();
        System.out.println(sessionID);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url= "/sessions/"+sessionID;
        ResponseEntity<CourseSessionDTO> response = client.exchange(url, HttpMethod.GET, request,CourseSessionDTO.class );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionID, response.getBody().getId());

    }
    @Test
    @Order(3)
    public void updateCourseSessionStartTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
        String date = "2024-04-01 15:53:28.371";
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(date,headers);
        String url= "/sessions/"+sessionID+"/startTime";
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course session start time changed", response.getBody().getMessage());
    }
    @Test
    @Order(4)
    public void updateCourseSessionEndTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
        String date = "2025-04-01 15:53:28.371";
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(date,headers);
        String url= "/sessions/"+sessionID+"/endTime";
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course session end time changed", response.getBody().getMessage());
    }
    @Test
    @Order(5)
    public void deleteCourseSessionTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url= "/sessions/"+sessionID;
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.DELETE, request,HTTPDTO.class );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course session deleted", response.getBody().getMessage());
    }
    public static void createPerson(
            Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

}

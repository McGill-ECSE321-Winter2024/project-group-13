package ca.mcgill.ecse321.rest.integration;


import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
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
    @BeforeAll
    public void set_up(){
        String personPassword = "Password1";
        String personName = "Jordan";
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        sportCenter.setName(sportCenterName);
        owner.setSportCenter(sportCenter);
        instructor.setSportCenter(sportCenter);
        customer.setSportCenter(sportCenter);
        createPerson(owner,ownerEmail,"222-456-7890",personName,personPassword);
        createPerson(instructor,instructorEmail,"444-123-7890",personName,personPassword);
        createPerson(customer,customerEmail,"777-456-0123",personName,personPassword);

        personRepository.save(owner);
        personRepository.save(customer);
        personRepository.save(instructor);
        sportCenterRepository.save(sportCenter);

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
        String name = "Weights";
        Schedule schedule= new Schedule();
        schedule.setMondayStart(new Time(8, 0, 0));
        schedule.setMondayEnd(new Time(18, 0, 0));
        schedule.setSaturdayStart(new Time(8, 0, 0));
        schedule.setSaturdayEnd(new Time(18, 0, 0));
        schedule.setWednesdayStart(new Time(8, 0, 0));
        schedule.setWednesdayEnd(new Time(18, 0, 0));
        scheduleRepository.save(schedule);


        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(name, headers);
        String url= "/courses/{course_id}+/sessions"
        ResponseEntity<HTTPDTO> response = client.postForEntity("/courses", request,HTTPDTO.class);


        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseDTO createdCourse= new CourseDTO(courseRepository.findCourseByName(name));
        assertNotNull(createdCourse);
        assertEquals(name, createdCourse.getName());
        assertNotNull(createdCourse.getId());
        assertEquals("AwaitingApproval", createdCourse.getCourseState());
    }
    public static void createPerson(
            Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

}

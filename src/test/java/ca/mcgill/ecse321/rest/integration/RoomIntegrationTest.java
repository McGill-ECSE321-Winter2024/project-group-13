package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CourseSessionDTO;
import ca.mcgill.ecse321.rest.dto.RoomDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomIntegrationTest {
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
    private CourseSessionRepository courseSessionRepository;
    @Autowired
    private AuthenticationService authenticationService;

    private final String sportCenterName="HealthPlus1z";
    private final String ownerEmail="owner1234567z@gmail.com";
    private final String instructorEmail="instructor123456z@gmail.com";
    private final String customerEmail="customer123456z@gmail.com";

    @BeforeAll
    public void set_up(){
        clearDatabase();
        String personPassword = "password1";
        String personName = "Jake";
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        sportCenter.setName(sportCenterName);
        owner.setSportCenter(sportCenter);
        instructor.setSportCenter(sportCenter);
        customer.setSportCenter(sportCenter);
        createPerson(owner,ownerEmail,"223-456-7890",personName,personPassword);
        createPerson(instructor,instructorEmail,"446-123-7890",personName,personPassword);
        createPerson(customer,customerEmail,"779-456-0123",personName,personPassword);

        personRepository.save(owner);
        personRepository.save(customer);
        personRepository.save(instructor);
        sportCenterRepository.save(sportCenter);
    }

    @AfterAll
    public void clearDatabase() {
        courseSessionRepository.deleteAll();
        courseRepository.deleteAll();
        roomRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void createRoomTestAsOwner(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
        RoomDTO roomDTO = new RoomDTO("Yoga", sportCenter.getId());

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<RoomDTO> request = new HttpEntity<>(roomDTO, headers);
        ResponseEntity<HTTPDTO> response = client.postForEntity("/rooms", request,HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void createRoomTestAsInstructor(){
        // Set up
        String instructorAuthentication = authenticationService.issueTokenWithEmail(instructorEmail);

        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
        RoomDTO roomDTO = new RoomDTO("Yoga", sportCenter.getId());

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<RoomDTO> request = new HttpEntity<>(roomDTO, headers);
        ResponseEntity<HTTPDTO> response = client.postForEntity("/rooms", request,HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(response.getBody().getMessage(),"Must be the Owner of the Sport Center");
    }

    @Test
    @Order(3)
    public void createRoomTestAsCustomer(){
        // Set up
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);

        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
        RoomDTO roomDTO = new RoomDTO("Yoga", sportCenter.getId());

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<RoomDTO> request = new HttpEntity<>(roomDTO, headers);
        ResponseEntity<HTTPDTO> response = client.postForEntity("/rooms", request,HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(response.getBody().getMessage(),"Must be the Owner of the Sport Center");
    }

    @Test
    @Order(4)
    public void getCoursesPerRoomTest(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
        Room room = new Room();
        room.setRoomName("YogaRoom");
        room.setSportCenter(sportCenter);
        roomRepository.save(room);
        Course course = new Course();
        course.setName("YogaCourse");
        course.setRoom(room);
        course.setSportCenter(sportCenter);
        courseRepository.save(course);
        CourseDTO courseDTO = new CourseDTO(course);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<RoomDTO> request = new HttpEntity<>(new RoomDTO(room), headers);
        ResponseEntity<CourseDTO[]> response = client.exchange("/room/courses", HttpMethod.GET, request, CourseDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertEquals(courseDTO, response.getBody()[0]);
    }

    @Test
    @Order(5)
    public void getCourseSessionsPerRoomTest(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
        Room room = new Room();
        room.setRoomName("YogaRoomName");
        room.setSportCenter(sportCenter);
        roomRepository.save(room);
        Course course = new Course();
        course.setName("YogaCourseName");
        course.setRoom(room);
        course.setSportCenter(sportCenter);
        courseRepository.save(course);
        Schedule schedule = new Schedule();
        scheduleRepository.save(schedule);
        CourseSession courseSession = new CourseSession();
        courseSession.setCourse(course);
        courseSession.setStartTime(new Timestamp(System.currentTimeMillis()));
        courseSession.setEndTime(new Timestamp(System.currentTimeMillis()));
        courseSessionRepository.save(courseSession);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<RoomDTO> request = new HttpEntity<>(new RoomDTO(room), headers);
        ResponseEntity<CourseSessionDTO[]> response = client.exchange("/room/course-sessions", HttpMethod.GET, request, CourseSessionDTO[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertEquals(new CourseSessionDTO(courseSession), response.getBody()[0]);
    }

    @Test
    @Order(6)
    public void deleteExistingRoomTest(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
        Room room = new Room();
        room.setRoomName("YogaRoomName");
        room.setSportCenter(sportCenter);
        roomRepository.save(room);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HTTPDTO> response = client.exchange("/rooms/"+room.getId(), HttpMethod.DELETE, request, HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(roomRepository.findRoomById(room.getId()));
    }

    @Test
    @Order(7)
    public void deleteNonExistingRoomTest(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String nonExistingRoomId = "nonExistingRoomId";
        ResponseEntity<HTTPDTO> response = client.exchange("/rooms/"+nonExistingRoomId, HttpMethod.DELETE, request, HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Room does not exist", response.getBody().getMessage());
    }

    public static void createPerson(Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

}

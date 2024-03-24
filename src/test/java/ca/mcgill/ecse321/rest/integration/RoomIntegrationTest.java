package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    private AuthenticationService authenticationService;

    private final String sportCenterName="HealthPlus1";
    private final String ownerEmail="owner123456@gmail.com";
    private final String instructorEmail="instructor123456@gmail.com";
    private final String customerEmail="customer123456@gmail.com";

    @BeforeAll
    public void set_up(){
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
        roomRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    public static void createPerson(Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

//    @Test
//    @Order(1)
//    public void createInvalidRoomTest(){
//        // Set up
//        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
//        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail);
//        String name = "Yoga";
//        // Act
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(ownerAuthentication);
//        HttpEntity<String> request = new HttpEntity<>("", headers);
//        ResponseEntity<HTTPDTO> response = client.postForEntity("/rooms", request,HTTPDTO.class);
//
//        headers.setBearerAuth(customerAuthentication);
//        HttpEntity<String> request2 = new HttpEntity<>(name, headers);
//        ResponseEntity<HTTPDTO> response2 = client.postForEntity("/rooms", request2, HTTPDTO.class);
//
//        // Assert
//        assertNotNull(response);
//        assertNotNull(response2);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Room requires name to be created", response.getBody().getMessage());
//        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
//        assertEquals("Must be the Owner of the Sport Center", response2.getBody().getMessage());
//    }
//    @Test
//    @Order(2)
//    public void createRoomTest(){
//        // Set up
//        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
//        String name = "Yoga";
//
//        // Act
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authentication);
//        HttpEntity<String> request = new HttpEntity<>(name, headers);
//        ResponseEntity<HTTPDTO> response = client.postForEntity("/rooms", request,HTTPDTO.class);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        RoomDTO createdRoom= new RoomDTO(roomRepository.findRoomByRoomName(name));
//        assertNotNull(createdRoom);
//        assertEquals(name, createdRoom.getName());
//        assertNotNull(createdRoom.getId());
//    }

    //TODO get courses per room test
    //TODO get course sessions per room test (owner only)
}

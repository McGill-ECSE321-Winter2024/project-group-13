package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.SportCenterDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Time;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportCenterIntegrationTest {
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


    private final String sportCenterName="HealthPlus";
    private final String ownerEmail="owner@gmail.com";
    private final String instructorEmail="instructor@gmail.com";
    private final String customerEmail="customer@gmail.com";

    @BeforeEach
    public void set_up(){
        clearRepositories();
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
    @AfterAll
    public void clearDatabase() {
        clearRepositories();
    }

    @Test
    @Order(1)
    public void testGetSportCenter(){
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail);
        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        assertNotNull(sportCenter);
        SportCenterDTO sportCenterDTO = new SportCenterDTO(sportCenter);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<SportCenterDTO> request = new HttpEntity<>(headers);
        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter", HttpMethod.GET, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(sportCenterDTO,response.getBody());
    }



    private void clearRepositories() {
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }
    public static void createPerson(
            Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }



}

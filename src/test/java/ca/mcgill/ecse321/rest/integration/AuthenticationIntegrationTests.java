package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.LoginDTO;
import ca.mcgill.ecse321.rest.dto.auth.RegisterDTO;
import ca.mcgill.ecse321.rest.dto.auth.SessionDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationIntegrationTests {
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
    private RegistrationRepository registrationRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;


    private final String ownerEmail="owner@gmail.com";
    private final String instructorEmail="instructor2221@gmail.com";
    private final String customerEmail="customer20891823@gmail.com";
    private final String customerEmail2="customer993u1023@gmail.com";

    @BeforeAll
    public void set_up(){
        String personPassword = "password";
        String personName = "Dave";
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        Customer customer2 = new Customer();
        String sportCenterName = "HealthPlus";
        sportCenter.setName(sportCenterName);
        owner.setSportCenter(sportCenter);
        instructor.setSportCenter(sportCenter);
        customer.setSportCenter(sportCenter);
        customer2.setSportCenter(sportCenter);

        createPerson(owner,ownerEmail,"123-456-7890",personName,personPassword);
        createPerson(instructor,instructorEmail,"456-123-7890",personName,personPassword);
        createPerson(customer,customerEmail,"789-456-0123",personName,personPassword);
        createPerson(customer2, customerEmail2, "123-456-7878", personName, personPassword);

        personRepository.save(owner);
        personRepository.save(customer);
        personRepository.save(instructor);
        personRepository.save(customer2);
        sportCenterRepository.save(sportCenter);

    }

    @AfterAll
    public void clearDatabase() {
        invoiceRepository.deleteAll();
        registrationRepository.deleteAll();
        roomRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testLoginCorrectCredential(){

        // owner login
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO login =  new LoginDTO(ownerEmail,"password");
        Person person = personRepository.findPersonByEmail(ownerEmail);
        HttpEntity<LoginDTO> request = new HttpEntity<>(login, headers);
        ResponseEntity<SessionDTO> response = client.postForEntity("/auth/login", request, SessionDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PersonSession personSession = authenticationService.verifyTokenAndGetUser("Bearer "+response.getBody().getSession());
        assertEquals(person.getId(), personSession.getPersonId());
        assertEquals(PersonSession.PersonType.Owner, personSession.getPersonType());

        // instructor login
        login.setEmail(instructorEmail);
        login.setPassword("password");
        request = new HttpEntity<>(login, headers);
        response = client.postForEntity("/auth/login", request, SessionDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        person = personRepository.findPersonByEmail(instructorEmail);
        personSession = authenticationService.verifyTokenAndGetUser("Bearer "+response.getBody().getSession());
        assertEquals(person.getId(), personSession.getPersonId());
        assertEquals(PersonSession.PersonType.Instructor, personSession.getPersonType());

        // customer login
        login.setEmail(customerEmail);
        login.setPassword("password");
        request = new HttpEntity<>(login, headers);
        response = client.postForEntity("/auth/login", request, SessionDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        person = personRepository.findPersonByEmail(customerEmail);
        personSession = authenticationService.verifyTokenAndGetUser("Bearer "+response.getBody().getSession());
        assertEquals(person.getId(), personSession.getPersonId());

    }


    @Test
    @Order(2)
    public void testLoginIncorrectCredential(){

        // owner login
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO login =  new LoginDTO(ownerEmail,"wrongpassword");
        HttpEntity<LoginDTO> request = new HttpEntity<>(login, headers);
        ResponseEntity<SessionDTO> response = client.postForEntity("/auth/login", request, SessionDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // instructor login
        login.setEmail(instructorEmail);
        login.setPassword("wrongpassword");
        request = new HttpEntity<>(login, headers);
        response = client.postForEntity("/auth/login", request, SessionDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // customer login
        login.setEmail(customerEmail);
        login.setPassword("wrongpassword");
        request = new HttpEntity<>(login, headers);
        response = client.postForEntity("/auth/login", request, SessionDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    @Order(3)
    public void testRegisterCustomerWithSameEmail() {
        String session = authenticationService.issueTokenWithEmail(ownerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        String email = customerEmail;
        String password = "password";
        String name = "John";
        String phoneNumber = "123-456-7899";
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail(email);
        registerDTO.setPassword(password);
        registerDTO.setName(name);
        registerDTO.setPhoneNumber(phoneNumber);

        HttpEntity<RegisterDTO> request = new HttpEntity<>(registerDTO, headers);

        ResponseEntity<String> response = client.postForEntity("/auth/register/customer", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    // invalid email
    @Test
    @Order(4)
    public void testRegisterCustomerInvalidEmail() {
        String session = authenticationService.issueTokenWithEmail(ownerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        String email = "customer2test.com";
        String password = "password";
        String name = "John";
        String phoneNumber = "123-456-7899";
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail(email);
        registerDTO.setPassword(password);
        registerDTO.setName(name);
        registerDTO.setPhoneNumber(phoneNumber);

        HttpEntity<RegisterDTO> request = new HttpEntity<>(registerDTO, headers);

        ResponseEntity<String> response = client.postForEntity("/auth/register/customer", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    // register customer as instructor
    @Test
    @Order(5)
    public void testRegisterCustomerAsInstructor() {
        String session = authenticationService.issueTokenWithEmail(instructorEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        RegisterDTO registerDTO = new RegisterDTO();

        HttpEntity<RegisterDTO> request = new HttpEntity<>(registerDTO, headers);

        ResponseEntity<String> response = client.postForEntity("/auth/register/customer", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @Order(6)
    public void testChangePassword() {
        String session = authenticationService.issueTokenWithEmail(customerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword("newpassword");

        HttpEntity<LoginDTO> request = new HttpEntity<>(loginDTO, headers);

        ResponseEntity<String> response = client.exchange("/auth/password", HttpMethod.PUT, request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(7)
    public void testChangeEmail() {
        String session = authenticationService.issueTokenWithEmail(customerEmail2);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        String email = "customer6@gmail.com";
        Map<String, String> emailMap = Map.of("email", email);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(emailMap, headers);

        ResponseEntity<String> response = client.exchange("/auth/email", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testChangeEmailInvalidEmail() {
        String session = authenticationService.issueTokenWithEmail(customerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        String email = "customer2test.com";
        Map<String, String> emailMap = Map.of("email", email);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(emailMap, headers);

        ResponseEntity<String> response = client.exchange("/auth/email", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(9)
    public void testChangePhoneNumber() {
        System.out.println(personRepository.findAll());
        String session = authenticationService.issueTokenWithEmail(customerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        String phoneNumber = "123-456-7891";
        Map<String, String> phoneNumberMap = Map.of("phoneNumber", phoneNumber);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(phoneNumberMap, headers);

        ResponseEntity<String> response = client.exchange("/auth/phoneNumber", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // phone number already in use
    @Test
    @Order(10)
    public void testChangePhoneNumberAlreadyInUse() {
        String session = authenticationService.issueTokenWithEmail(customerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(session);
        String phoneNumber = "123-456-7890";
        Map<String, String> phoneNumberMap = Map.of("phoneNumber", phoneNumber);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(phoneNumberMap, headers);

        ResponseEntity<String> response = client.exchange("/auth/phoneNumber", HttpMethod.PUT, request, String.class);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    public static void createPerson(
            Person person, String email, String phoneNumber, String name, String password
    ) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

    public Invoice getSampleInvoice(Registration registration) {
        Invoice invoice = new Invoice();
        invoice.setRegistration(registration);
        invoice.setAmount(100);
        invoice.setStatus(Invoice.Status.Open);
        invoiceRepository.save(invoice);
        return invoice;
    }
}

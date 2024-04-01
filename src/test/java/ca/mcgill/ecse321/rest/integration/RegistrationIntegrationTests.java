package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.*;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationIntegrationTests {


    //Using @Autowired on Repositories and Service that will be used later in the code
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private AuthenticationService authenticationService;


    //Global variables
    private final String sportCenterName = "SportCenterNameHere";
    private final String ownerEmail = "owner12332@gmail.com";
    private final String instructorEmail = "instructor@gmail.com";
    private final String instructor1Email = "1instructor@gmail.com";
    private final String customerEmail = "customer@gmail.com";
    private final String customer1Email = "1customer@gmail.com";
    private String registration1ID = "";
    private String registration2ID = "";
    private Course course2 = null;
    private Customer customer2 = null;


    /**
     * This method is called before All the tests;
     * It saves all the needed objects that we use in the following tests.
     * @Author Teddy El-Husseini
     */
    @BeforeAll
    public void set_up() {

        //Objects needed for setup
        int rating1 = 3;
        int rating2 = 4;
        String personPassword = "notMyPassword";
        String personName = "Teddy";
        SportCenter sportCenter = new SportCenter();
        Owner owner = new Owner();
        Instructor instructor1 = new Instructor(); Instructor instructor2 = new Instructor();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        Course course1 = new Course(); Course course2 = new Course();

        sportCenter.setName(sportCenterName);
        owner.setSportCenter(sportCenter);
        instructor1.setSportCenter(sportCenter); instructor2.setSportCenter(sportCenter);
        customer1.setSportCenter(sportCenter); customer2.setSportCenter(sportCenter);
        course1.setInstructor(instructor1); course2.setInstructor(instructor2); course1.setName("HIHIHIHIHI");

        createPerson(owner, ownerEmail, "123-456-7890", personName, personPassword);
        createPerson(instructor1, "1" + instructorEmail, "156-123-7890", personName, personPassword);
        createPerson(instructor2, instructorEmail, "456-123-7890", personName, personPassword);
        createPerson(customer1, "1" + customerEmail, "189-456-0123", personName, personPassword);
        createPerson(customer2, customerEmail, "789-456-0123", personName, personPassword);

        Registration registration1 = new Registration("ID1", rating1, customer1, course1);
        registration1.setCustomer(customer1); registration1.setCourse(course1);
        Registration registration2 = new Registration("ID2", rating2, customer2, course2);
        registration2.setCustomer(customer2); registration2.setCourse(course2);

        Invoice invoice1 = new Invoice();
        invoice1.setAmount(10); invoice1.setRegistration(registration1); invoice1.setStatus(Invoice.Status.Open);
        Invoice invoice2 = new Invoice();
        invoice2.setAmount(30); invoice2.setRegistration(registration1); invoice2.setStatus(Invoice.Status.Open);
        Invoice invoice3 = new Invoice();
        invoice3.setAmount(20); invoice3.setRegistration(registration1); invoice3.setStatus(Invoice.Status.Open);

        personRepository.save(owner);
        personRepository.save(customer1);
        personRepository.save(customer2);
        personRepository.save(instructor1);
        personRepository.save(instructor2);
        courseRepository.save(course1);
        courseRepository.save(course2);
        registrationRepository.save(registration1);
        registrationRepository.save(registration2);
        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);
        invoiceRepository.save(invoice3);
        sportCenterRepository.save(sportCenter);

        this.registration1ID = registration1.getId();
        this.registration2ID = registration2.getId();
        this.course2 = course2;
        this.customer2 = customer2;
    }
    
    /**
     * Clearing the database after all the test
     */
     
    @AfterAll
    public void clearDatabase() {
        invoiceRepository.deleteAll();
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }
    
    /**
     * Template for all the following tests:
     * 
     * First create a header containing the token of a role (Owner, Instructor or Customer)
     * Then depending on the URL and parameters given to "client.exchange", the according method is called
     * in the controller. The returned object should pass all written asserts.
     */
    

    /**
     * This test follows the template described before the first test
     * This tests tries to get all registration for an owner and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(1)
    public void getRegistrationsByOwner_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT = client.exchange(
                "/registrations",
                HttpMethod.GET,
                entity,
                ArrayList.class);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());

        List<RegistrationDTO> NotExpected = new ArrayList<>();
        assertNotEquals(NotExpected, registrationGOT.getBody());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all registration for an owner, an instructor and a customer,
     * but a bad parameter is passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(2)
    public void getRegistrationsByOwnerAndInstructorAndCustomer_Invalid() {

        ResponseEntity<RegistrationDTO> registrationGOT1 = client.exchange(
                "/registrations",
                HttpMethod.GET,
                null,
                RegistrationDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT1.getStatusCode());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get all registration for an instructor and succeeds.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(3)
    public void getRegistrationsByInstructor_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(instructor1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT = client.exchange(
                "/registrations",
                HttpMethod.GET,
                entity,
                ArrayList.class);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());

        List<RegistrationDTO> NotExpected = new ArrayList<>();
        assertNotEquals(NotExpected, registrationGOT.getBody());
    }
    
    /**
     * This test follows the template described before test1
     * This tests tries to get all registration for a customer and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(4)
    public void getRegistrationsByCustomer_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT = client.exchange(
                "/registrations",
                HttpMethod.GET,
                entity,
                ArrayList.class);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());

        List<RegistrationDTO> NotExpected = new ArrayList<>();
        assertNotEquals(NotExpected, registrationGOT.getBody());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an owner given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(5)
    public void getSpecificRegistrationByOwner_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RegistrationDTO> registrationGOT = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                entity,
                RegistrationDTO.class,
                this.registration1ID);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());
        assertEquals(this.registration1ID, registrationGOT.getBody().getID());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an owner given the registrationID.
     * but some invalid parameters are passed
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(6)
    public void getSpecificRegistrationByOwner_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RegistrationDTO> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                entity,
                RegistrationDTO.class,
                "invalidID");

        ResponseEntity<RegistrationDTO> registrationGOT2 = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                null,
                RegistrationDTO.class,
                this.registration1ID);

        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT2.getStatusCode());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an instructor given the registrationID, 
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(7)
    public void getSpecificRegistrationByInstructor_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(instructor1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RegistrationDTO> registrationGOT = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                entity,
                RegistrationDTO.class,
                this.registration1ID);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());
        assertEquals(this.registration1ID, registrationGOT.getBody().getID());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an instructor given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(8)
    public void getSpecificRegistrationByInstructor_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(instructor1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RegistrationDTO> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                entity,
                RegistrationDTO.class,
                "invalidID");

        ResponseEntity<RegistrationDTO> registrationGOT2 = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                null,
                RegistrationDTO.class,
                this.registration1ID);

        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT2.getStatusCode());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for a Customer given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(9)
    public void getSpecificRegistrationByCustomer_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RegistrationDTO> registrationGOT = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                entity,
                RegistrationDTO.class,
                this.registration1ID);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());
        assertEquals(this.registration1ID, registrationGOT.getBody().getID());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for a Customer given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(10)
    public void getSpecificRegistrationByCustomer_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RegistrationDTO> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                entity,
                RegistrationDTO.class,
                "invalidID");

        ResponseEntity<RegistrationDTO> registrationGOT2 = client.exchange(
                "/registrations/{registration_id}",
                HttpMethod.GET,
                null,
                RegistrationDTO.class,
                this.registration1ID);

        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT2.getStatusCode());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for an owner given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(11)
    public void cancelSpecificRegistrationByOwner_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT = client.exchange(
                "/registrations/{registration_id}/cancel",
                HttpMethod.POST,
                entity,
                Boolean.class,
                this.registration2ID);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());
        assertEquals(true, registrationGOT.getBody());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for an owner given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(12)
    public void cancelSpecificRegistrationByOwner_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/cancel",
                HttpMethod.POST,
                entity,
                Boolean.class,
                "INVALID");

        assertThrows(RestClientException.class, () -> {
             client.exchange(
                    "/registrations/{registration_id}/cancel",
                    HttpMethod.POST,
                    null,
                    Boolean.class,
                    this.registration1ID);
        });
        
        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());
        assertEquals(false, registrationGOT1.getBody());


    }
    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for an instructor given the registrationID,
     * but instructor cannot cancel a registration.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(13)
    public void cancelSpecificRegistrationByInstructor_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(instructorEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT = client.exchange(
                "/registrations/{registration_id}/cancel",
                HttpMethod.POST,
                entity,
                Boolean.class,
                this.registration2ID);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());
        assertEquals(false, registrationGOT.getBody());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for a customer given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(14)
    public void cancelSpecificRegistrationByCustomer_Valid() {

        Registration registration2 = new Registration("ID2", 4, customer2, course2);
        registration2.setCustomer(customer2);
        registration2.setCourse(course2);
        registrationRepository.save(registration2);

        String authentication = authenticationService.issueTokenWithEmail(customerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT = client.exchange(
                "/registrations/{registration_id}/cancel",
                HttpMethod.POST,
                entity,
                Boolean.class,
                registration2.getId());

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());
        assertEquals(true, registrationGOT.getBody());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for a customer given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(15)
    public void cancelSpecificRegistrationByCustomer_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(customerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/cancel",
                HttpMethod.POST,
                entity,
                Boolean.class,
                "INVALID");

        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());
        assertEquals(false, registrationGOT1.getBody());

    }
    /**
     * This test follows the template described before the first test
     * This tests tries to update a specific registration for an owner and an intructors given the registrationID and rating,
     * but owners and instructors cannot update the rating of a registration.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(16)
    public void updateRegistrationByOwnerAndInstructor_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String authentication1 = authenticationService.issueTokenWithEmail(instructor1Email);
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setBearerAuth(authentication1);
        HttpEntity<String> entity1 = new HttpEntity<>(headers1);

        ResponseEntity<Boolean> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/{rating}",
                HttpMethod.POST,
                entity,
                Boolean.class,
                this.registration1ID, 3);

        ResponseEntity<Boolean> registrationGOT2 = client.exchange(
                "/registrations/{registration_id}/{rating}",
                HttpMethod.POST,
                entity1,
                Boolean.class,
                this.registration1ID, 3);

        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());
        assertEquals(false, registrationGOT1.getBody());
        assertEquals(HttpStatus.OK, registrationGOT2.getStatusCode());
        assertEquals(false, registrationGOT2.getBody());

    }
    /**
     * This test follows the template described before the first test
     * This tests tries to update a specific registration for a customer given the registrationID and rating and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(17)
    public void updateRegistrationByCustomer_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(customer1Email);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/{rating}",
                HttpMethod.POST,
                entity,
                Boolean.class,
                this.registration1ID, 3);

        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());
        assertEquals(true, registrationGOT1.getBody());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to update a specific registration for a customer given the registrationID and rating,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(18)
    public void updateRegistrationByCustomer_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(customerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT3 = client.exchange(
                "/registrations/{registration_id}/{rating}",
                HttpMethod.POST,
                entity,
                Boolean.class,
                this.registration1ID, 3);
        
        ResponseEntity<Boolean> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/{rating}",
                HttpMethod.POST,
                entity,
                Boolean.class,
                "this.registration1ID", 3);

        ResponseEntity<Boolean> registrationGOT2 = client.exchange(
                "/registrations/{registration_id}/{rating}",
                HttpMethod.POST,
                entity,
                Boolean.class,
                this.registration1ID, 0);
        
        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());
        assertEquals(false, registrationGOT1.getBody());

        assertEquals(HttpStatus.OK, registrationGOT2.getStatusCode());
        assertEquals(false, registrationGOT2.getBody());

        assertEquals(HttpStatus.OK, registrationGOT3.getStatusCode());
        assertEquals(false, registrationGOT3.getBody());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for an owner given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(19)
    public void getInvoicesByOwner_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, this.registration1ID);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());

        List<RegistrationDTO> NotExpected = new ArrayList<>();
        assertNotEquals(NotExpected, registrationGOT.getBody());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for an owner given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(20)
    public void getInvoicesByOwner_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, this.registration2ID);

        ResponseEntity<ArrayList> registrationGOT2 = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, "invalid");

        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());

    List<Invoice> expected = new ArrayList<>();
    assertEquals(expected, registrationGOT1.getBody());
    assertEquals(expected, registrationGOT2.getBody());
}

    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for an instructor given the registrationID,
     * but instructors can not get invoices.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(21)
    public void getInvoicesByInstructor_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(instructorEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, this.registration1ID);

        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT1.getStatusCode());
    }
    
    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for a customer given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(22)
    public void getInvoicesByCustomer_Valid() {

        String authentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, this.registration1ID);

        assertNotNull(registrationGOT);
        assertEquals(HttpStatus.OK, registrationGOT.getStatusCode());

        List<RegistrationDTO> NotExpected = new ArrayList<>();
        assertNotEquals(NotExpected, registrationGOT.getBody());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for a customer given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    @Order(23)
    public void getInvoicesByCustomer_Invalid() {

        String authentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, this.registration2ID);

        ResponseEntity<ArrayList> registrationGOT2 = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, "invalid");

        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());

        List<Invoice> expected = new ArrayList<>();
        assertEquals(expected, registrationGOT1.getBody());
        assertEquals(expected, registrationGOT2.getBody());
    }

    /**
     * This method sets the following parameters to a person
     * @param person
     * @param email
     * @param phoneNumber
     * @param name
     * @param password
     * 
     * @Author Mohamed 
     */
    public static void createPerson(Person person, String email, String phoneNumber,String name,String password){
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }
}

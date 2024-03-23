package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.*;
import ca.mcgill.ecse321.rest.helpers.*;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationIntegrationTests {

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


    private final String sportCenterName = "HealthPlus";
    private final String ownerEmail = "owner@gmail.com";
    private String ownerID = "";
    private final String instructorEmail = "instructor@gmail.com";
    private final String instructor1Email = "1instructor@gmail.com";
    private final String customerEmail = "customer@gmail.com";
    private final String customer1Email = "1customer@gmail.com";

    private String registration1ID = "";
    private String registration2ID = "";

    private Course course1 = null;
    private Course course2 = null;
    private Customer customer1 = null;
    private Customer customer2 = null;


    @BeforeAll
    public void set_up() {

        int rating1 = 3;
        int rating2 = 4;
        String personPassword = "notMyPassword";
        String personName = "Teddy";
        SportCenter sportCenter = new SportCenter();
        Owner owner = new Owner();
        Instructor instructor1 = new Instructor();
        Instructor instructor2 = new Instructor();
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        Course course1 = new Course();
        Course course2 = new Course();

        sportCenter.setName(sportCenterName);
        owner.setSportCenter(sportCenter);
        instructor1.setSportCenter(sportCenter);
        instructor2.setSportCenter(sportCenter);
        customer1.setSportCenter(sportCenter);
        customer2.setSportCenter(sportCenter);
        course1.setInstructor(instructor1);
        course2.setInstructor(instructor2);
        course1.setName("HIHIHIHIHI");

        createPerson(owner, ownerEmail, "123-456-7890", personName, personPassword);
        createPerson(instructor1, "1" + instructorEmail, "156-123-7890", personName, personPassword);
        createPerson(instructor2, instructorEmail, "456-123-7890", personName, personPassword);
        createPerson(customer1, "1" + customerEmail, "189-456-0123", personName, personPassword);
        createPerson(customer2, customerEmail, "789-456-0123", personName, personPassword);

        Registration registration1 = new Registration("ID1", rating1, customer1, course1);
        registration1.setCustomer(customer1);
        registration1.setCourse(course1);
        Registration registration2 = new Registration("ID2", rating2, customer2, course2);
        registration2.setCustomer(customer2);
        registration2.setCourse(course2);

        Invoice invoice1 = new Invoice();
        invoice1.setAmount(10);
        invoice1.setRegistration(registration1);
        invoice1.setStatus(Invoice.Status.Open);
        Invoice invoice2 = new Invoice();
        invoice2.setAmount(30);
        invoice2.setRegistration(registration1);
        invoice2.setStatus(Invoice.Status.Open);
        Invoice invoice3 = new Invoice();
        invoice3.setAmount(20);
        invoice3.setRegistration(registration1);
        invoice3.setStatus(Invoice.Status.Open);


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
        this.course1 = course1;
        this.course2 = course2;
        this.customer1 = customer1;
        this.customer2 = customer2;
        this.ownerID = personRepository.findPersonByEmail(ownerEmail).getId();
    }

    @AfterAll
    public void clearDatabase() {
        invoiceRepository.deleteAll();
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void getRegistrationsByOwner_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(2)
    public void getRegistrationsByOwnerAndInstructor_AndCustomerInvalid() {

        ResponseEntity<RegistrationDTO> registrationGOT1 = client.exchange(
                "/registrations",
                HttpMethod.GET,
                null,
                RegistrationDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT1.getStatusCode());
    }

    @Test
    @Order(3)
    public void getRegistrationsByInstructor_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(instructor1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(4)
    public void getRegistrationsByCustomer_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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


    @Test
    @Order(5)
    public void getSpecificRegistrationByOwner_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(6)
    public void getSpecificRegistrationByOwner_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(7)
    public void getSpecificRegistrationByInstructor_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(instructor1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(8)
    public void getSpecificRegistrationByInstructor_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(instructor1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(9)
    public void getSpecificRegistrationByCustomer_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(10)
    public void getSpecificRegistrationByCustomer_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(11)
    public void cancelSpecificRegistrationByOwner_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(12)
    public void cancelSpecificRegistrationByOwner_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/cancel",
                HttpMethod.POST,
                entity,
                Boolean.class,
                "INVALID");

        assertThrows(RestClientException.class, () -> {
            ResponseEntity<Boolean> registrationGOT2 = client.exchange(
                    "/registrations/{registration_id}/cancel",
                    HttpMethod.POST,
                    entity,
                    Boolean.class,
                    this.registration1ID);
        });


        assertEquals(HttpStatus.OK, registrationGOT1.getStatusCode());
        assertEquals(false, registrationGOT1.getBody());

    }

    @Test
    @Order(13)
    public void cancelSpecificRegistrationByInstructor_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(instructorEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(14)
    public void cancelSpecificRegistrationByCustomer_Valid() {

        Registration registration2 = new Registration("ID2", 4, customer2, course2);
        registration2.setCustomer(customer2);
        registration2.setCourse(course2);
        registrationRepository.save(registration2);

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(15)
    public void cancelSpecificRegistrationByCustomer_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(16)
    public void updateRegistrationByOwnerAndInstructor_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String ownerAuthentication1 = authenticationService.issueTokenWithEmail(instructor1Email);
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setBearerAuth(ownerAuthentication1);
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

    @Test
    @Order(17)
    public void updateRegistrationByCustomer_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customer1Email);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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


    @Test
    @Order(18)
    public void updateRegistrationByCustomer_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(19)
    public void getInvoicesByOwner_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(20)
    public void getInvoicesByOwner_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(21)
    public void getInvoicesByInstructor_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(instructorEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<ArrayList> registrationGOT1 = client.exchange(
                "/registrations/{registration_id}/invoices",
                HttpMethod.GET,
                entity,
                ArrayList.class, this.registration1ID);

        assertEquals(HttpStatus.BAD_REQUEST, registrationGOT1.getStatusCode());
    }

    @Test
    @Order(22)
    public void getInvoicesByCustomer_Valid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    @Test
    @Order(23)
    public void getInvoicesByCustomer_Invalid() {

        String ownerAuthentication = authenticationService.issueTokenWithEmail(customer1Email);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
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

    public static void createPerson(Person person, String email, String phoneNumber,String name,String password){
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }


}

package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CreateInvoiceDTO;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InvoiceIntegrationTests {
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


    private final String ownerEmail="owner34@gmail.com";
    private final String instructorEmail="instructor54@gmail.com";
    private final String customerEmail="customer65@gmail.com";

    @BeforeAll
    public void set_up(){
        String personPassword = "password";
        String personName = "Dave";
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        String sportCenterName = "HealthPlus";
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
    public void getAllInvoicesAsCustomerAndOwner(){
        // Set up
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<CourseDTO> request = new HttpEntity<>(headers);
        ResponseEntity<List<InvoiceDTO>> response = client.exchange(
            "/invoices",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Set up
        String ownerAuthentication= authenticationService.issueTokenWithEmail(ownerEmail);

        // Act
        headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        request = new HttpEntity<>(headers);
        response = client.exchange(
            "/invoices",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(2)
    public void getAllInvoicesAsInstructor(){
        // Set up
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<CourseDTO> request = new HttpEntity<>(headers);
        ResponseEntity<HTTPDTO> response = client.exchange(
            "/invoices",
            HttpMethod.GET,
            request,
            HTTPDTO.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void createInvoiceAsOwner(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        Registration registration = new Registration();
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        CreateInvoiceDTO createInvoiceDTO = new CreateInvoiceDTO();
        createInvoiceDTO.setRegistrationId(registration.getId());
        createInvoiceDTO.setAmount(100);
        HttpEntity<CreateInvoiceDTO> request = new HttpEntity<>(createInvoiceDTO, headers);
        ResponseEntity<?> response = client.exchange(
            "/invoices",
            HttpMethod.POST,
            request,
            InvoiceDTO.class
        );

        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        InvoiceDTO invoiceDTO = (InvoiceDTO) response.getBody();
        assertNotNull(invoiceDTO);
        assertEquals(100.0, invoiceDTO.getAmount());
        assertEquals(registration.getId(), invoiceDTO.getRegistrationId());
        assertEquals(Invoice.Status.Open.toString(), invoiceDTO.getStatus());
        assertEquals(invoiceDTO.getCustomerId(), customer.getId());

    }

    @Test
    @Order(4)
    public void createInvoiceAsCustomer(){
        // Set up
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);
        Registration registration = new Registration();
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        CreateInvoiceDTO createInvoiceDTO = new CreateInvoiceDTO();
        createInvoiceDTO.setRegistrationId(registration.getId());
        createInvoiceDTO.setAmount(100);
        HttpEntity<CreateInvoiceDTO> request = new HttpEntity<>(createInvoiceDTO, headers);
        ResponseEntity<String> response = client.exchange(
            "/invoices",
            HttpMethod.POST,
            request,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @Order(5)
    public void updateInvoiceStatusAsCustomer(){
        // Set up
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        Registration registration = new Registration();
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        Invoice invoice = getSampleInvoice(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = client.exchange(
                "/invoices/" + invoice.getId() + "/status?status=Completed",
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());


    }

    @Test
    @Order(6)
    public void getInvoiceAsCustomer_customer_owns_invoice(){
        // Set up
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);
        Registration registration = new Registration();
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        Invoice invoice = getSampleInvoice(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<InvoiceDTO> response = client.exchange(
            "/invoices/" + invoice.getId(),
            HttpMethod.GET,
            request,
            InvoiceDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        InvoiceDTO invoiceDTO = response.getBody();
        assertNotNull(invoiceDTO);
        assertEquals(100.0, invoiceDTO.getAmount());
        assertEquals(registration.getId(), invoiceDTO.getRegistrationId());
        assertEquals(Invoice.Status.Open.toString(), invoiceDTO.getStatus());
        assertEquals(invoiceDTO.getCustomerId(), customer.getId());
    }

    @Test
    @Order(7)
    public void getInvoiceAsCustomer_customer_does_not_own_invoice(){

        Customer customer2 = new Customer();
        customer2.setEmail("customer2@example.com");
        customer2.setName("Customer 2");
        customer2.setPassword("password");
        customer2.setPhoneNumber("123-456-7899");
        personRepository.save(customer2);
        // Set up
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);
        Registration registration = new Registration();
        registration.setCustomer(customer2);
        registrationRepository.save(registration);
        Invoice invoice = getSampleInvoice(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = client.exchange(
            "/invoices/" + invoice.getId(),
            HttpMethod.GET,
            request,
            String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void getInvoiceAsOwner(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        Registration registration = new Registration();
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        Invoice invoice = getSampleInvoice(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<InvoiceDTO> response = client.exchange(
            "/invoices/" + invoice.getId(),
            HttpMethod.GET,
            request,
            InvoiceDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        InvoiceDTO invoiceDTO = response.getBody();
        assertNotNull(invoiceDTO);
        assertEquals(100.0, invoiceDTO.getAmount());
        assertEquals(registration.getId(), invoiceDTO.getRegistrationId());
        assertEquals(Invoice.Status.Open.toString(), invoiceDTO.getStatus());
        assertEquals(invoiceDTO.getCustomerId(), customer.getId());
    }

    @Test
    @Order(9)
    public void getInvoiceAsInstructor(){
        // Set up
        String instructorAuthentication = authenticationService.issueTokenWithEmail(instructorEmail);
        Registration registration = new Registration();
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        Invoice invoice = getSampleInvoice(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = client.exchange(
            "/invoices/" + invoice.getId(),
            HttpMethod.GET,
            request,
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(10)
    public void getInvoiceAsCustomer_invoice_does_not_exist(){
        // Set up
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = client.exchange(
            "/invoices/123456",
            HttpMethod.GET,
            request,
            String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // update amount
    @Test
    @Order(11)
    public void updateInvoiceAmountAsOwner(){
        // Set up
        String ownerAuthentication = authenticationService.issueTokenWithEmail(ownerEmail);
        Registration registration = new Registration();
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        Invoice invoice = getSampleInvoice(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<?> response = client.exchange(
            "/invoices/" + invoice.getId() + "/amount?amount=200",
            HttpMethod.PUT,
            request,
            String.class
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Invoice updatedInvoice = invoiceRepository.findInvoiceById(invoice.getId());
        assertEquals(200, updatedInvoice.getAmount());
    }

    // update amount
    @Test
    @Order(12)
    public void updateInvoiceAmountAsCustomer(){
        // Set up
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail);
        Registration registration = new Registration();
        Customer customer = (Customer) personRepository.findPersonByEmail(customerEmail);
        registration.setCustomer(customer);
        registrationRepository.save(registration);
        Invoice invoice = getSampleInvoice(registration);
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = client.exchange(
            "/invoices/" + invoice.getId() + "/amount?amount=200",
            HttpMethod.PUT,
            request,
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

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

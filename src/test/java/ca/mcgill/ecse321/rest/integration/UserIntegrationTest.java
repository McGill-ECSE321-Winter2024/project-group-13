package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.*;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthenticationService authenticationService;


    private final String sportCenterName="iamHealthPlus";

    private final String sportCenterAddress="1234 Rue Saint Catherine";
    private final String ownerEmail="iamtheowner@gmail.com";
    private final String instructorEmail1="iaminstructor1@gmail.com";
    private final String instructorEmail2="iaminstructor2@gmail.com";
    private final String instructorEmail3="iaminstructor3@gmail.com";
    private final String customerEmail1="iamcustomer1@gmail.com";
    private final String customerEmail2="iamcustomer2@gmail.com";
    private final String customerEmail3="iamcustomer3@gmail.com";

    private final InstructorDTO instructor4DTO = new InstructorDTO();


    @BeforeAll
    public void set_up(){
        clearRepositories();
        String personName = "iamPerson";
        String personPassword = "iampersonPassword";
        String instructor1Name = "iamInstructor1";
        String instructor2Name = "iamInstructor2";
        String instructor3Name = "iamInstructor3";
        String instructor1Password = "iaminstructor1Password";
        String instructor2Password = "iaminstructor2Password";
        String instructor3Password = "iaminstructor3Password";
        String customer1Name = "iamCustomer1";
        String customer2Name = "iamCustomer2";
        String customer3Name = "iamCustomer3";
        String customer1Password = "iamcustomer1Password";
        String customer2Password = "iamcustomer2Password";
        String customer3Password = "iamcustomer3Password";
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor1 = new Instructor();
        Instructor instructor2 = new Instructor();
        Instructor instructor3 = new Instructor();
        Customer customer1= new Customer();
        Customer customer2= new Customer();
        Customer customer3= new Customer();
        sportCenter.setName(sportCenterName);
        owner.setSportCenter(sportCenter);
        instructor1.setSportCenter(sportCenter);
        instructor2.setSportCenter(sportCenter);
        instructor3.setSportCenter(sportCenter);
        customer1.setSportCenter(sportCenter);
        customer2.setSportCenter(sportCenter);
        customer3.setSportCenter(sportCenter);
        createPerson(owner,ownerEmail,"123-456-7890",personName,personPassword);
        createPerson(instructor1,instructorEmail1,"456-123-7890",instructor1Name,instructor1Password);
        createPerson(instructor2,instructorEmail2,"789-123-4560",instructor2Name,instructor2Password);
        createPerson(instructor3,instructorEmail3,"789-456-1230",instructor3Name,instructor3Password);
        createPerson(customer1,customerEmail1,"789-456-0123",customer1Name,customer1Password);
        createPerson(customer2,customerEmail2,"789-546-0123",customer2Name,customer2Password);
        createPerson(customer3,customerEmail3,"978-456-0123",customer3Name,customer3Password);

        personRepository.save(owner);
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        instructorRepository.save(instructor3);
        sportCenterRepository.save(sportCenter);

        instructor4DTO.setName("iamInstructor4");
        instructor4DTO.setEmail("instructor4@gmail.com");
        instructor4DTO.setPhoneNumber("123-456-7980");
        instructor4DTO.setPassword("iaminstructor4Password");
        instructor4DTO.setSportCenterId(sportCenter.getId());
    }
    @AfterAll
    public void clearDatabase() {
        clearRepositories();
    }

    @Test
    @Order(1)
    public void testGetAllInstructorsAsOwner(){
        String ownerAuthentication= authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<InstructorDTO[]> response = client.exchange("/instructors", HttpMethod.GET, request, InstructorDTO[].class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<InstructorDTO> instructorsDTOs = Arrays.asList(response.getBody());
        assertNotNull(instructorsDTOs);
        assertEquals(instructorsDTOs.size(),3);
        List<Instructor> instructors = (List<Instructor>) instructorRepository.findAll();
        for(int i = 0; i < 3; i++) {
            InstructorDTO instructorDTO = instructorsDTOs.get(i);
            InstructorDTO instructorDTO1 = new InstructorDTO(instructors.get(i));
            assertEquals(instructorDTO,instructorDTO1);
        }
    }

    @Test
    @Order(2)
    public void testGetAllInstructorsAsInstructor(){
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<InstructorDTO[]> response = client.exchange("/instructors", HttpMethod.GET, request, InstructorDTO[].class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<InstructorDTO> instructorsDTOs = Arrays.asList(response.getBody());
        assertNotNull(instructorsDTOs);
        assertEquals(instructorsDTOs.size(),3);
        List<Instructor> instructors = (List<Instructor>) instructorRepository.findAll();
        for(int i = 0; i < 3; i++) {
            InstructorDTO instructorDTO = instructorsDTOs.get(i);
            InstructorDTO instructorDTO1 = new InstructorDTO(instructors.get(i));
            assertEquals(instructorDTO,instructorDTO1);
        }
    }


    @Test
    @Order(3)
    public void testGetAllInstructorsAsCustomer(){
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<InstructorDTO[]> response = client.exchange("/instructors", HttpMethod.GET, request, InstructorDTO[].class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<InstructorDTO> instructorsDTOs = Arrays.asList(response.getBody());
        assertNotNull(instructorsDTOs);
        assertEquals(instructorsDTOs.size(),3);
        List<Instructor> instructors = (List<Instructor>) instructorRepository.findAll();
        for(int i = 0; i < 3; i++) {
            InstructorDTO instructorDTO = instructorsDTOs.get(i);
            InstructorDTO instructorDTO1 = new InstructorDTO(instructors.get(i));
            assertEquals(instructorDTO,instructorDTO1);
        }
    }

    @Test
    @Order(4)
    public void testGetAllCustomersAsOwner(){
        String ownerAuthentication= authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = client.exchange("/customers", HttpMethod.GET, request, CustomerDTO[].class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<CustomerDTO> customerDTOS = Arrays.asList(response.getBody());
        assertNotNull(customerDTOS);
        assertEquals(customerDTOS.size(),3);
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        for(int i = 0; i < 3; i++) {
            CustomerDTO customerDTO = customerDTOS.get(i);
            CustomerDTO customerDTO1 = new CustomerDTO(customers.get(i));
            assertEquals(customerDTO,customerDTO1);
        }
    }

    @Test
    @Order(5)
    public void testGetAllCustomersAsInstructor(){
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = client.exchange("/customers", HttpMethod.GET, request, CustomerDTO[].class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<CustomerDTO> customerDTOS = Arrays.asList(response.getBody());
        assertNotNull(customerDTOS);
        assertEquals(customerDTOS.size(),3);
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        for(int i = 0; i < 3; i++) {
            CustomerDTO customerDTO = customerDTOS.get(i);
            CustomerDTO customerDTO1 = new CustomerDTO(customers.get(i));
            assertEquals(customerDTO,customerDTO1);
        }
    }
    @Test
    @Order(6)
    public void testGetAllCustomersAsCustomer(){
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = client.exchange("/customers", HttpMethod.GET, request, CustomerDTO[].class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertNull(response.getBody());
    }




    @Test
    @Order(7)
    public void testCreateInstructorAsInstructor(){
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);

        HttpEntity<InstructorDTO> request = new HttpEntity<>(instructor4DTO,headers);

        ResponseEntity<InstructorDTO> response = client.exchange("/instructors", HttpMethod.POST, request, InstructorDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Instructor instructorDB = instructorRepository.findInstructorByName("iamInstructor4");
        assertNull(instructorDB);
    }

    @Test
    @Order(8)
    public void testCreateInstructorAsCustomer(){
        String customerAuthentication = authenticationService.issueTokenWithEmail(customerEmail1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);

        HttpEntity<InstructorDTO> request = new HttpEntity<>(instructor4DTO,headers);

        ResponseEntity<InstructorDTO> response = client.exchange("/instructors", HttpMethod.POST, request, InstructorDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        Instructor instructorDB = instructorRepository.findInstructorByName("iamInstructor4");
        assertNull(instructorDB);
    }
    @Test
    @Order(9)
    public void testCreateInstructorAsOwner(){
        String ownerAuthentication= authenticationService.issueTokenWithEmail(ownerEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);

        SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();

        HttpEntity<InstructorDTO> request = new HttpEntity<>(instructor4DTO,headers);

        ResponseEntity<InstructorDTO> response = client.exchange("/instructors", HttpMethod.POST, request, InstructorDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Instructor instructorDB = instructorRepository.findInstructorByName("iamInstructor4");
        assertNotNull(instructorDB);
        InstructorDTO instructorDBDTO = new InstructorDTO(instructorDB);
        assertNotNull(instructorDB.getId());
        assertEquals(instructorDBDTO.getName(),instructor4DTO.getName());
        assertEquals(instructorDBDTO.getEmail(),instructor4DTO.getEmail());
        assertEquals(instructorDBDTO.getPhoneNumber(),instructor4DTO.getPhoneNumber());
        assertEquals(instructorDBDTO.getPassword(),instructor4DTO.getPassword());
        assertEquals(instructorDBDTO.getSportCenterId(),instructor4DTO.getSportCenterId());
    }

    private void clearRepositories() {
        personRepository.deleteAll();
        instructorRepository.deleteAll();
        customerRepository.deleteAll();
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

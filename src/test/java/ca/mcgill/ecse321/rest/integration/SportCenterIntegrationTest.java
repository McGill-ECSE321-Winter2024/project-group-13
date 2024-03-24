package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.dto.SportCenterDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Time;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportCenterIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private AuthenticationService authenticationService;


    private final String sportCenterName="iamHealthPlus";

    private final String sportCenterAddress="1234 Rue Saint Catherine";
    private final String ownerEmail="iamowner@gmail.com";
    private final String instructorEmail="iaminstructor@gmail.com";
    private final String customerEmail="iamcustomer@gmail.com";

    private String scheduleId;

    @BeforeAll
    public void set_up(){
        clearRepositories();
        String personPassword = "iampassword";
        String personName = "iamDave";
        SportCenter sportCenter=new SportCenter();
        Owner owner= new Owner();
        Instructor instructor = new Instructor();
        Customer customer= new Customer();
        sportCenter.setName(sportCenterName);
        sportCenter.setAddress(sportCenterAddress);
        Schedule schedule = new Schedule(new Time(9,0,0),new Time(21,0,0));
        sportCenter.setSchedule(schedule);
        owner.setSportCenter(sportCenter);
        instructor.setSportCenter(sportCenter);
        customer.setSportCenter(sportCenter);
        createPerson(owner,ownerEmail,"123-456-7890",personName,personPassword);
        createPerson(instructor,instructorEmail,"456-123-7890",personName,personPassword);
        createPerson(customer,customerEmail,"789-456-0123",personName,personPassword);

        scheduleRepository.save(schedule);
        personRepository.save(owner);
        personRepository.save(customer);
        personRepository.save(instructor);
        sportCenterRepository.save(sportCenter);
        scheduleId=schedule.getId();
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
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter", HttpMethod.GET, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(sportCenterDTO,response.getBody());
    }

    @Test
    @Order(2)
    public void testUpdateSportCenterNameAsInstructor(){
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail);
        String newSportCenterName="FitnessPlus";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<String> request = new HttpEntity<>(newSportCenterName,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/name", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);

        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(newSportCenterName);
        assertNull(sportCenter);
        sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        assertNotNull(sportCenter);
        assertNotEquals(sportCenter.getName(),newSportCenterName);
        assertEquals(sportCenter.getName(),sportCenterName);
    }

    @Test
    @Order(3)
    public void testUpdateSportCenterNameAsCustomer(){
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail);
        String newSportCenterName="FitnessPlus";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(newSportCenterName,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/name", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);

        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(newSportCenterName);
        assertNull(sportCenter);
        sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        assertNotNull(sportCenter);
        assertNotEquals(sportCenter.getName(),newSportCenterName);
        assertEquals(sportCenter.getName(),sportCenterName);
    }

    @Test
    @Order(4)
    public void testUpdateSportCenterNameAsOwner(){
        String ownerAuthentication= authenticationService.issueTokenWithEmail(ownerEmail);
        String newSportCenterName="FitnessPlus";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(newSportCenterName,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/name", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);

        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(newSportCenterName);
        assertNotNull(sportCenter);
        assertEquals(sportCenter.getName(),newSportCenterName);
        sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        assertNull(sportCenter);

    }

    @Test
    @Order(5)
    public void testUpdateSportCenterAddressAsCustomer(){
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail);
        String newSportCenterAddress="1234 Rue De La Montagne";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(newSportCenterAddress,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/address", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);

        SportCenter sportCenter=sportCenterRepository.findSportCenterByIdNotNull();
        assertNotNull(sportCenter);
        assertEquals(sportCenter.getAddress(),sportCenterAddress);
        assertNotEquals(sportCenter.getAddress(),newSportCenterAddress);

    }
    @Test
    @Order(6)
    public void testUpdateSportCenterAddressAsInstructor(){
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail);
        String newSportCenterAddress="1234 Rue De La Montagne";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<String> request = new HttpEntity<>(newSportCenterAddress,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/address", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);

        SportCenter sportCenter=sportCenterRepository.findSportCenterByIdNotNull();
        assertNotNull(sportCenter);
        assertEquals(sportCenter.getAddress(),sportCenterAddress);
        assertNotEquals(sportCenter.getAddress(),newSportCenterAddress);
    }



    @Test
    @Order(7)
    public void testUpdateSportCenterAddressAsOwner(){
        String ownerAuthentication= authenticationService.issueTokenWithEmail(ownerEmail);
        String newSportCenterAddress="1234 Rue De La Montagne";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<String> request = new HttpEntity<>(newSportCenterAddress,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/address", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
        SportCenter sportCenter=sportCenterRepository.findSportCenterByIdNotNull();
        assertNotNull(sportCenter);
        assertEquals(sportCenter.getAddress(),newSportCenterAddress);
    }


    @Test
    @Order(8)
    public void testUpdateSportCenterScheduleAsCustomer(){
        String customerAuthentication= authenticationService.issueTokenWithEmail(instructorEmail);
        Schedule newSchedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        //scheduleRepository.save(newSchedule);
        ScheduleDTO scheduleDTO = new ScheduleDTO(newSchedule);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerAuthentication);
        HttpEntity<ScheduleDTO> request = new HttpEntity<>(scheduleDTO,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/opening-hours", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        SportCenter sportCenter=sportCenterRepository.findSportCenterByIdNotNull();
        assertNotNull(sportCenter);
        assertEquals(sportCenter.getSchedule().getId(),scheduleId);
        Schedule oldSchedule = scheduleRepository.findScheduleById(scheduleId);
        ScheduleDTO oldScheduleDTO = new ScheduleDTO(oldSchedule);
        assertScheduleEquality(sportCenter, oldScheduleDTO);
    }
    @Test
    @Order(9)
    public void testUpdateSportCenterScheduleAsInstructor(){
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail);
        Schedule newSchedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        //scheduleRepository.save(newSchedule);
        ScheduleDTO scheduleDTO = new ScheduleDTO(newSchedule);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<ScheduleDTO> request = new HttpEntity<>(scheduleDTO,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/opening-hours", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        SportCenter sportCenter=sportCenterRepository.findSportCenterByIdNotNull();
        assertNotNull(sportCenter);
        assertEquals(sportCenter.getSchedule().getId(),scheduleId);
        Schedule oldSchedule = scheduleRepository.findScheduleById(scheduleId);
        ScheduleDTO oldScheduleDTO = new ScheduleDTO(oldSchedule);
        assertScheduleEquality(sportCenter, oldScheduleDTO);
    }
    @Test
    @Order(10)
    public void testUpdateSportCenterScheduleAsOwner(){
        String ownerAuthentication= authenticationService.issueTokenWithEmail(ownerEmail);
        Schedule newSchedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        ScheduleDTO scheduleDTO = new ScheduleDTO(newSchedule);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ownerAuthentication);
        HttpEntity<ScheduleDTO> request = new HttpEntity<>(scheduleDTO,headers);

        ResponseEntity<SportCenterDTO> response = client.exchange("/sportcenter/opening-hours", HttpMethod.PUT, request, SportCenterDTO.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
        SportCenter sportCenter=sportCenterRepository.findSportCenterByIdNotNull();
        assertNotNull(sportCenter);
        assertScheduleEquality(sportCenter, scheduleDTO);
        assertNotEquals(sportCenter.getSchedule().getId(),scheduleId);
    }

    private void assertScheduleEquality(SportCenter sportCenter, ScheduleDTO scheduleDTO){
        assertEquals(sportCenter.getSchedule().getMondayStart().toString(),scheduleDTO.getMondayStart());
        assertEquals(sportCenter.getSchedule().getMondayEnd().toString(),scheduleDTO.getMondayEnd());
        assertEquals(sportCenter.getSchedule().getTuesdayStart().toString(),scheduleDTO.getTuesdayStart());
        assertEquals(sportCenter.getSchedule().getTuesdayEnd().toString(),scheduleDTO.getTuesdayEnd());
        assertEquals(sportCenter.getSchedule().getWednesdayStart().toString(),scheduleDTO.getWednesdayStart());
        assertEquals(sportCenter.getSchedule().getWednesdayEnd().toString(),scheduleDTO.getWednesdayEnd());
        assertEquals(sportCenter.getSchedule().getThursdayStart().toString(),scheduleDTO.getThursdayStart());
        assertEquals(sportCenter.getSchedule().getThursdayEnd().toString(),scheduleDTO.getThursdayEnd());
        assertEquals(sportCenter.getSchedule().getFridayStart().toString(),scheduleDTO.getFridayStart());
        assertEquals(sportCenter.getSchedule().getFridayEnd().toString(),scheduleDTO.getFridayEnd());
        assertEquals(sportCenter.getSchedule().getSaturdayStart().toString(),scheduleDTO.getSaturdayStart());
        assertEquals(sportCenter.getSchedule().getSaturdayEnd().toString(),scheduleDTO.getSaturdayEnd());
        assertEquals(sportCenter.getSchedule().getSundayStart().toString(),scheduleDTO.getSundayStart());
        assertEquals(sportCenter.getSchedule().getSundayEnd().toString(),scheduleDTO.getSundayEnd());
        assertNotNull(sportCenter.getSchedule().getId());
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

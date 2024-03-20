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
import org.springframework.http.*;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseIntegrationTests {
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

    @BeforeAll
    public void set_up(){
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
        roomRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void createInvalidCourseTest(){
        // Set up
        String instructorAuthentication= authenticationService.issueTokenWithEmail(instructorEmail);
        String customerAuthentication= authenticationService.issueTokenWithEmail(customerEmail);
        String name = "Yoga";
        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        CourseDTO requestBody = new CourseDTO("",sportCenter.getId());
        CourseDTO requestBody1 = new CourseDTO(name,"");
        CourseDTO requestBody2 = new CourseDTO(name,sportCenter.getId());

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<CourseDTO> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<HTTPDTO> response = client.postForEntity("/courses", request,HTTPDTO.class);

        HttpEntity<CourseDTO> request1 = new HttpEntity<>(requestBody1, headers);
        ResponseEntity<HTTPDTO> response1 = client.postForEntity("/courses", request1,HTTPDTO.class);

        headers.setBearerAuth(customerAuthentication);
        HttpEntity<CourseDTO> request2 = new HttpEntity<>(requestBody2, headers);
        ResponseEntity<HTTPDTO> response2 = client.postForEntity("/courses", request2, HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Course requires name to be created", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals("Invalid sport's center id", response1.getBody().getMessage());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
        assertEquals("Must be an owner or instructor", response2.getBody().getMessage());
    }
    @Test
    @Order(2)
    public void createCourseTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
        String name = "Yoga";
        SportCenter sportCenter=sportCenterRepository.findSportCenterByName(sportCenterName);
        CourseDTO requestBody = new CourseDTO(name,sportCenter.getId());

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<CourseDTO> request = new HttpEntity<>(requestBody, headers);
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

    @Test
    @Order(3)
    public void approveCourseTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Yoga"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/approve";

        // Act
        HttpEntity<CourseDTO> request = new HttpEntity<>(headers);
        ResponseEntity<HTTPDTO> response = client.postForEntity(url, request,HTTPDTO.class);

        // Assert
        assertNotNull(response);
        CourseDTO courseDTO=new CourseDTO(courseRepository.findCourseByName("Yoga"));
        assertNotNull(courseDTO);
        assertEquals("Yoga", courseDTO.getName());
        assertEquals(course.getId(), courseDTO.getId());
        assertEquals("Approved", courseDTO.getCourseState());
        assertEquals("Course approved", response.getBody().getMessage());

    }

    @Test
    @Order(4)
    public void updateCourseNameTest(){
    // Set up
    String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
    CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Yoga"));
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(authentication);
    assertNotNull(course);
    String url = "/courses/" + course.getId() + "/name";
    String newName= "Spin";

    // Act
    HttpEntity<String> request = new HttpEntity<>(newName,headers);
    ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

    assertNotNull(response);
    CourseDTO updatedCourse = new CourseDTO(courseRepository.findCourseByName("Yoga"));
    assertNotNull(updatedCourse);
    assertNull(updatedCourse.getName());
    assertNull(updatedCourse.getId());
    updatedCourse = new CourseDTO(courseRepository.findCourseByName("Spin"));
    assertNotNull(updatedCourse);
    assertEquals(newName, updatedCourse.getName());
    assertEquals(course.getId(), updatedCourse.getId());
    assertEquals("Course name changed", response.getBody().getMessage());
    }
    @Test
    @Order(5)
    public void updateCourseDescriptionTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/description";
        String description= "Intense spin class";

        // Act
        HttpEntity<String> request = new HttpEntity<>(description,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(description, updatedCourse.getDescription());
        assertEquals("Course description changed", response.getBody().getMessage());
    }
    @Test
    @Order(6)
    public void updateCourseLevelTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/level";
        String level= "Advanced";

        // Act
        HttpEntity<String> request = new HttpEntity<>(level,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(level, updatedCourse.getLevel());
        assertEquals("Course level changed", response.getBody().getMessage());
    }
    @Test
    @Order(7)
    public void updateCourseRateTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/rate";
        Double hourlyRateAmount= 12.45;

        // Act
        HttpEntity<Double> request = new HttpEntity<>(hourlyRateAmount,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(hourlyRateAmount, updatedCourse.getHourlyRateAmount());
        assertEquals("Course rate changed", response.getBody().getMessage());
    }
    @Test
    @Order(8)
    public void updateCourseStartDateTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/startDate";
        Timestamp courseStartDate = new Timestamp(2023, 1, 1, 6, 15, 0, 0);


        // Act
        HttpEntity<Timestamp> request = new HttpEntity<>(courseStartDate,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(courseStartDate, updatedCourse.getCourseStartDate());
        assertEquals("Course start date changed", response.getBody().getMessage());
    }
    @Test
    @Order(9)
    public void updateCourseEndDateTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/endDate";
        Timestamp courseEndDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);


        // Act
        HttpEntity<Timestamp> request = new HttpEntity<>(courseEndDate,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(courseEndDate, updatedCourse.getCourseEndDate());
        assertEquals("Course end date changed", response.getBody().getMessage());
    }
//    @Test
//    @Order(10)
//    public void updateCourseRoomTest(){
//        // Set up
//        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
//        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authentication);
//        assertNotNull(course);
//        String url = "/courses/" + course.getId() + "/room";
//        Room room = new Room();
//        room.setSportCenter(sportCenterRepository.findSportCenterByName(sportCenterName));
//        room.setRoomName("Spin room");
//        String roomID="roomID";
//        room.setId(roomID);
//        System.out.println(roomID);
//        roomRepository.save(room);
//        assertNotNull(roomRepository.findRoomById(room.getId()));
//        // Act
//        HttpEntity<String> request = new HttpEntity<>(roomID,headers);
//        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);
//
//        assertNotNull(response);
//        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
//        assertNotNull(updatedCourse);
//        assertEquals("Spin", updatedCourse.getName());
//        assertEquals(course.getId(), updatedCourse.getId());
//        assertEquals(roomID, updatedCourse.getRoom());
//        assertEquals("Course room changed", response.getBody().getMessage());
//    }
    @Test
    @Order(11)
    public void updateCourseInstructorTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/instructor";
        Person instructor = personRepository.findPersonByEmail(instructorEmail);

        // Act
        HttpEntity<String> request = new HttpEntity<>(instructor.getId(),headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(instructor.getId(), updatedCourse.getInstructor());
        assertEquals("Course instructor changed", response.getBody().getMessage());
    }
    @Test
    @Order(12)
    public void updateCourseScheduleTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/schedule";
        Schedule schedule= new Schedule();
        schedule.setId("ScheduleID");
        scheduleRepository.save(schedule);

        // Act
        HttpEntity<String> request = new HttpEntity<>(schedule.getId(),headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(schedule.getId(), updatedCourse.getSchedule());
        assertEquals("Course schedule changed", response.getBody().getMessage());
    }
    @Test
    @Order(13)
    public void deleteCourseTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() ;

        // Act
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.DELETE, request,HTTPDTO.class);

        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertNull(updatedCourse.getName());
        assertNull( updatedCourse.getId());
        assertEquals("Course deleted", response.getBody().getMessage());
    }


  public static void createPerson(
      Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

}

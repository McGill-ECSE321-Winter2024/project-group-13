package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseIntegrationTests {
    @Autowired private TestRestTemplate client;
    @Autowired private CourseRepository courseRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private SportCenterRepository sportCenterRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private AuthenticationService authenticationService;
    @Autowired private CourseSessionRepository courseSessionRepository;

    private final String sportCenterName="HealthPlus1";
    private final String ownerEmail="owner1234@gmail.com";
    private final String instructorEmail="instructor1234567@gmail.com";
    private final String customerEmail="customer1234567@gmail.com";

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
        createPerson(owner,ownerEmail,"223-456-7892",personName,personPassword);
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
        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(instructorAuthentication);
        HttpEntity<String> request = new HttpEntity<>("", headers);
        ResponseEntity<HTTPDTO> response = client.postForEntity("/courses", request,HTTPDTO.class);

        headers.setBearerAuth(customerAuthentication);
        HttpEntity<String> request2 = new HttpEntity<>(name, headers);
        ResponseEntity<HTTPDTO> response2 = client.postForEntity("/courses", request2, HTTPDTO.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response2);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Course requires name to be created", response.getBody().getMessage());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
        assertEquals("Must be an owner or instructor", response2.getBody().getMessage());
    }
    @Test
    @Order(2)
    public void createCourseTest(){
        // Set up
        String authentication= authenticationService.issueTokenWithEmail(ownerEmail);
        String name = "Yoga";


        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        HttpEntity<String> request = new HttpEntity<>(name, headers);
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
        HttpEntity<String> request = new HttpEntity<>(headers);
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
    String invalidName="";

    // Act
    HttpEntity<String> request = new HttpEntity<>(newName,headers);
    ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);
    HttpEntity<String> requestInvalid = new HttpEntity<>(invalidName,headers);
    ResponseEntity<HTTPDTO> responseInvalid = client.exchange(url, HttpMethod.PUT, requestInvalid,HTTPDTO.class);

    assertNotNull(response);
    assertNotNull(responseInvalid);
    assertNull(courseRepository.findCourseByName(invalidName));
    assertEquals("Requires valid name", responseInvalid.getBody().getMessage());
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
        String invalidDescription="";


        // Act
        HttpEntity<String> request = new HttpEntity<>(description,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);
        HttpEntity<String> requestInvalid = new HttpEntity<>(invalidDescription,headers);
        ResponseEntity<HTTPDTO> responseInvalid = client.exchange(url, HttpMethod.PUT, requestInvalid,HTTPDTO.class);

        assertNotNull(response);
        assertNotNull(responseInvalid);
        assertEquals("Requires valid description", responseInvalid.getBody().getMessage());
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
        String emptyLevel="";
        String invalidLevel="Hello";

        // Act
        HttpEntity<String> request = new HttpEntity<>(level,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);
        HttpEntity<String> requestEmptyLevel = new HttpEntity<>(emptyLevel,headers);
        ResponseEntity<HTTPDTO> responseEmptyLevel = client.exchange(url, HttpMethod.PUT, requestEmptyLevel,HTTPDTO.class);
        HttpEntity<String> requestInvalidLevel = new HttpEntity<>(invalidLevel,headers);
        ResponseEntity<HTTPDTO> responseInvalid = client.exchange(url, HttpMethod.PUT, requestInvalidLevel,HTTPDTO.class);

        assertNotNull(responseInvalid);
        assertEquals("Requires valid level", responseInvalid.getBody().getMessage());
        assertNotNull(responseEmptyLevel);
        assertEquals("Requires valid level", responseEmptyLevel.getBody().getMessage());
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
        Double emptyhourlyRateAmount=null;
        Double invalidhourlyRateAmount=-1000.00;

        // Act
        HttpEntity<Double> request = new HttpEntity<>(hourlyRateAmount,headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);
        HttpEntity<Double> requestInvalid = new HttpEntity<>(emptyhourlyRateAmount,headers);
        ResponseEntity<HTTPDTO> responseInvalid = client.exchange(url, HttpMethod.PUT, requestInvalid,HTTPDTO.class);
        HttpEntity<Double> requestInvalidhourlyRateAmount = new HttpEntity<>(invalidhourlyRateAmount,headers);
        ResponseEntity<HTTPDTO> responseInvalidhourlyRateAmount = client.exchange(url, HttpMethod.PUT, requestInvalidhourlyRateAmount,HTTPDTO.class);

        assertNotNull(response);
        assertNotNull(responseInvalid);
        assertNotNull(requestInvalidhourlyRateAmount);
        assertEquals("Requires valid hourly rate amount", responseInvalid.getBody().getMessage());
        assertEquals("Course rate must be a positive number", responseInvalidhourlyRateAmount.getBody().getMessage());
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
    @Test
    @Order(10)
    public void updateCourseRoomTest(){
        // Set up
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        CourseDTO course = new CourseDTO(courseRepository.findCourseByName("Spin"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        assertNotNull(course);
        String url = "/courses/" + course.getId() + "/room";
        Room room = new Room();
        room.setSportCenter(sportCenterRepository.findSportCenterByName(sportCenterName));
        room.setRoomName("Spin room");
        roomRepository.save(room);
        assertNotNull(roomRepository.findRoomById(room.getId()));

        // Act
        HttpEntity<String> request = new HttpEntity<>(room.getId(),headers);
        ResponseEntity<HTTPDTO> response = client.exchange(url, HttpMethod.PUT, request,HTTPDTO.class);
        HttpEntity<String> requestInvalid = new HttpEntity<>("123456",headers);
        ResponseEntity<HTTPDTO> responseInvalid = client.exchange(url, HttpMethod.PUT, requestInvalid,HTTPDTO.class);

        assertNotNull(responseInvalid);
        assertEquals("Room not found", responseInvalid.getBody().getMessage());
        assertNotNull(response);
        CourseDTO updatedCourse =new CourseDTO(courseRepository.findCourseByName("Spin"));
        assertNotNull(updatedCourse);
        assertEquals("Spin", updatedCourse.getName());
        assertEquals(course.getId(), updatedCourse.getId());
        assertEquals(room.getId(), updatedCourse.getRoom());
        assertEquals("Course room changed", response.getBody().getMessage());
    }
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
        HttpEntity<String> requestInvalid = new HttpEntity<>("123456",headers);
        ResponseEntity<HTTPDTO> responseInvalid = client.exchange(url, HttpMethod.PUT, requestInvalid,HTTPDTO.class);

        assertNotNull(responseInvalid);
        assertEquals("Instructor not found", responseInvalid.getBody().getMessage());
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
        HttpEntity<String> requestInvalid = new HttpEntity<>("123456",headers);
        ResponseEntity<HTTPDTO> responseInvalid = client.exchange(url, HttpMethod.PUT, requestInvalid,HTTPDTO.class);

        assertNotNull(responseInvalid);
        assertEquals("Schedule not found", responseInvalid.getBody().getMessage());
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


    @Test
    @Order(14)
    public void testAddScheduleToCourseSuccessfully() {
        // Setup
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setMondayStart("08:00:00");
        scheduleDTO.setMondayEnd("10:00:00");

        // Create a course to link
        Course newCourse = new Course();
        newCourse.setName("Aerobics");
        newCourse.setCourseStartDate(Timestamp.valueOf("2024-01-01 00:00:00"));
        newCourse.setCourseEndDate(Timestamp.valueOf("2024-01-31 23:59:59"));
        courseRepository.save(newCourse);

        // Act
        HttpEntity<ScheduleDTO> request = new HttpEntity<>(scheduleDTO, headers);
        ResponseEntity<HTTPDTO> response = client.exchange("/courses/" + newCourse.getId() + "/update-schedule-and-sessions", HttpMethod.PUT, request, HTTPDTO.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Schedule successfully added/updated and linked to the course", response.getBody().getMessage());

        // Calculate expected number of Mondays in the course duration
        LocalDate startDate = newCourse.getCourseStartDate().toLocalDateTime().toLocalDate();
        LocalDate endDate = newCourse.getCourseEndDate().toLocalDateTime().toLocalDate();
        long expectedSessionCount = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.MONDAY)
                .count();

        // Verify that sessions are generated
        List<CourseSession> sessions = courseSessionRepository.findCourseSessionsByCourse(newCourse);
        assertFalse(sessions.isEmpty());
        assertEquals(expectedSessionCount, sessions.size(), "Expected number of sessions does not match actual sessions created.");
    }

    @Test
    @Order(15)
    public void testAddScheduleWithInvalidTimeFormat() {
        // Setup
        String authentication = authenticationService.issueTokenWithEmail(ownerEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authentication);
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setMondayStart("invalid_time");

        // Create a course to link
        Course newCourse = new Course();
        newCourse.setName("Zumba");
        courseRepository.save(newCourse);

        // Act
        HttpEntity<ScheduleDTO> request = new HttpEntity<>(scheduleDTO, headers);
        ResponseEntity<HTTPDTO> response = client.exchange("/courses/" + newCourse.getId() + "/update-schedule-and-sessions", HttpMethod.PUT, request, HTTPDTO.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Invalid time format"));
    }


    public static void createPerson(
      Person person, String email, String phoneNumber, String name, String password) {
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
    }

}

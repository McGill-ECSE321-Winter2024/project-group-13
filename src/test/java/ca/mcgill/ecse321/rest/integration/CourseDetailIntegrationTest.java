package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Time;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // This is the key annotation
public class CourseDetailIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;

    @Autowired private CourseRepository courseRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private OwnerRepository ownerRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private SportCenterRepository sportCenterRepository;
    @Autowired private AuthenticationService authenticationService;

    private String ownerId;
    private String customerId;
    private String unapprovedCourseId;
    private String approvedCourseId;
    private Schedule scheduleForUnapprovedCourse;
    private Schedule scheduleForApprovedCourse;

    @BeforeAll
    public void setup() {
        // Clear the repositories before each test
        courseRepository.deleteAll();
        scheduleRepository.deleteAll();
        personRepository.deleteAll();
        customerRepository.deleteAll();
        sportCenterRepository.deleteAll();


        //Creating a sports center
        SportCenter sportCenter = new SportCenter();
        sportCenterRepository.save(sportCenter);

        //Create the customer and persist
        Customer customer = new Customer();
        customer.setName("Hamid");
        customer.setEmail("my-customer@mail.com");
        customer.setPassword("test");
        customer.setPhoneNumber("8198888888");
        customer.setSportCenter(sportCenter);
        customerRepository.save(customer);
        customerId = customer.getId();

        //Creating an owner
        Owner owner = new Owner();
        owner.setName("Boss");
        owner.setEmail("my-boss@mail.com");
        owner.setPassword("test");
        owner.setPhoneNumber("8198888888");
        owner.setSportCenter(sportCenter);
        ownerRepository.save(owner);
        ownerId = owner.getId();

        // Create and save the schedule for the unapproved course
        scheduleForUnapprovedCourse = new Schedule();
        scheduleForUnapprovedCourse.setSundayStart(new Time(new Calendar.Builder().setTimeOfDay(8, 0, 0).build().getTimeInMillis()));
        scheduleForUnapprovedCourse.setSundayEnd(new Time(new Calendar.Builder().setTimeOfDay(10, 0, 0).build().getTimeInMillis()));
        scheduleRepository.save(scheduleForUnapprovedCourse);

        // Create and save the unapproved course with the first schedule
        Course unapprovedCourse = new Course();
        unapprovedCourse.setName("Unapproved Test Course");
        unapprovedCourse.setCourseState(Course.CourseState.Inactive);
        unapprovedCourse.setSchedule(scheduleForUnapprovedCourse);
        unapprovedCourse = courseRepository.save(unapprovedCourse);
        unapprovedCourseId = unapprovedCourse.getId();

        // Create and save the schedule for the approved course
        scheduleForApprovedCourse = new Schedule();
        scheduleForApprovedCourse.setSundayStart(new Time(new Calendar.Builder().setTimeOfDay(11, 0, 0).build().getTimeInMillis()));
        scheduleForApprovedCourse.setSundayEnd(new Time(new Calendar.Builder().setTimeOfDay(13, 0, 0).build().getTimeInMillis()));
        scheduleRepository.save(scheduleForApprovedCourse);

        // Create and save the second course with the second schedule
        Course approvedCourse = new Course();
        approvedCourse.setName("Approved Test Course");
        approvedCourse.setCourseState(Course.CourseState.Approved);
        approvedCourse.setSchedule(scheduleForApprovedCourse);
        approvedCourse = courseRepository.save(approvedCourse);
        approvedCourseId = approvedCourse.getId();
    }

    @AfterAll
    public void teardown() {
        // Clean up the database after tests
        courseRepository.deleteAll();
        scheduleRepository.deleteAll();
        personRepository.deleteAll();
        customerRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    public void customerTriesToAccessTheScheduleOfAnApprovedCourse_ValidRequest() {
        //Issue token for customer
        String customerToken = authenticationService.issueToken(customerId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to your endpoint
        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(
                "/courses/{course_id}/schedule",
                HttpMethod.GET,
                entity,
                ScheduleDTO.class,
                approvedCourseId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForApprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");

    }

    @Test
    public void customerTriesToAccessScheduleOfUnapprovedCourse_ForbiddenRequest() {
        // Issue a token for the customer
        String customerToken = authenticationService.issueToken(customerId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to the endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(
                "/courses/{course_id}/schedule",
                HttpMethod.GET,
                entity,
                ScheduleDTO.class,
                unapprovedCourseId);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
        assertNull(response.getBody(), "Response body should be null because access is not allowed");
    }

    @Test
    public void ownerAccessesAnySchedule_ValidRequest() {
        String ownerToken = authenticationService.issueToken(ownerId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ownerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to the endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(
                "/courses/{course_id}/schedule",
                HttpMethod.GET,
                entity,
                ScheduleDTO.class,
                approvedCourseId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForApprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }

    @Test
    public void instructorAccessesOwnedCourseSchedule() {
        // Setup Instructor and issue token
        // Setup an unapproved course with the instructor as owner and a schedule
        // Make a GET request to your endpoint as the instructor
        // Assert that the instructor can access their own course's schedule
    }

    @Test
    public void instructorAccessesUnownedApprovedCourseSchedule() {
        // Setup Instructor and issue token
        // Setup an approved course with a different instructor and a schedule
        // Make a GET request to your endpoint as the instructor
        // Assert that the instructor can access an approved course that they do not own
    }

    @Test
    public void instructorDeniedAccessToUnownedUnapprovedCourseSchedule() {
        // Setup Instructor and issue token
        // Setup an unapproved course with a different instructor and a schedule
        // Make a GET request to your endpoint as the instructor
        // Assert that the instructor cannot access an unapproved course that they do not own
    }

    @Test
    public void customerAccessesApprovedCourseSchedule() {
        // Setup Customer and issue token
        // Setup an approved course with a schedule
        // Make a GET request to your endpoint as the customer
        // Assert that the customer can access an approved course's schedule
    }

    @Test
    public void customerDeniedAccessToUnapprovedCourseSchedule() {
        // Setup Customer and issue token
        // Setup an unapproved course with a schedule
        // Make a GET request to your endpoint as the customer
        // Assert that the customer cannot access an unapproved course's schedule
    }

    @Test
    public void tokenVerificationFailed() {
        // Issue an invalid token
        // Setup an approved course with a schedule
        // Make a GET request to your endpoint with the invalid token
        // Assert that the response status is UNAUTHORIZED
    }

    @Test
    public void courseNotFound() {
        // Issue token for any user
        // Use a non-existent courseId in the GET request to your endpoint
        // Assert that the response status is NOT_FOUND
    }



}

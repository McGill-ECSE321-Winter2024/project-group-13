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
    @Autowired private InstructorRepository instructorRepository;
    @Autowired private SportCenterRepository sportCenterRepository;
    @Autowired private AuthenticationService authenticationService;

    private String ownerId;
    private String customerId;
    private String instructorId;
    private String unapprovedCourseOwnedId;
    private String unapprovedCourseId;
    private String approvedCourseId;
    private Schedule scheduleForUnapprovedCourseOwned;
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
        sportCenter.setName("Gold's Gym");
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

        //Creating an owner and persist
        Owner owner = new Owner();
        owner.setName("Boss");
        owner.setEmail("my-boss@mail.com");
        owner.setPassword("test");
        owner.setPhoneNumber("8198808888");
        ownerRepository.save(owner);
        owner.setSportCenter(sportCenter);
        ownerRepository.save(owner);
        ownerId = owner.getId();

        //Create the customer and persist
        Instructor instructor = new Instructor();
        instructor.setName("Trainer");
        instructor.setEmail("my-instructor@mail.com");
        instructor.setPassword("test");
        instructor.setPhoneNumber("8198888889");
        instructor.setSportCenter(sportCenter);
        instructorRepository.save(instructor);
        instructorId = instructor.getId();

        // Create and save the schedule for the unapproved course Owned by Instructor
        scheduleForUnapprovedCourseOwned = new Schedule();
        scheduleForUnapprovedCourseOwned.setSundayStart(new Time(new Calendar.Builder().setTimeOfDay(8, 0, 0).build().getTimeInMillis()));
        scheduleForUnapprovedCourseOwned.setSundayEnd(new Time(new Calendar.Builder().setTimeOfDay(10, 0, 0).build().getTimeInMillis()));
        scheduleRepository.save(scheduleForUnapprovedCourseOwned);

        // Create and save the unapproved course with the first schedule. This course is also taught by an instructor
        Course unapprovedCourseOwned = new Course();
        unapprovedCourseOwned.setName("Unapproved Test Course Owned by Instructor");
        unapprovedCourseOwned.setCourseState(Course.CourseState.Inactive);
        unapprovedCourseOwned.setSchedule(scheduleForUnapprovedCourseOwned);
        unapprovedCourseOwned.setInstructor(instructor);
        unapprovedCourseOwned = courseRepository.save(unapprovedCourseOwned);
        unapprovedCourseOwnedId = unapprovedCourseOwned.getId();

        // Create and save the schedule for the unapproved course Owned by Instructor
        scheduleForUnapprovedCourse = new Schedule();
        scheduleForUnapprovedCourse.setSundayStart(new Time(new Calendar.Builder().setTimeOfDay(8, 0, 0).build().getTimeInMillis()));
        scheduleForUnapprovedCourse.setSundayEnd(new Time(new Calendar.Builder().setTimeOfDay(10, 0, 0).build().getTimeInMillis()));
        scheduleRepository.save(scheduleForUnapprovedCourse);

        // Create and save the unapproved course with the first schedule. This course is also taught by an instructor
        Course unapprovedCourse = new Course();
        unapprovedCourse.setName("Unapproved Test Course Not Owned by Anyone");
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
                unapprovedCourseOwnedId);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
        assertNull(response.getBody(), "Response body should be null because access is not allowed");
    }

    @Test
    public void ownerAccessesApprovedCourseSchedule_ValidRequest() {
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
    public void ownerAccessesUnapprovedCourseSchedule_ValidRequest() {
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
                unapprovedCourseId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForUnapprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }

    @Test
    public void instructorAccessesOwnedCourseSchedule_ValidRequest() {
        // Issue token for the instructor
        String instructorToken = authenticationService.issueToken(instructorId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to your endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(
                "/courses/{course_id}/schedule",
                HttpMethod.GET,
                entity,
                ScheduleDTO.class,
                unapprovedCourseOwnedId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForUnapprovedCourseOwned); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }

    @Test
    public void instructorAccessesUnownedApprovedCourseSchedule_ValidRequest() {
        // Issue token for the instructor
        String instructorToken = authenticationService.issueToken(instructorId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to your endpoint for the approved course's schedule
        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(
                "/courses/{course_id}/schedule",
                HttpMethod.GET,
                entity,
                ScheduleDTO.class,
                approvedCourseId); // `approvedCourseId` is for a course the instructor does not own

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForApprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }


    @Test
    public void instructorDeniedAccessToUnownedUnapprovedCourseSchedule_ForbiddenRequest() {
        // Issue a token for the instructor
        String instructorToken = authenticationService.issueToken(instructorId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
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
    public void tokenVerificationFailed() {
        // Issue an invalid token. Here, we just create a random string that doesn't correspond to any valid token.
        String invalidToken = "invalidToken12345";

        // Set up the Authorization header with the invalid token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + invalidToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to your endpoint with the invalid token. The choice of course doesn't matter here since the request should fail due to the invalid token, but we'll use the approvedCourseId for consistency.
        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(
                "/courses/{course_id}/schedule",
                HttpMethod.GET,
                entity,
                ScheduleDTO.class,
                approvedCourseId);

        // Assert that the response status is UNAUTHORIZED
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be UNAUTHORIZED");
        assertNull(response.getBody(), "Response body should be null because the token is invalid");
    }


    @Test
    public void courseNotFound() {
        // Issue token for an existing user, for simplicity we'll use the customer already setup
        String userToken = authenticationService.issueToken(customerId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Use a non-existent courseId in the GET request to your endpoint
        String nonExistentCourseId = "non-existent-course-id";
        ResponseEntity<ScheduleDTO> response = restTemplate.exchange(
                "/courses/{course_id}/schedule",
                HttpMethod.GET,
                entity,
                ScheduleDTO.class,
                nonExistentCourseId);

        // Assert that the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response status should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null because the course does not exist");
    }




}

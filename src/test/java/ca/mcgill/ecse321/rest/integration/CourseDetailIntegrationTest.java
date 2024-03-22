package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
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

import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private AuthenticationService authenticationService;

    private String ownerId;
    private String firstCustomerId;
    private String secondCustomerId;
    private String instructorId;
    private String unapprovedCourseOwnedId;
    private String unapprovedCourseId;
    private String approvedCourseId;
    private String courseWithCustomersId;
    private Schedule scheduleForUnapprovedCourseOwned;
    private Schedule scheduleForUnapprovedCourse;
    private Schedule scheduleForApprovedCourse;
//    private Schedule scheduleForCourseWithCustomers;

    @BeforeAll
    public void setup() {
        clearRepositories();
        SportCenter sportCenter = createAndSaveSportCenter("Gold's Gym");

        firstCustomerId = createAndSavePerson(Customer.class, "Hamid", "my-customer@mail.com", "test", "8198888888", sportCenter);
        secondCustomerId = createAndSavePerson(Customer.class, "Elias", "my-customer2@mail.com", "test", "8198884888", sportCenter);
        ownerId = createAndSavePerson(Owner.class, "Boss", "my-boss@mail.com", "test", "8198808888", sportCenter);
        instructorId = createAndSavePerson(Instructor.class, "Trainer", "my-instructor@mail.com", "test", "8198888889", sportCenter);

        createCourses();
        createRegistrations();

    }

    @AfterAll
    public void teardown() {
        // Clean up the database after tests
        clearRepositories();
    }

    private void clearRepositories() {
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        scheduleRepository.deleteAll();
        personRepository.deleteAll();
        customerRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    private void createCourses(){
        unapprovedCourseOwnedId = createAndSaveCourse("Unapproved Test Course Owned by Instructor", Course.CourseState.Inactive, 8, 10, instructorId);
        unapprovedCourseId = createAndSaveCourse("Unapproved Test Course Not Owned by Anyone", Course.CourseState.Inactive, 8, 10, null);
        approvedCourseId = createAndSaveCourse("Approved Test Course", Course.CourseState.Approved, 11, 13, null);
        courseWithCustomersId = createAndSaveCourse("Test Course With Customers", Course.CourseState.Approved, 11, 13, instructorId);

        scheduleForUnapprovedCourseOwned = courseRepository.findCourseById(unapprovedCourseOwnedId).getSchedule();
        scheduleForUnapprovedCourse = courseRepository.findCourseById(unapprovedCourseId).getSchedule();
        scheduleForApprovedCourse = courseRepository.findCourseById(approvedCourseId).getSchedule();
//        scheduleForCourseWithCustomers = courseRepository.findCourseById(courseWithCustomersId).getSchedule();
    }

    public void createRegistrations(){
        //Populate course with 2 customers
        Registration firstRegistration = new Registration();
        firstRegistration.setCustomer(customerRepository.findCustomerById(firstCustomerId));
        firstRegistration.setCourse(courseRepository.findCourseById(courseWithCustomersId));
        registrationRepository.save(firstRegistration);
        Registration secondRegistration = new Registration();
        secondRegistration.setCustomer(customerRepository.findCustomerById(secondCustomerId));
        secondRegistration.setCourse(courseRepository.findCourseById(courseWithCustomersId));
        registrationRepository.save(secondRegistration);
    }

    private SportCenter createAndSaveSportCenter(String name) {
        SportCenter sportCenter = new SportCenter();
        sportCenter.setName(name);
        return sportCenterRepository.save(sportCenter);
    }

    private <T extends Person> String createAndSavePerson(Class<T> type, String name, String email, String password, String phoneNumber, SportCenter sportCenter) {
        T person;
        try {
            person = type.getDeclaredConstructor().newInstance();

            person.setName(name);
            person.setEmail(email);
            person.setPassword(password);
            person.setPhoneNumber(phoneNumber);

            //After debuging it seems that the owner needs to be saved before associating with the sports center
            if(person instanceof Owner){
                personRepository.save(person);
            }
            // Check type and cast to call subclass-specific method
            if (person instanceof Instructor) {
                ((Instructor)person).setSportCenter(sportCenter);
            } else if (person instanceof Owner) {
                ((Owner)person).setSportCenter(sportCenter);
            } else if (person instanceof Customer) {
                ((Customer)person).setSportCenter(sportCenter);
            }
            personRepository.save(person);
            return person.getId();

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String createAndSaveCourse(String name, Course.CourseState state, int startHour, int endHour, String instructorId) {
        Schedule schedule = new Schedule();
        schedule.setSundayStart(new Time(new Calendar.Builder().setTimeOfDay(startHour, 0, 0).build().getTimeInMillis()));
        schedule.setSundayEnd(new Time(new Calendar.Builder().setTimeOfDay(endHour, 0, 0).build().getTimeInMillis()));
        scheduleRepository.save(schedule);

        Course course = new Course();
        course.setName(name);
        course.setCourseState(state);
        course.setSchedule(schedule);
        Instructor instructor = instructorRepository.findInstructorById(instructorId);
        if (instructor != null) {
            course.setInstructor(instructor);
        }
        courseRepository.save(course);
        return course.getId();
    }


    @Test
    public void customerTriesToAccessTheScheduleOfAnApprovedCourse_ValidRequest() {
        //Issue token for customer
        String customerToken = authenticationService.issueToken(firstCustomerId);

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
        String customerToken = authenticationService.issueToken(firstCustomerId);

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
        String userToken = authenticationService.issueToken(firstCustomerId);

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

    @Test
    public void ownerRetrievesAllCourses_ValidRequest() {
        // Issue token for the owner
        String ownerToken = authenticationService.issueToken(ownerId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ownerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to the `/courses` endpoint
        ResponseEntity<CourseDTO[]> response = restTemplate.exchange(
                "/courses",
                HttpMethod.GET,
                entity,
                CourseDTO[].class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        List<CourseDTO> receivedCourses = Arrays.asList(response.getBody());
        assertEquals(4, receivedCourses.size(), "Should return all courses");
    }

    @Test
    public void instructorAccessesActiveAndOwnedCourses_ValidRequest() {
        // Issue token for the instructor
        String instructorToken = authenticationService.issueToken(instructorId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to the `/courses` endpoint
        ResponseEntity<CourseDTO[]> response = restTemplate.exchange(
                "/courses",
                HttpMethod.GET,
                entity,
                CourseDTO[].class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        List<CourseDTO> receivedCourses = Arrays.asList(response.getBody());

        // Verify that received courses include both the active course and the owned inactive course
        assertTrue(receivedCourses.stream().anyMatch(course -> course.getId().equals(approvedCourseId)),
                "Response should include the active course");
        assertTrue(receivedCourses.stream().anyMatch(course -> course.getId().equals(unapprovedCourseOwnedId)),
                "Response should include the owned inactive course");

        // Optionally, verify that no other unowned inactive courses are included
        assertFalse(receivedCourses.stream().anyMatch(course -> course.getId().equals(unapprovedCourseId)),
                "Response should not include unowned inactive courses");
    }


    @Test
    public void customerAccessesActiveCoursesOnly_ValidRequest() {
        // Issue token for the customer
        String customerToken = authenticationService.issueToken(firstCustomerId);

        // Set up the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make a GET request to the `/courses` endpoint
        ResponseEntity<CourseDTO[]> response = restTemplate.exchange(
                "/courses",
                HttpMethod.GET,
                entity,
                CourseDTO[].class);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        List<CourseDTO> receivedCourses = Arrays.asList(response.getBody());

        // Verify that received courses include the active course and do not include any inactive courses
        assertTrue(receivedCourses.stream().anyMatch(course -> course.getId().equals(approvedCourseId)),
                "Response should include the active course");

        // Optionally, verify that no inactive courses are included
        assertFalse(receivedCourses.stream().anyMatch(course -> course.getId().equals(unapprovedCourseOwnedId) || course.getId().equals(unapprovedCourseId)),
                "Response should not include inactive courses");
    }

    @Test
    public void unauthorizedAccessReturnsUnauthorized() {
        String fakeToken = "fake1234";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + fakeToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CourseDTO[]> response = restTemplate.exchange(
                "/courses",
                HttpMethod.GET,
                entity,
                CourseDTO[].class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be FORBIDDEN");
    }

    @Test
    public void validTokenButNoCoursesExist() {
        // Clear all courses to ensure the database has no courses
        registrationRepository.deleteAll();
        courseRepository.deleteAll();

        // Issue token for a valid user, for simplicity, let's use the owner
        String ownerToken = authenticationService.issueToken(ownerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ownerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CourseDTO[]> response = restTemplate.exchange(
                "/courses",
                HttpMethod.GET,
                entity,
                CourseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(0, response.getBody().length, "Response body should be an empty list");

        //Restore courses
        createCourses();
        createRegistrations();
    }

    @Test
    public void ownerAccessesAnyCourse_ValidRequest() {
        String ownerToken = authenticationService.issueToken(ownerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ownerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Try to access an unowned inactive course
        ResponseEntity<CourseDTO> responseInactive = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                unapprovedCourseId);

        assertEquals(HttpStatus.OK, responseInactive.getStatusCode());
        assertNotNull(responseInactive.getBody());
        assertEquals("Unapproved Test Course Not Owned by Anyone", responseInactive.getBody().getName());

        // Try to access an owned active course
        ResponseEntity<CourseDTO> responseActive = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                approvedCourseId);

        assertEquals(HttpStatus.OK, responseActive.getStatusCode());
        assertNotNull(responseActive.getBody());
        assertEquals("Approved Test Course", responseActive.getBody().getName());
    }

    @Test
    public void instructorAccessesOwnedCourse_ValidRequest() {
        String instructorToken = authenticationService.issueToken(instructorId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Accessing a course instructed by the same instructor
        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                unapprovedCourseOwnedId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unapproved Test Course Owned by Instructor", response.getBody().getName());
    }

    @Test
    public void instructorAccessesUnownedActiveCourse_ValidRequest() {
        String instructorToken = authenticationService.issueToken(instructorId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Accessing an active course not instructed by the same instructor
        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                approvedCourseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Approved Test Course", response.getBody().getName());
    }


    @Test
    public void instructorDeniedAccessToUnownedInactiveCourse() {
        String instructorToken = authenticationService.issueToken(instructorId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Attempt to access an unowned and inactive course
        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                unapprovedCourseId); // Ensure this is an ID of an inactive course not owned by the instructor

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void customerAccessesActiveCourse() {
        String customerToken = authenticationService.issueToken(firstCustomerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Accessing an active course
        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                approvedCourseId); // Ensure this is an ID of an active course

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Approved Test Course", response.getBody().getName());
    }

    @Test
    public void customerDeniedAccessToInactiveCourse() {
        String customerToken = authenticationService.issueToken(firstCustomerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Attempt to access an inactive course
        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                unapprovedCourseId); // Ensure this is an ID of an inactive course

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void unauthorizedUserRoleDeniedAccess() {
        // Assuming there's a way to issue a token for an unsupported role, or manually create a token
        String unsupportedRoleToken = "someTokenForUnsupportedRole";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + unsupportedRoleToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                approvedCourseId); // Use an existing course ID

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidTokenResultsInUnauthorized() {
        String invalidToken = "invalidToken123";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + invalidToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                approvedCourseId); // Use an existing course ID

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void nonexistentCourseReturnsNotFound() {
        // Use a valid token for this test to ensure the response is specifically about the course not existing
        String validToken = authenticationService.issueToken(firstCustomerId); // Or any valid user

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + validToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String nonexistentCourseId = "nonexistentCourseId";
        ResponseEntity<CourseDTO> response = restTemplate.exchange(
                "/courses/{course_id}",
                HttpMethod.GET,
                entity,
                CourseDTO.class,
                nonexistentCourseId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void ownerAccessesCourseCustomers_ValidRequest() {
        String ownerToken = authenticationService.issueToken(ownerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ownerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                courseWithCustomersId);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().length > 0, "Response body should contain at least one customer");
    }

    @Test
    public void instructorAccessesOwnCourseCustomers_ValidRequest() {
        String instructorToken = authenticationService.issueToken(instructorId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Assuming `courseWithCustomersId` is the ID of a course instructed by the current instructor
        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                courseWithCustomersId);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().length > 0, "Response body should contain at least one customer");
    }


    @Test
    public void instructorDeniedAccessToOtherCourseCustomers_ForbiddenRequest() {
        String instructorToken = authenticationService.issueToken(instructorId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Use a course ID that the instructor does not own
        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                unapprovedCourseId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
    }

    @Test
    public void customerDeniedAccess_ForbiddenRequest() {
        String customerToken = authenticationService.issueToken(firstCustomerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                courseWithCustomersId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
    }

    @Test
    public void unauthorizedUserRoleDeniedAccessToCustomers() {
        String fakeToken = "fake1234"; // Assuming a mechanism to generate or use an invalid token

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + fakeToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                courseWithCustomersId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be UNAUTHORIZED");
    }

    @Test
    public void invalidTokenResultsInUnauthorizedAccessToCustomers() {
        String invalidToken = "invalidToken"; // Simulate an invalid token

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + invalidToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                courseWithCustomersId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be UNAUTHORIZED");
    }

    @Test
    public void nonexistentCourseReturnsNotFoundAccessToCustomers() {
        String validToken = authenticationService.issueToken(ownerId); // Use a valid owner or instructor token

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + validToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String nonexistentCourseId = "nonexistentCourseId";
        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                nonexistentCourseId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response status should be NOT_FOUND");
    }

    @Test
    public void ownerOrInstructorWithValidTokenButNoCustomers() {
        String validToken = authenticationService.issueToken(ownerId); // Use a valid owner or instructor token

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + validToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange(
                "/courses/{course_id}/customers",
                HttpMethod.GET,
                entity,
                CustomerDTO[].class,
                approvedCourseId);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(0, response.getBody().length, "Response body should be an empty list");
    }




}

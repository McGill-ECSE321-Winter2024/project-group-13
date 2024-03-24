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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseDetailIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;

    @Autowired private CourseRepository courseRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private PersonRepository personRepository;
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

    @BeforeAll
    public void setup() {
        clearRepositories();
        SportCenter sportCenter = createAndSaveSportCenter();

        firstCustomerId = createAndSavePerson(Customer.class, "Hamid", "my-customer@mail.com", "8198888888", sportCenter);
        secondCustomerId = createAndSavePerson(Customer.class, "Elias", "my-customer2@mail.com", "8198884888", sportCenter);
        ownerId = createAndSavePerson(Owner.class, "Boss", "my-boss@mail.com", "8198808888", sportCenter);
        instructorId = createAndSavePerson(Instructor.class, "Trainer", "my-instructor@mail.com", "8198888889", sportCenter);

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

    private SportCenter createAndSaveSportCenter() {
        SportCenter sportCenter = new SportCenter();
        sportCenter.setName("Gold's Gym");
        return sportCenterRepository.save(sportCenter);
    }

    private <T extends Person> String createAndSavePerson(Class<T> type, String name, String email, String phoneNumber, SportCenter sportCenter) {
        T person;
        try {
            person = type.getDeclaredConstructor().newInstance();

            person.setName(name);
            person.setEmail(email);
            person.setPassword("test");
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
        //This represents a date and time of March 27, 2024, at 22:48:29 UTC.
        course.setCourseStartDate(new Timestamp(1711579709965L));
        Instructor instructor = instructorRepository.findInstructorById(instructorId);
        if (instructor != null) {
            course.setInstructor(instructor);
        }
        courseRepository.save(course);
        return course.getId();
    }

    private void assertOKResponse(ResponseEntity<?> response) {
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    private <T> ResponseEntity<T> performGetRequest(String token, String url, Class<T> responseType) {
        HttpEntity<String> entity = createHttpEntity(token);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }

    private HttpEntity<String> createHttpEntity(String token) {
        return new HttpEntity<>(createHeaders(token));
    }
    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }



    //---------------------------------------------------------------------------------------------------------------
    //------------------------- Testing getSchedule() controller method ---------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    @Test
    public void customerTriesToAccessTheScheduleOfAnApprovedCourse_ValidRequest() {
        // Issue token for customer and Make a GET request to your endpoint
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses/"+approvedCourseId+"/schedule",
                ScheduleDTO.class);

        // Assertions
        assertOKResponse(response);

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForApprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");

    }

    @Test
    public void customerTriesToAccessScheduleOfUnapprovedCourse_ForbiddenRequest() {
        // Issue a token for the customer and Make a GET request to the endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses/"+unapprovedCourseOwnedId+"/schedule",
                ScheduleDTO.class);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
        assertNull(response.getBody(), "Response body should be null because access is not allowed");
    }

    @Test
    public void ownerAccessesApprovedCourseSchedule_ValidRequest() {
        // Issue a token for the owner and Make a GET request to the endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses/"+approvedCourseId+"/schedule",
                ScheduleDTO.class);

        // Assertions
        assertOKResponse(response);

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForApprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }

    @Test
    public void ownerAccessesUnapprovedCourseSchedule_ValidRequest() {
        // Issue a token for the owner and Make a GET request to the endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses/"+unapprovedCourseId+"/schedule",
                ScheduleDTO.class);

        // Assertions
        assertOKResponse(response);

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForUnapprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }

    @Test
    public void instructorAccessesOwnedCourseSchedule_ValidRequest() {
        // Issue token for the instructor and Make a GET request to your endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+unapprovedCourseOwnedId+"/schedule",
                ScheduleDTO.class);

        // Assertions
        assertOKResponse(response);

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForUnapprovedCourseOwned); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }

    @Test
    public void instructorAccessesUnownedApprovedCourseSchedule_ValidRequest() {
        // Issue token for the instructor and Make a GET request to your endpoint for the approved course's schedule
        // `approvedCourseId` is for a course the instructor does not own
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+approvedCourseId+"/schedule",
                ScheduleDTO.class);

        // Assertions
        assertOKResponse(response);

        //Response body verifications
        ScheduleDTO expectedSchedule = new ScheduleDTO(scheduleForApprovedCourse); // Assuming you have a matching constructor or setters
        assertEquals(expectedSchedule.getSundayStart(), response.getBody().getSundayStart(), "Sunday start times should match");
        assertEquals(expectedSchedule.getSundayEnd(), response.getBody().getSundayEnd(), "Sunday end times should match");
    }


    @Test
    public void instructorDeniedAccessToUnownedUnapprovedCourseSchedule_ForbiddenRequest() {
        // Issue a token for the instructor and Make a GET request to the endpoint for the unapproved course's schedule
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+unapprovedCourseId+"/schedule",
                ScheduleDTO.class);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
        assertNull(response.getBody(), "Response body should be null because access is not allowed");
    }

    @Test
    public void tokenVerificationFailed() {
        // Issue an invalid token. Here, we just create a random string that doesn't correspond to any valid token.
        // Make a GET request to your endpoint with the invalid token.
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                "invalidToken12345",
                "/courses/"+approvedCourseId+"/schedule",
                ScheduleDTO.class);

        // Assert that the response status is UNAUTHORIZED
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be UNAUTHORIZED");
        assertNull(response.getBody(), "Response body should be null because the token is invalid");
    }


    @Test
    public void courseNotFound() {
        // Issue token for an existing user, for simplicity we'll use the customer already setup
        // Use a non-existent courseId in the GET request to your endpoint
        ResponseEntity<ScheduleDTO> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses/non-existent-course-id/schedule",
                ScheduleDTO.class);

        // Assert that the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response status should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null because the course does not exist");
    }

    //---------------------------------------------------------------------------------------------------------------
    //------------------------- Testing getAllCourses() controller method -------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    @Test
    public void ownerRetrievesAllCourses_ValidRequest() {
        // Issue token for the owner and Make a GET request to the `/courses` endpoint
        ResponseEntity<CourseDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses",
                CourseDTO[].class);

        // Assertions
        assertOKResponse(response);
        List<CourseDTO> receivedCourses = Arrays.asList(response.getBody());
        assertEquals(4, receivedCourses.size(), "Should return all courses");
    }

    @Test
    public void instructorAccessesActiveAndOwnedCourses_ValidRequest() {
        // Issue token for the instructor and Make a GET request to the `/courses` endpoint
        ResponseEntity<CourseDTO[]> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses",
                CourseDTO[].class);

        // Assertions
        assertOKResponse(response);
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
        // Issue token for the customer Make a GET request to the `/courses` endpoint
        ResponseEntity<CourseDTO[]> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses",
                CourseDTO[].class);

        // Assertions
        assertOKResponse(response);
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
        // Make a GET request to the `/courses` endpoint using a fake token
        ResponseEntity<CourseDTO[]> response = performGetRequest(
                "fake1234",
                "/courses",
                CourseDTO[].class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be FORBIDDEN");
    }

    @Test
    public void validTokenButNoCoursesExist() {
        // Clear all courses to ensure the database has no courses
        registrationRepository.deleteAll();
        courseRepository.deleteAll();

        // Issue token for a valid user, for simplicity, let's use the owner
        // Make a GET request to the `/courses` endpoint
        ResponseEntity<CourseDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses",
                CourseDTO[].class);

        assertOKResponse(response);
        assertEquals(0, response.getBody().length, "Response body should be an empty list");

        //Restore courses
        createCourses();
        createRegistrations();
    }

    @Test
    public void queryApprovedCourses_ReturnsApprovedCoursesOnly() {
        // Issue token for the owner and Query parameter for filtering by course state
        ResponseEntity<CourseDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses?state=Approved",
                CourseDTO[].class);

        assertOKResponse(response);
        // Check that all returned courses are in the Approved state
        assertTrue(Arrays.stream(response.getBody()).allMatch(course -> course.getCourseState().equals("Approved")), "All returned courses should be Approved");
    }

    @Test
    public void queryCoursesByStartDate_ReturnsCoursesStartingAfterGivenDate() {
        // Issue token for a role that has the privilege to query courses, such as the owner
        // Define the start date filter and perform the GET request
        String startDate = "2023-03-15";
        ResponseEntity<CourseDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses?startDate="+startDate,
                CourseDTO[].class);

        // Assertions
        assertOKResponse(response);

        // Convert the response body to a list for easier handling
        List<CourseDTO> courses = Arrays.asList(response.getBody());

        // Check that all returned courses start after the specified startDate
        assertTrue(courses.stream().allMatch(course -> course.getCourseStartDate().after(Timestamp.valueOf(startDate + " 00:00:00"))),
                "All returned courses should start after " + startDate);
    }


    @Test
    public void queryCoursesByInstructor_ReturnsInstructorSpecificCourses() {
        // Assuming the instructor's name is part of the URL query parameter
        Instructor instructor = instructorRepository.findInstructorById(instructorId);
        String url = "/courses?instructorName=" + instructor.getName();

        ResponseEntity<CourseDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                url,
                CourseDTO[].class);

        assertOKResponse(response);
        // Check that all returned courses are associated with the specified instructor
        assertTrue(Arrays.stream(response.getBody()).allMatch(course -> course.getInstructor().equals(instructor.getId())), "All returned courses should be taught by the specified instructor");
    }

    //---------------------------------------------------------------------------------------------------------------
    //------------------------- Testing getCourse() controller method -----------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    @Test
    public void ownerAccessesAnyCourse_ValidRequest() {
        String ownerToken = authenticationService.issueToken(ownerId);

        // Try to access an unowned inactive course
        ResponseEntity<CourseDTO> responseInactive = performGetRequest(
                ownerToken,
                "/courses/"+unapprovedCourseId,
                CourseDTO.class);

        assertEquals(HttpStatus.OK, responseInactive.getStatusCode());
        assertNotNull(responseInactive.getBody());
        assertEquals("Unapproved Test Course Not Owned by Anyone", responseInactive.getBody().getName());

        // Try to access an owned active course
        ResponseEntity<CourseDTO> responseActive = performGetRequest(
                ownerToken,
                "/courses/"+approvedCourseId,
                CourseDTO.class);

        assertOKResponse(responseActive);
        assertEquals("Approved Test Course", responseActive.getBody().getName());
    }

    @Test
    public void instructorAccessesOwnedCourse_ValidRequest() {
        // Accessing a course instructed by the same instructor
        ResponseEntity<CourseDTO> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+unapprovedCourseOwnedId,
                CourseDTO.class);

        assertOKResponse(response);
        assertEquals("Unapproved Test Course Owned by Instructor", response.getBody().getName());
    }

    @Test
    public void instructorAccessesUnownedActiveCourse_ValidRequest() {
        // Accessing an active course not instructed by the same instructor
        ResponseEntity<CourseDTO> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+approvedCourseId,
                CourseDTO.class);

        assertOKResponse(response);
        assertEquals("Approved Test Course", response.getBody().getName());
    }


    @Test
    public void instructorDeniedAccessToUnownedInactiveCourse() {
        // Attempt to access an unowned and inactive course
        ResponseEntity<CourseDTO> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+unapprovedCourseId,
                CourseDTO.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void customerAccessesActiveCourse() {
        // Accessing an active course
        ResponseEntity<CourseDTO> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses/"+approvedCourseId,
                CourseDTO.class);

        assertOKResponse(response);
        assertEquals("Approved Test Course", response.getBody().getName());
    }

    @Test
    public void customerDeniedAccessToInactiveCourse() {
        // Attempt to access an inactive course
        ResponseEntity<CourseDTO> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses/"+unapprovedCourseId,
                CourseDTO.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void unauthorizedUserRoleDeniedAccess() {
        ResponseEntity<CourseDTO> response = performGetRequest(
                "someTokenForUnsupportedRole",
                "/courses/"+approvedCourseId,
                CourseDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidTokenResultsInUnauthorized() {
        ResponseEntity<CourseDTO> response = performGetRequest(
                "invalidToken123",
                "/courses/"+approvedCourseId,
                CourseDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void nonexistentCourseReturnsNotFound() {
        // Use a valid token for this test to ensure the response is specifically about the course not existing
        ResponseEntity<CourseDTO> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses/nonexistentCourseId",
                CourseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //---------------------------------------------------------------------------------------------------------------
    //------------------------- Testing getCustomers() controller method --------------------------------------------
    //---------------------------------------------------------------------------------------------------------------
    @Test
    public void ownerAccessesCourseWithCustomers_ValidRequest() {
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses/"+courseWithCustomersId+"/customers",
                CustomerDTO[].class);

        assertOKResponse(response);
        assertTrue(response.getBody().length > 0, "Response body should contain at least one customer");
    }

    @Test
    public void instructorAccessesOwnCourseCustomers_ValidRequest() {
        // Assuming `courseWithCustomersId` is the ID of a course instructed by the current instructor
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+courseWithCustomersId+"/customers",
                CustomerDTO[].class);

        assertOKResponse(response);
        assertTrue(response.getBody().length > 0, "Response body should contain at least one customer");
    }


    @Test
    public void instructorDeniedAccessToOtherCourseCustomers_ForbiddenRequest() {
        // Use a course ID that the instructor does not own
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(instructorId),
                "/courses/"+unapprovedCourseId+"/customers",
                CustomerDTO[].class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
    }

    @Test
    public void customerDeniedAccess_ForbiddenRequest() {
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(firstCustomerId),
                "/courses/"+courseWithCustomersId+"/customers",
                CustomerDTO[].class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response status should be FORBIDDEN");
    }

    @Test
    public void unauthorizedUserRoleDeniedAccessToCustomers() {
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                "fake1234",
                "/courses/"+courseWithCustomersId+"/customers",
                CustomerDTO[].class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be UNAUTHORIZED");
    }

    @Test
    public void nonexistentCourseReturnsNotFoundAccessToCustomers() {
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses/nonexistentCourseId/customers",
                CustomerDTO[].class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response status should be NOT_FOUND");
    }

    @Test
    public void ownerOrInstructorWithValidTokenButNoCustomers() {
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses/"+approvedCourseId+"/customers",
                CustomerDTO[].class);

        assertOKResponse(response);
        assertEquals(0, response.getBody().length, "Response body should be an empty list");
    }

    @Test
    public void ownerAccessesCourseCustomersFilteredByEmail_ValidRequest() {
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses/" + courseWithCustomersId + "/customers?email=my-customer@mail.com",
                CustomerDTO[].class);

        assertOKResponse(response);
        assertEquals(1, response.getBody().length, "Response body should contain one customer with the specified email");
        assertEquals("my-customer@mail.com", response.getBody()[0].getEmail(), "The email of the returned customer should match the query parameter");
    }

    @Test
    public void ownerAccessesCourseCustomersFilteredByName_ValidRequest() {
        ResponseEntity<CustomerDTO[]> response = performGetRequest(
                authenticationService.issueToken(ownerId),
                "/courses/" + courseWithCustomersId + "/customers?name=Hamid",
                CustomerDTO[].class);

        assertOKResponse(response);
        assertTrue(response.getBody().length > 0, "Response body should contain at least one customer with the specified name");
        assertTrue(Arrays.stream(response.getBody()).anyMatch(customer -> customer.getName().equals("Hamid")), "The name of at least one returned customer should match the query parameter");
    }




}

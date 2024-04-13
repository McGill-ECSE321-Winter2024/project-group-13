package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseRegistrationIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;

    @Autowired private SportCenterRepository sportCenterRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private AuthenticationService authenticationService;
    @Autowired private CourseSessionRepository courseSessionRepository;

    private String customerToken;
    private String ownerToken;
    private String instructorToken;
    private String unapprovedCourseId;
    private String RegistrationValidCourseId;
    private String RegistrationInvalidCourseId;
    @BeforeAll
    public void setup() {
        clearRepositories();
        SportCenter sportCenter = createAndSaveSportCenter();
        createCourses();

        String customerId = createAndSavePerson(Customer.class, "Customer", "my-customer1@mail.com", "8198884888", sportCenter);
        String ownerId = createAndSavePerson(Owner.class, "Boss", "my-boss1@mail.com", "8198808888", sportCenter);
        String instructorId = createAndSavePerson(Instructor.class, "Trainer", "my-instructor1@mail.com", "8198888889", sportCenter);

        // Issue a valid token
        customerToken = authenticationService.issueToken(customerId);
        ownerToken = authenticationService.issueToken(ownerId);
        instructorToken = authenticationService.issueToken(instructorId);
    }

    @AfterEach
    public void teardown() {
        registrationRepository.deleteAll();
    }

    @AfterAll
    public void teardownEnd() {
        clearRepositories();
    }

    private void clearRepositories() {
        courseSessionRepository.deleteAll();
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
        sportCenterRepository.deleteAll();
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

    private void createCourses(){
        unapprovedCourseId = createAndSaveCourse("Unapproved Test Course Not Owned by Anyone", Course.CourseState.Inactive, Timestamp.valueOf("2025-02-24 20:00:00"));
        RegistrationValidCourseId = createAndSaveCourse("Approved Test Course", Course.CourseState.Approved, Timestamp.valueOf("2025-02-24 20:00:00"));
        RegistrationInvalidCourseId = createAndSaveCourse("Approved Test Course but Ended", Course.CourseState.Approved, Timestamp.valueOf("2012-02-24 20:00:00"));
    }

    private String createAndSaveCourse(String name, Course.CourseState state, Timestamp timestamp) {
        Course course = new Course();
        course.setName(name);
        course.setCourseState(state);

        course.setCourseEndDate(timestamp);
        courseRepository.save(course);
        return course.getId();
    }

    @Test
    public void instructorAttemptsToRegisterForCourse_InvalidRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + instructorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/courses/" + RegistrationValidCourseId + "/register", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Only customers can register for courses.", response.getBody());
    }

    @Test
    public void ownerAttemptsToRegisterForCourse_InvalidRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + ownerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/courses/" + RegistrationValidCourseId + "/register", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Only customers can register for courses.", response.getBody());
    }

    @Test
    public void customerAttemptsToRegisterForEndedCourse_InvalidRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/courses/" + RegistrationInvalidCourseId + "/register", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Registration is only possible for approved courses with an end date in the future."));
    }

    @Test
    public void customerAttemptsToRegisterForUnapprovedCourse_InvalidRequest() {
        // Set up the Authorization header with the customer's bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Attempt to register for the unapproved course using its ID
        ResponseEntity<String> response = restTemplate.postForEntity("/courses/" + unapprovedCourseId + "/register", entity, String.class);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "The response status should indicate a bad request due to the course being unapproved.");
        assertTrue(response.getBody().contains("Registration is only possible for approved courses"),
                "The response body should explain that registration failed because the course is not approved.");
    }




    @Test
    public void AttemptToRegisterForNonexistentCourse() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + customerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Assuming "nonexistentCourseId" does not exist in the database
        ResponseEntity<String> response = restTemplate.postForEntity("/courses/nonexistentCourseId/register", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Course not found.", response.getBody());
    }




}

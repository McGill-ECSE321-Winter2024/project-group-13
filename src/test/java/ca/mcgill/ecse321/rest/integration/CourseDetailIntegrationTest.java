package ca.mcgill.ecse321.rest.integration;

import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.CustomerRepository;
import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.dao.ScheduleRepository;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Schedule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ca.mcgill.ecse321.rest.services.AuthenticationService;

import java.sql.Time;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CourseDetailIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;

    @Autowired private CourseRepository courseRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private AuthenticationService authenticationService;

    private String unapprovedCourseId;
    private String approvedCourseId;
    private Schedule scheduleForUnapprovedCourse;
    private Schedule scheduleForApprovedCourse;

    @BeforeEach
    public void setup() {
        // Clear the repositories before each test
        courseRepository.deleteAll();
        scheduleRepository.deleteAll();
        personRepository.deleteAll();

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

    @AfterEach
    public void teardown() {
        // Clean up the database after tests
        courseRepository.deleteAll();
        scheduleRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    public void anCustomerTriesToAccessTheScheduleOfAnApprovedCourse_ValidRequest() {
        //Create the customer and persist
        Customer customer = new Customer();
        customer.setName("Hamid");
        customer.setPassword("test");
        customer.setPhoneNumber("8198888888");
        customer.setEmail("my-customer@mail.com");
        customerRepository.save(customer);

        //Issue token for customer
        String customerToken = authenticationService.issueToken(customer.getId());

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


}

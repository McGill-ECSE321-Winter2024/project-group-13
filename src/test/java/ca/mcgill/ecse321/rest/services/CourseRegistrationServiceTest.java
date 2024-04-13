package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.CustomerRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.helpers.PersonSession.PersonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Nested
@ExtendWith(MockitoExtension.class)
class CourseRegistrationServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RegistrationRepository registrationRepository;
    @InjectMocks
    private CourseRegistrationService courseRegistrationService;

    private final String courseId = "Course123";
    private final String customerId = "Customer123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void registerForCourse_CourseNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        PersonSession personSession = new PersonSession(customerId, PersonType.Customer, "SportCenter123");

        String result = courseRegistrationService.registerForCourse(courseId, personSession);

        assertEquals("Course not found.", result);
    }

    @Test
    void registerForCourse_NotACustomer() {
        PersonSession personSession = new PersonSession(customerId, PersonType.Instructor, "SportCenter123");

        String result = courseRegistrationService.registerForCourse(courseId, personSession);

        assertEquals("Only customers can register for courses.", result);
    }

    @Test
    void registerForCourse_CourseNotApprovedOrEnded() {
        Course course = new Course();
        course.setId(courseId);
        course.setCourseState(Course.CourseState.AwaitingApproval);
        course.setCourseEndDate(new Timestamp(System.currentTimeMillis() - 1000000)); // Past date

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        PersonSession personSession = new PersonSession(customerId, PersonType.Customer, "SportCenter123");

        String result = courseRegistrationService.registerForCourse(courseId, personSession);

        assertEquals("Registration is only possible for approved courses with an end date in the future.", result);
    }
}

package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.InstructorRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CourseDetailServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private InstructorRepository instructorRepository;
    @Mock private RegistrationRepository registrationRepository;

    @InjectMocks
    private CourseDetailService courseDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSchedule_CourseExists_ReturnsSchedule() {
        // Arrange
        String courseId = "1";
        Course mockCourse = mock(Course.class);
        Schedule mockSchedule = mock(Schedule.class); // Create a mock Schedule object
        when(mockCourse.getSchedule()).thenReturn(mockSchedule); // Stub the getSchedule() method
        when(courseRepository.findCourseById(courseId)).thenReturn(mockCourse);

        // Act
        Schedule result = courseDetailService.getSchedule(courseId);

        // Assert
        assertNotNull(result, "The returned Schedule should not be null.");
        verify(courseRepository).findCourseById(courseId);
        verify(mockCourse).getSchedule(); // Optionally verify that getSchedule was called on the mockCourse
    }


    @Test
    void getAllCourses_ReturnsListOfCourses() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList(new Course(), new Course()));

        // Act
        List<Course> result = courseDetailService.getCoursesWithFilters(null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseRepository).findAll();
    }

    @Test
    void getCoursesWithFilters_ByStateAndInstructor_ReturnsFilteredCourses() {
        // Arrange
        Course.CourseState state = Course.CourseState.Approved;
        String instructorName = "John Doe";
        List<Course> expectedCourses = Arrays.asList(new Course(), new Course());

        Instructor mockInstructor = new Instructor();
        mockInstructor.setName(instructorName);
        mockInstructor.setId("instructorId");

        // Mock the instructor finding process
        when(instructorRepository.findInstructorByName(instructorName)).thenReturn(mockInstructor);
        // Assume the findCoursesByFilters to be correctly implemented to handle instructorId and state
        when(courseRepository.findCoursesByFilters(eq(state), eq(mockInstructor.getId()), any())).thenReturn(expectedCourses);

        // Act
        List<Course> result = courseDetailService.getCoursesWithFilters(state, instructorName, null);

        // Assert
        assertNotNull(result, "The returned list should not be null.");
        assertEquals(expectedCourses.size(), result.size(), "The returned list size should match the expected size.");
        assertTrue(result.containsAll(expectedCourses), "The returned list should contain all the expected courses.");
        verify(courseRepository).findCoursesByFilters(eq(state), eq(mockInstructor.getId()), any());
    }


    @Test
    void getSpecificCourse_CourseExists_ReturnsCourse() {
        // Arrange
        String courseId = "1";
        Course expectedCourse = new Course();
        when(courseRepository.findCourseById(courseId)).thenReturn(expectedCourse);

        // Act
        Course result = courseDetailService.getSpecificCourse(courseId);

        // Assert
        assertEquals(expectedCourse, result);
        verify(courseRepository).findCourseById(courseId);
    }

    @Test
    void getCustomers_CourseHasRegistrations_ReturnsCustomers() {
        // Arrange
        String courseId = "1";
        Registration reg1 = mock(Registration.class);
        Registration reg2 = mock(Registration.class);
        when(registrationRepository.findRegistrationByCourseId(courseId)).thenReturn(Arrays.asList(reg1, reg2));
        when(reg1.getCustomer()).thenReturn(new Customer());
        when(reg2.getCustomer()).thenReturn(new Customer());

        // Act
        List<Customer> result = courseDetailService.getCustomers(courseId, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(registrationRepository).findRegistrationByCourseId(courseId);
    }

    @Test
    void getActiveCourses_ReturnsOnlyApprovedCourses() {
        // Arrange
        when(courseRepository.findCoursesByCourseState(Course.CourseState.Approved)).thenReturn(List.of(new Course()));

        // Act
        List<Course> result = courseDetailService.getActiveCourses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(courseRepository).findCoursesByCourseState(Course.CourseState.Approved);
    }

    @Test
    void getAllActiveAndInstructorSpecificCourses_ReturnsCombinedList() {
        // Arrange
        String instructorId = "1";
        Course activeCourse = new Course();
        Course instructorSpecificCourse = new Course();
        when(courseDetailService.getActiveCourses()).thenReturn(List.of(activeCourse));
        when(courseRepository.findCoursesByInstructorId(instructorId)).thenReturn(List.of(instructorSpecificCourse));

        // Act
        List<Course> result = courseDetailService.getAllActiveAndInstructorSpecificCourses(instructorId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Assuming both lists have unique courses
        verify(courseRepository).findCoursesByInstructorId(instructorId);
    }

    @Test
    void isInstructorOfCourse_InstructorIsOfCourse_ReturnsTrue() {
        // Arrange
        String courseId = "1";
        String instructorId = "1";
        Course course = mock(Course.class);
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        when(course.getInstructor()).thenReturn(instructor);
        when(courseRepository.findCourseById(courseId)).thenReturn(course);

        // Act
        boolean result = courseDetailService.isInstructorOfCourse(courseId, instructorId);

        // Assert
        assertTrue(result);
        verify(courseRepository).findCourseById(courseId);
    }

}


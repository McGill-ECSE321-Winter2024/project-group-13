package ca.mcgill.ecse321.rest.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.SportCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Nested
@ExtendWith(MockitoExtension.class)
public class CourseSessionServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SportCenterRepository sportCenterRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private CourseSessionService courseSessionService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourses_ValidInput() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        Course course= new Course();
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        CourseDTO courseDTO= new CourseDTO(courseName,sportsCenterID);
        PersonSession personSessionOwner= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        PersonSession personSessionInstructor= new PersonSession(personID,PersonSession.PersonType.Instructor,sportsCenterID);
        PersonSession personSessionCustomer= new PersonSession(personID,PersonSession.PersonType.Customer,sportsCenterID);
        when(sportCenterRepository.findSportCenterById(courseDTO.getSportCenter())).thenReturn(sportCenter);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        String errorMessageOwner= courseService.createCourse(courseName,personSessionOwner);
        String errorMessageInstructor= courseService.createCourse(courseName,personSessionInstructor);
        String errorMessageCustomer= courseService.createCourse(courseName,personSessionCustomer);

        assertEquals("",errorMessageOwner);
        assertEquals("",errorMessageInstructor);
        assertEquals("Must be an owner or instructor",errorMessageCustomer);
        verify(sportCenterRepository,times(2)).findSportCenterById(courseDTO.getSportCenter());
        verify(courseRepository,times(2)).save(any(Course.class));
    }
    @Test
    void createCourse_InvalidInput() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        Course course= new Course();
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        PersonSession personSession1= new PersonSession(personID,PersonSession.PersonType.Owner,"123");


        String errorMessageName= courseService.createCourse("",personSession);
        String errorMessageID= courseService.createCourse(courseName,personSession1);

        assertEquals("Course requires name to be created",errorMessageName);
        assertEquals("Invalid sport's center id",errorMessageID);
    }
    @Test
    public void testCreateSessionsPerCourse() {
        // Write your test scenario here
    }

    // Test updating course session start time
    @Test
    public void testUpdateCourseSessionStart() {
        // Write your test scenario here
    }

    // Test updating course session end time
    @Test
    public void testUpdateCourseSessionEnd() {
        // Write your test scenario here
    }

    // Test deleting course session
    @Test
    public void testDeleteCourseSession() {
        // Write your test scenario here
    }
}

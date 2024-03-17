package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Nested
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SportCenterRepository sportCenterRepository;
    @InjectMocks
    private CourseService courseService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourse_ValidInput() {
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

        String errorMessageOwner= courseService.createCourse(courseDTO,personSessionOwner);
        String errorMessageInstructor= courseService.createCourse(courseDTO,personSessionInstructor);
        String errorMessageCustomer= courseService.createCourse(courseDTO,personSessionCustomer);

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
        CourseDTO courseDTO= new CourseDTO("",sportsCenterID);
        CourseDTO courseDTO1= new CourseDTO(courseName,"123");
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);

        String errorMessageName= courseService.createCourse(courseDTO,personSession);
        String errorMessageID= courseService.createCourse(courseDTO1,personSession);

        assertEquals("Course requires name to be created",errorMessageName);
        assertEquals("Invalid sport's center id",errorMessageID);
    }
    @Test
    void approveCourse_ValidInput() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        Course course= new Course();
        course.setId("Course_ID");
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        when(courseRepository.findCourseById(courseID)).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        String errorMessage= courseService.approveCourse(courseID,personSession);

        assertEquals("",errorMessage);
        verify(courseRepository).findCourseById(courseID);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void approveCourse_InvalidInput() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        Course course= new Course();
        course.setId(courseID);
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        PersonSession personSessionInstructor= new PersonSession(personID,PersonSession.PersonType.Instructor,sportsCenterID);
        when(courseRepository.findCourseById(courseID)).thenReturn(course);
        when(courseRepository.findCourseById("")).thenReturn(null);


        String errorMessageOwner= courseService.approveCourse(courseID,personSessionInstructor);
        String errorMessageNull= courseService.approveCourse("",personSession);

        assertEquals("Must be an owner of the course's sports center", errorMessageOwner);
        assertEquals("Course does not exist",errorMessageNull);
        verify(courseRepository).findCourseById(courseID);
        verify(courseRepository).findCourseById("");
    }
}

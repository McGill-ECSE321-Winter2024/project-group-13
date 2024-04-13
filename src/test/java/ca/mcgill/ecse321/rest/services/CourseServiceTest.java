package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
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

import java.sql.Timestamp;

@Nested
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SportCenterRepository sportCenterRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private CourseService courseService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        assertEquals("", errorMessageOwner);
        assertEquals("Course does not exist",errorMessageNull);
        verify(courseRepository).findCourseById(courseID);
        verify(courseRepository).findCourseById("");
    }
    @Test
    void updateCourseNameTest() {
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
        when(courseRepository.findCourseById(courseID)).thenReturn(course);

        String errorMessage= courseService.updateCourseName(personSession,courseID,courseName+"123");
        String errorMessageNull= courseService.updateCourseName(personSession,courseID,"");

        assertEquals("",errorMessage);
        assertNull(courseRepository.findCourseByName(courseName));
        assertEquals("Name must not be null",errorMessageNull);

        verify(courseRepository,times(2)).findCourseById(courseID);
    }

    @Test
    void updateCourseDescriptionTest() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        String description= "This course description";
        Course course= new Course();
        course.setId(courseID);
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        when(courseRepository.findCourseById(courseID)).thenReturn(course);

        String errorMessage= courseService.updateCourseDescription(personSession,courseID,description);
        String errorMessageNull= courseService.updateCourseDescription(personSession,courseID,"");

        assertEquals("",errorMessage);
        assertEquals("Description must not be null",errorMessageNull);

        verify(courseRepository,times(2)).findCourseById(courseID);
    }
    @Test
    void updateCourseLevel() {
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
        when(courseRepository.findCourseById(courseID)).thenReturn(course);

        String errorMessageBeginner= courseService.updateCourseLevel(personSession,courseID,"Beginner");
        String errorMessageIntermediate= courseService.updateCourseLevel(personSession,courseID,"Intermediate");
        String errorMessageAdvanced= courseService.updateCourseLevel(personSession,courseID,"Advanced");
        String errorMessageNull= courseService.updateCourseLevel(personSession,courseID,"");

        assertEquals("",errorMessageBeginner);
        assertEquals("",errorMessageIntermediate);
        assertEquals("",errorMessageAdvanced);
        assertEquals("Requires valid level",errorMessageNull);

        verify(courseRepository,times(4)).findCourseById(courseID);
    }
    @Test
    void updateCourseRate() {
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
        when(courseRepository.findCourseById(courseID)).thenReturn(course);

        String errorMessage= courseService.updateCourseRate(personSession,courseID,10.0);
        String errorMessageZero= courseService.updateCourseRate(personSession,courseID,0.0);
        String errorMessageNegative= courseService.updateCourseRate(personSession,courseID,-1000.0);

        assertEquals("",errorMessage);
        assertEquals("",errorMessageZero);
        assertEquals("Course rate must be a positive number",errorMessageNegative);

        verify(courseRepository,times(3)).findCourseById(courseID);
    }
    @Test
    void updateCourseStartDate() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        Timestamp courseStartDate = new Timestamp(2023, 1, 1, 6, 15, 0, 0);
        Timestamp courseStartDate1 = new Timestamp(2024, 2, 1, 6, 15, 0, 0);
        Timestamp courseEndDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);
        Course course= new Course();
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);
        course.setId(courseID);
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        when(courseRepository.findCourseById(courseID)).thenReturn(course);

        String errorMessage= courseService.updateCourseStartDate(personSession,courseID,courseStartDate);
        String errorMessageAfter= courseService.updateCourseStartDate(personSession,courseID,courseStartDate1);

        assertEquals("",errorMessage);
        assertEquals("Course start date must be before course end date",errorMessageAfter);

        verify(courseRepository,times(2)).findCourseById(courseID);
    }
    @Test
    void updateCourseEndDate() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        Timestamp courseStartDate = new Timestamp(2023, 1, 1, 6, 15, 0, 0);
        Timestamp courseEndDate = new Timestamp(2024, 2, 1, 6, 15, 0, 0);
        Timestamp courseEndDate1 = new Timestamp(2022, 1, 1, 6, 15, 0, 0);
        Course course= new Course();
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);
        course.setId(courseID);
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        when(courseRepository.findCourseById(courseID)).thenReturn(course);

        String errorMessage= courseService.updateCourseEndDate(personSession,courseID,courseEndDate);
        String errorMessageBefore= courseService.updateCourseEndDate(personSession,courseID,courseEndDate1);

        assertEquals("",errorMessage);
        assertEquals("Course end date must be after course start date",errorMessageBefore);

        verify(courseRepository,times(2)).findCourseById(courseID);
    }
    @Test
    void updateCourseInstructor() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        String instructorID= "Instructor123";
        Course course= new Course();
        Instructor instructor = new Instructor();
        instructor.setId(instructorID);
        course.setId(courseID);
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);

        when(courseRepository.findCourseById(courseID)).thenReturn(course);
        when(instructorRepository.findInstructorById(instructorID)).thenReturn(instructor);
        when(instructorRepository.findInstructorById("")).thenReturn(null);

        String errorMessage= courseService.updateCourseInstructor(personSession,courseID,instructorID);
        String errorMessageNull= courseService.updateCourseInstructor(personSession,courseID,"");

        assertEquals("",errorMessage);
        assertEquals("Instructor not found",errorMessageNull);

        verify(courseRepository,times(2)).findCourseById(courseID);
        verify(instructorRepository).findInstructorById(instructorID);
        verify(instructorRepository).findInstructorById("");
    }
    @Test
    void updateCourseRoom() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        String roomID= "Room123";
        Course course= new Course();
        Room room= new Room();
        room.setId(roomID);
        course.setId(courseID);
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        when(courseRepository.findCourseById(courseID)).thenReturn(course);
        when(roomRepository.findRoomById(roomID)).thenReturn(room);
        when(roomRepository.findRoomById("")).thenReturn(null);

        String errorMessage= courseService.updateCourseRoom(personSession,courseID,roomID);
        String errorMessageNull= courseService.updateCourseRoom(personSession,courseID,"");

        assertEquals("",errorMessage);
        assertEquals("Room not found",errorMessageNull);

        verify(courseRepository,times(2)).findCourseById(courseID);
        verify(roomRepository).findRoomById(roomID);
        verify(roomRepository).findRoomById("");

    }
    @Test
    void updateCourseSchedule() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        String scheduleID= "Schedule123";
        Course course= new Course();
        Schedule schedule = new Schedule();
        schedule.setId(scheduleID);
        course.setId(courseID);
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSession= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);

        when(courseRepository.findCourseById(courseID)).thenReturn(course);
        when(scheduleRepository.findScheduleById(scheduleID)).thenReturn(schedule);
        when(scheduleRepository.findScheduleById("")).thenReturn(null);

        String errorMessage= courseService.updateCourseSchedule(personSession,courseID,scheduleID);
        String errorMessageNull= courseService.updateCourseSchedule(personSession,courseID,"");

        assertEquals("",errorMessage);
        assertEquals("Schedule not found",errorMessageNull);

        verify(courseRepository,times(2)).findCourseById(courseID);
        verify(scheduleRepository).findScheduleById(scheduleID);
        verify(scheduleRepository).findScheduleById("");
    }
    @Test
    void deleteCourse() {
        String courseName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String courseID= "Course_ID";
        Course course= new Course();
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        course.setName(courseName);
        course.setSportCenter(sportCenter);
        PersonSession personSessionOwner= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        PersonSession personSessionInstructor= new PersonSession(personID,PersonSession.PersonType.Instructor,sportsCenterID);
        PersonSession personSessionCustomer= new PersonSession(personID,PersonSession.PersonType.Customer,sportsCenterID);
        when(courseRepository.findCourseById(courseID)).thenReturn(course);
        when(courseRepository.findCourseById("")).thenReturn(null);


        String errorMessageOwner= courseService.deleteCourse(personSessionOwner,courseID);
        String errorMessageInstructor= courseService.deleteCourse(personSessionInstructor,courseID);
        String errorMessageCustomer= courseService.deleteCourse(personSessionCustomer,courseID);
        String errorMessageInvalid= courseService.deleteCourse(personSessionOwner,"");

        assertEquals("",errorMessageOwner);
        assertEquals("",errorMessageInstructor);
        assertEquals("Must be an owner of the course's sports center",errorMessageCustomer);
        assertEquals("Course does not exist",errorMessageInvalid);

        verify(courseRepository,times(3)).findCourseById(courseID);
        verify(courseRepository).findCourseById("");
    }

}

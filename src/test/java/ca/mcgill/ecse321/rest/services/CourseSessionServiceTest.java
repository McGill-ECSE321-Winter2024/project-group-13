package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.CourseSessionRepository;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import ca.mcgill.ecse321.rest.models.Schedule;
import ca.mcgill.ecse321.rest.models.SportCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.openmbean.InvalidKeyException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
public class CourseSessionServiceTest {
    @Mock private CourseRepository courseRepository;
    @Mock private CourseSessionRepository courseSessionRepository;
    @InjectMocks private CourseSessionService courseSessionService;
    @InjectMocks private CourseService courseService;


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

        assertEquals("Course requires name to be created",errorMessageName);
    }
    @Test
    void createSessionsPerCourse_ValidInput() {
        // Arrange
        Course course = mock(Course.class);
        Schedule schedule = mock(Schedule.class);
        String courseId = "courseId";

        when(courseRepository.findCourseById(courseId)).thenReturn(course);
        when(course.getSchedule()).thenReturn(schedule);
        when(course.getCourseStartDate()).thenReturn(Timestamp.valueOf("2024-03-25 00:00:00"));
        when(course.getCourseEndDate()).thenReturn(Timestamp.valueOf("2024-04-01 00:00:00"));
        when(schedule.getMondayStart()).thenReturn(Time.valueOf("09:00:00"));
        when(schedule.getMondayEnd()).thenReturn(Time.valueOf("10:00:00"));

        // Act
        String result = courseSessionService.createSessionsPerCourse(courseId);

        // Assert
        assertEquals("", result); // Assuming the method returns an empty string on success
        verify(courseSessionRepository, atLeastOnce()).save(any(CourseSession.class));
    }

    @Test
    void whenCreateSessionsForValidCourse_thenSessionsAreCreated() {
        // Arrange
        String validCourseId = "validCourseId";
        Course mockCourse = mock(Course.class);
        Schedule mockSchedule = mock(Schedule.class);
        when(courseRepository.findCourseById(validCourseId)).thenReturn(mockCourse);
        when(mockCourse.getSchedule()).thenReturn(mockSchedule);
        when(mockCourse.getCourseStartDate()).thenReturn(Timestamp.valueOf("2024-03-01 00:00:00"));
        when(mockCourse.getCourseEndDate()).thenReturn(Timestamp.valueOf("2024-03-07 00:00:00"));
        // Assume the schedule for simplicity
        when(mockSchedule.getMondayStart()).thenReturn(Time.valueOf("09:00:00"));
        when(mockSchedule.getMondayEnd()).thenReturn(Time.valueOf("11:00:00"));

        // Execute
        String result = courseSessionService.createSessionsPerCourse(validCourseId);

        // Verify
        assertEquals("", result);
        verify(courseSessionRepository, atLeastOnce()).save(any(CourseSession.class));
    }

    @Test
    void whenCreateSessionsForInvalidCourse_thenReturnsInvalidCourseIdMessage() {
        // Arrange
        String invalidCourseId = "invalidCourseId";
        when(courseRepository.findCourseById(invalidCourseId)).thenReturn(null);

        // Act
        String result = courseSessionService.createSessionsPerCourse(invalidCourseId);

        // Assert
        assertEquals("Invalid course id", result);
        verify(courseSessionRepository, never()).save(any(CourseSession.class));
    }

    @Test
    void whenCreateSessionsForCourseWithoutSchedule_thenReturnsScheduleNotFoundMessage() {
        // Arrange
        String courseIdWithoutSchedule = "courseIdWithoutSchedule";
        Course mockCourse = mock(Course.class);
        when(courseRepository.findCourseById(courseIdWithoutSchedule)).thenReturn(mockCourse);
        when(mockCourse.getSchedule()).thenReturn(null);

        // Act
        String result = courseSessionService.createSessionsPerCourse(courseIdWithoutSchedule);

        // Assert
        assertEquals("Schedule not found", result);
        verify(courseSessionRepository, never()).save(any(CourseSession.class));
    }

    @Test
    void whenCreateSessionsForCourseWithActiveDays_thenSessionsAreCreatedForEachDay() {
        // Arrange
        String validCourseId = "courseWithMultipleActiveDays";
        Course mockCourse = mock(Course.class);
        Schedule mockSchedule = mock(Schedule.class);
        when(courseRepository.findCourseById(validCourseId)).thenReturn(mockCourse);
        when(mockCourse.getSchedule()).thenReturn(mockSchedule);
        when(mockCourse.getCourseStartDate()).thenReturn(Timestamp.valueOf("2024-03-01 00:00:00"));
        when(mockCourse.getCourseEndDate()).thenReturn(Timestamp.valueOf("2024-03-07 00:00:00"));
        // Define mock behavior for schedule times
        when(mockSchedule.getMondayStart()).thenReturn(Time.valueOf("09:00:00"));
        when(mockSchedule.getMondayEnd()).thenReturn(Time.valueOf("11:00:00"));
        when(mockSchedule.getTuesdayStart()).thenReturn(Time.valueOf("10:00:00"));
        when(mockSchedule.getTuesdayEnd()).thenReturn(Time.valueOf("12:00:00"));

        // Act
        String result = courseSessionService.createSessionsPerCourse(validCourseId);

        // Assert
        assertEquals("", result);
        verify(courseSessionRepository, atLeast(2)).save(any(CourseSession.class)); // Expecting at least two saves, one for each active day
    }

    @Test
    void whenGetCourseSessionWithValidId_thenSessionIsReturned() {
        // Arrange
        String validSessionId = "validSessionId";
        CourseSession expectedSession = new CourseSession();
        expectedSession.setId(validSessionId);
        when(courseSessionRepository.findCourseSessionById(validSessionId)).thenReturn(expectedSession);

        // Act
        CourseSession actualSession = courseSessionService.getCourseSession(validSessionId);

        // Assert
        assertNotNull(actualSession);
        assertEquals(validSessionId, actualSession.getId());
    }

    @Test
    void whenGetCourseSessionWithInvalidId_thenReturnsNull() {
        // Arrange
        String invalidSessionId = "invalidSessionId";
        when(courseSessionRepository.findCourseSessionById(invalidSessionId)).thenReturn(null);

        // expect error
        assertThrows(InvalidKeyException.class, () -> {
            courseSessionService.getCourseSession(invalidSessionId);
        });
    }

    @Test
    void whenDeleteSessionsForValidCourse_thenAllSessionsAreDeleted() {
        // Arrange
        String validCourseId = "validCourseId";
        Course mockCourse = mock(Course.class);
        when(courseRepository.findCourseById(validCourseId)).thenReturn(mockCourse);

        // Act
        String result = courseSessionService.deleteSessionsPerCourse(validCourseId);

        // Assert
        assertEquals("", result);
        verify(courseSessionRepository).deleteAllByCourseId(validCourseId);
    }

    @Test
    void whenDeleteSessionsForInvalidCourse_thenReturnsInvalidCourseIdMessage() {
        // Arrange
        String invalidCourseId = "invalidCourseId";
        when(courseRepository.findCourseById(invalidCourseId)).thenReturn(null);

        // Act
        String result = courseSessionService.deleteSessionsPerCourse(invalidCourseId);

        // Assert
        assertEquals("Invalid course id", result);
        verify(courseSessionRepository, never()).deleteAllByCourseId(invalidCourseId);
    }

    @Test
    void whenGetSessionsForValidCourse_thenListOfSessionsIsReturned() {
        // Arrange
        String validCourseId = "validCourseId";
        Course mockCourse = mock(Course.class);
        List<CourseSession> expectedSessions = List.of(mock(CourseSession.class), mock(CourseSession.class));

        when(courseRepository.findCourseById(validCourseId)).thenReturn(mockCourse);
        when(courseSessionRepository.findCourseSessionsByCourse(mockCourse)).thenReturn(expectedSessions);

        // Act
        List<CourseSession> result = courseSessionService.getSessionsPerCourse(validCourseId, new PersonSession("userId", PersonSession.PersonType.Owner, "sportCenterId"));

        // Assert
        assertEquals(expectedSessions, result);
    }

    @Test
    void whenGetSessionsForInvalidCourse_thenThrowsIllegalArgumentException() {
        // Arrange
        String invalidCourseId = "invalidCourseId";
        when(courseRepository.findCourseById(invalidCourseId)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            courseSessionService.getSessionsPerCourse(invalidCourseId, new PersonSession("userId", PersonSession.PersonType.Owner, "sportCenterId"));
        });
    }

    @Test
    void whenCreateSessionWithValidCourseAndOwner_thenSessionIsCreated() {
        // Arrange
        String validCourseId = "validCourseId";
        Course mockCourse = mock(Course.class);
        when(mockCourse.getSportCenter()).thenReturn(new SportCenter("sportCenterId"));
        when(courseRepository.findCourseById(validCourseId)).thenReturn(mockCourse);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.createCourseSession(validCourseId, ownerSession);

        // Assert
        assertEquals("", result);
        verify(courseSessionRepository).save(any(CourseSession.class));
    }

    @Test
    void whenCreateSessionWithInvalidCourseId_thenReturnsCourseNotFound() {
        // Arrange
        String invalidCourseId = "invalidCourseId";
        when(courseRepository.findCourseById(invalidCourseId)).thenReturn(null);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.createCourseSession(invalidCourseId, ownerSession);

        // Assert
        assertEquals("Course not found", result);
    }

    @Test
    void whenCreateSessionWithNonOwner_thenReturnsMustBeAnOwner() {
        // Arrange
        String validCourseId = "validCourseId";
        Course mockCourse = mock(Course.class);
        when(courseRepository.findCourseById(validCourseId)).thenReturn(mockCourse);

        PersonSession nonOwnerSession = new PersonSession("userId", PersonSession.PersonType.Customer, "sportCenterId");

        // Act
        String result = courseSessionService.createCourseSession(validCourseId, nonOwnerSession);

        // Assert
        assertEquals("Must be an owner", result);
    }

    @Test
    void whenCreateSessionWithMismatchedSportCenter_thenReturnsSessionMustBelongToTheSameSportsCenter() {
        // Arrange
        String validCourseId = "validCourseId";
        Course mockCourse = mock(Course.class);
        SportCenter sportCenter = new SportCenter("differentSportCenterId");
        when(mockCourse.getSportCenter()).thenReturn(sportCenter);
        when(courseRepository.findCourseById(validCourseId)).thenReturn(mockCourse);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.createCourseSession(validCourseId, ownerSession);

        // Assert
        assertEquals("Session must belong to the same sports center", result);
    }

    @Test
    void whenUpdateStartWithValidConditions_thenUpdateSuccessfully() {
        // Arrange
        CourseSession mockSession = mock(CourseSession.class);
        Course mockCourse = mock(Course.class);
        SportCenter mockSportCenter = mock(SportCenter.class);

        when(mockSession.getEndTime()).thenReturn(Timestamp.valueOf("2024-03-25 10:00:00"));
        when(mockSession.getCourse()).thenReturn(mockCourse);
        when(mockCourse.getSportCenter()).thenReturn(mockSportCenter);
        when(mockSportCenter.getId()).thenReturn("sportCenterId");

        when(courseSessionRepository.findCourseSessionById("sessionID")).thenReturn(mockSession);
        when(courseRepository.findCourseById(mockSession.getCourse().getId())).thenReturn(mockCourse);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.updateCourseSessionStart("sessionID", Timestamp.valueOf("2024-03-25 09:00:00"), ownerSession);

        // Assert
        assertEquals("", result);
        verify(courseSessionRepository).save(mockSession);
    }


    @Test
    void whenUpdateStartWithInvalidSessionId_thenSessionNotFound() {
        // Arrange
        when(courseSessionRepository.findCourseSessionById("invalidSessionID")).thenReturn(null);

        // Act
        String result = courseSessionService.updateCourseSessionStart("invalidSessionID", Timestamp.valueOf("2024-03-25 09:00:00"), new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId"));

        // Assert
        assertEquals("Course session not found", result);
    }


    @Test
    void whenUpdateStartAfterEndTime_thenInvalidStartTime() {
        // Arrange
        CourseSession mockSession = mock(CourseSession.class);
        Course mockCourse = mock(Course.class);
        SportCenter mockSportCenter = mock(SportCenter.class);

        when(mockSession.getEndTime()).thenReturn(Timestamp.valueOf("2024-03-25 09:00:00"));
        when(mockSession.getCourse()).thenReturn(mockCourse);
        when(mockCourse.getSportCenter()).thenReturn(mockSportCenter);
        when(mockSportCenter.getId()).thenReturn("sportCenterId");

        when(courseSessionRepository.findCourseSessionById("sessionID")).thenReturn(mockSession);
        when(courseRepository.findCourseById(mockSession.getCourse().getId())).thenReturn(mockCourse);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.updateCourseSessionStart("sessionID", Timestamp.valueOf("2024-03-25 10:00:00"), ownerSession);

    // Assert
    assertEquals("Start time must be before end time", result);
    }

    @Test
    void whenUpdateStartWithMismatchedSportCenter_thenSessionMustBelongToTheSameSportsCenter() {
        // Arrange
        CourseSession mockSession = mock(CourseSession.class);
        Course mockCourse = mock(Course.class);
        SportCenter mockSportCenter = mock(SportCenter.class);

        when(mockSession.getCourse()).thenReturn(mockCourse);
        when(mockCourse.getSportCenter()).thenReturn(mockSportCenter);
        when(mockSportCenter.getId()).thenReturn("different sport center");

        when(courseSessionRepository.findCourseSessionById("sessionID")).thenReturn(mockSession);
        when(courseRepository.findCourseById(mockSession.getCourse().getId())).thenReturn(mockCourse);

        // Act
        String result = courseSessionService.updateCourseSessionStart("sessionID", Timestamp.valueOf("2024-03-25 08:00:00"), new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId"));

        // Assert
        assertEquals("Session must belong to the same sports center", result);
    }

    @Test
    void whenUpdateEndWithValidConditions_thenUpdateSuccessfully() {
        // Arrange
        CourseSession mockSession = mock(CourseSession.class);
        Course mockCourse = mock(Course.class);
        SportCenter mockSportCenter = mock(SportCenter.class);

        when(mockSession.getStartTime()).thenReturn(Timestamp.valueOf("2024-03-25 10:00:00"));
        when(mockSession.getCourse()).thenReturn(mockCourse);
        when(mockCourse.getSportCenter()).thenReturn(mockSportCenter);
        when(mockSportCenter.getId()).thenReturn("sportCenterId");

        when(courseSessionRepository.findCourseSessionById("sessionID")).thenReturn(mockSession);
        when(courseRepository.findCourseById(mockSession.getCourse().getId())).thenReturn(mockCourse);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.updateCourseSessionEnd("sessionID", Timestamp.valueOf("2024-03-25 11:00:00"), ownerSession);

        // Assert
        assertEquals("", result);
        verify(courseSessionRepository).save(mockSession);
    }


    @Test
    void whenUpdateEndWithInvalidSessionId_thenSessionNotFound() {
        // Arrange
        when(courseSessionRepository.findCourseSessionById("invalidSessionID")).thenReturn(null);

        // Act
        String result = courseSessionService.updateCourseSessionEnd("invalidSessionID", Timestamp.valueOf("2024-03-25 09:00:00"), new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId"));

        // Assert
        assertEquals("Course session not found", result);
    }


    @Test
    void whenUpdateEndBeforeStartTime_thenInvalidStartTime() {
        // Arrange
        CourseSession mockSession = mock(CourseSession.class);
        Course mockCourse = mock(Course.class);
        SportCenter mockSportCenter = mock(SportCenter.class);

        when(mockSession.getStartTime()).thenReturn(Timestamp.valueOf("2024-03-25 09:00:00"));
        when(mockSession.getCourse()).thenReturn(mockCourse);
        when(mockCourse.getSportCenter()).thenReturn(mockSportCenter);
        when(mockSportCenter.getId()).thenReturn("sportCenterId");

        when(courseSessionRepository.findCourseSessionById("sessionID")).thenReturn(mockSession);
        when(courseRepository.findCourseById(mockSession.getCourse().getId())).thenReturn(mockCourse);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.updateCourseSessionEnd("sessionID", Timestamp.valueOf("2024-03-25 08:00:00"), ownerSession);

        // Assert
        assertEquals("End time must be after start time", result);
    }

    @Test
    void whenUpdateEndWithMismatchedSportCenter_thenSessionMustBelongToTheSameSportsCenter() {
        // Arrange
        CourseSession mockSession = mock(CourseSession.class);
        Course mockCourse = mock(Course.class);
        SportCenter mockSportCenter = mock(SportCenter.class);

        when(mockSession.getCourse()).thenReturn(mockCourse);
        when(mockCourse.getSportCenter()).thenReturn(mockSportCenter);
        when(mockSportCenter.getId()).thenReturn("different sport center");

        when(courseSessionRepository.findCourseSessionById("sessionID")).thenReturn(mockSession);
        when(courseRepository.findCourseById(mockSession.getCourse().getId())).thenReturn(mockCourse);

        // Act
        String result = courseSessionService.updateCourseSessionEnd("sessionID", Timestamp.valueOf("2024-03-25 08:00:00"), new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId"));

        // Assert
        assertEquals("Session must belong to the same sports center", result);
    }

    @Test
    void whenDeleteSessionByOwner_thenDeleteSuccessfully() {
        // Arrange
        CourseSession session = mock(CourseSession.class);
        Course course = mock(Course.class);
        SportCenter sportCenter = mock(SportCenter.class);
        String sessionId = "sessionID";

        when(courseSessionRepository.findCourseSessionById(sessionId)).thenReturn(session);
        when(session.getCourse()).thenReturn(course);
        when(courseRepository.findCourseById(session.getCourse().getId())).thenReturn(course);
        when(session.getId()).thenReturn(sessionId);
        when(course.getSportCenter()).thenReturn(sportCenter);
        when(sportCenter.getId()).thenReturn("sportCenterId");

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.deleteCourseSession(sessionId, ownerSession);

        // Assert
        assertEquals("", result);
        verify(courseSessionRepository).deleteById(sessionId);
    }

    @Test
    void whenDeleteWithInvalidSessionId_thenSessionNotFound() {
        // Arrange
        CourseSession session = mock(CourseSession.class);
        Course course = mock(Course.class);
        String sessionId = null;

        when(courseSessionRepository.findCourseSessionById(sessionId)).thenReturn(session);
        when(session.getCourse()).thenReturn(course);
        when(courseRepository.findCourseById(session.getCourse().getId())).thenReturn(course);
        when(session.getId()).thenReturn(sessionId);

        PersonSession ownerSession = new PersonSession("ownerId", PersonSession.PersonType.Owner, "sportCenterId");

        // Act
        String result = courseSessionService.deleteCourseSession(sessionId, ownerSession);

        // Assert
        assertEquals("Course session not found", result);
    }

    @Test
    void whenDeleteSessionByNonOwner_thenMustBeAnOwner() {
        // Arrange
        CourseSession session = mock(CourseSession.class);
        Course course = mock(Course.class);
        String sessionId = "validId";

        when(courseSessionRepository.findCourseSessionById(sessionId)).thenReturn(session);
        when(session.getCourse()).thenReturn(course);
        when(courseRepository.findCourseById(session.getCourse().getId())).thenReturn(course);
        PersonSession nonOwnerSession = new PersonSession("nonOwnerId", PersonSession.PersonType.Instructor, "sportCenterId");

        // Act
        String result = courseSessionService.deleteCourseSession(sessionId, nonOwnerSession);

        // Assert
        assertEquals("Must be an owner", result);
    }


}

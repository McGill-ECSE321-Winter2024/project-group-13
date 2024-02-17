package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseSessionRepositoryTest {


    @Autowired
    private CourseSessionRepository courseSessionRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course testCourse;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        testCourse = new Course();
        testCourse.setName("TestCourse");
        testCourse.setCourseStartDate(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 8, 0)));
        testCourse.setCourseEndDate(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 10, 0)));
        testCourse.setDescription("Test Description");
        testCourse.setLevel(Course.Level.Beginner);
        courseRepository.save(testCourse);

        // Creating 10 courses with the same timing but different days
        for(int day = 1; day < 11; day++){
            CourseSession session = new CourseSession();
            session.setStartTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, day, 9, 0)));
            session.setEndTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, day, 11, 0)));
            session.setCourse(testCourse);
            courseSessionRepository.save(session);
        }
    }

    @AfterEach
    public void clearDatabase() {
        courseSessionRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    public void testFindCourseSessionsByCourse() {
        // Fetch all sessions for the test course
        List<CourseSession> sessions = courseSessionRepository.findCourseSessionsByCourse(testCourse);

        // Assert that the number of fetched sessions matches the expected number
        assertEquals(10, sessions.size(), "Should find 10 sessions for the test course");

        // Checking that all sessions belong to the test course
        assertTrue(sessions.stream().allMatch(session -> session.getCourse().getId().equals(testCourse.getId())),
                "All fetched sessions should belong to the test course");
    }


    @Test
    public void testFindSessionsByStartTimeBefore() {
        // Fetch sessions that start before the end of the first day
        List<CourseSession> sessionsBeforeEndOfFirstDay = courseSessionRepository.findCourseSessionsByStartTimeBefore(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 2, 0, 0))
        );

        // Since sessions start at 9:00, this should include only the first day's session
        assertEquals(1, sessionsBeforeEndOfFirstDay.size(), "Should find 1 session that starts before the end of the first day");
    }

    @Test
    public void testFindSessionsByStartTimeAfter() {
        // Fetch sessions that start after the beginning of the 1st day (effectively, sessions from day 2 onwards)
        List<CourseSession> sessionsAfterStartOfFirstDay = courseSessionRepository.findCourseSessionsByStartTimeAfter(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0))
        );

        // Since there are sessions on days 1 through 10, and we're fetching from after the start of day 1, we should get all 10 sessions
        assertEquals(10, sessionsAfterStartOfFirstDay.size(), "Should find all sessions starting after the beginning of the first day");
    }

    @Test
    public void testFindSessionsByStartTimeBetween() {
        // Fetch sessions for the first day
        List<CourseSession> firstDaySessions = courseSessionRepository.findCourseSessionsByStartTimeBetween(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 2, 0, 0))
        );
        assertEquals(1, firstDaySessions.size(), "Should find 1 session on the first day");

        // Fetch all sessions across 10 days
        List<CourseSession> allSessions = courseSessionRepository.findCourseSessionsByStartTimeBetween(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 11, 0, 0)) // Adjusted to cover all 10 days
        );
        assertEquals(10, allSessions.size(), "Should find 10 sessions in total");
    }

    @Test
    public void testCountByCourse() {
        long sessionCount = courseSessionRepository.countByCourse(testCourse);
        assertEquals(10, sessionCount);
    }

    @Test
    @Transactional
    public void testUpdateSessionTimes() {
        // Preparation: Create and persist a CourseSession
        CourseSession session = new CourseSession();
        Timestamp initialStartTime = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 9, 0));
        Timestamp initialEndTime = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 11, 0));
        session.setStartTime(initialStartTime);
        session.setEndTime(initialEndTime);
        session = courseSessionRepository.save(session); // Persist and then find to ensure it's managed

        // New times for update
        Timestamp newStartTime = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 10, 0));
        Timestamp newEndTime = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 12, 0));

        session.setStartTime(newStartTime);
        session.setEndTime(newEndTime);
        // Execution: Update the session times
        courseSessionRepository.save(session);


        // Verification: Retrieve the updated session and verify the changes
        CourseSession updatedSession = courseSessionRepository.findCourseSessionById(session.getId());
        assertEquals(newStartTime, updatedSession.getStartTime(), "The start time should be updated.");
        assertEquals(newEndTime, updatedSession.getEndTime(), "The end time should be updated.");
    }

    @Test
    @Transactional
    public void testDeleteById() {
        // Preparation: Create and persist a CourseSession
        CourseSession session = new CourseSession();
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 9, 0));
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 11, 0));
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        // Assuming a Course instance is required and has been set up appropriately
        Course course = new Course(); // Setup course as needed
        courseRepository.save(course); // Persist course if it's a part of the CourseSession
        session.setCourse(course);
        session = courseSessionRepository.save(session);

        // Ensure the session is persisted
        Optional<CourseSession> persistedSession = courseSessionRepository.findById(session.getId());
        assertTrue(persistedSession.isPresent(), "CourseSession should exist before deletion.");

        // Execution: Delete the session by id
        courseSessionRepository.deleteById(session.getId());

        // Verification: Check the session no longer exists
        Optional<CourseSession> deletedSession = courseSessionRepository.findById(session.getId());
        assertFalse(deletedSession.isPresent(), "CourseSession should not exist after deletion.");
    }

    @Test
    @Transactional
    public void testDeleteByCourse() {
        // Given the setUp method, we have a course and its associated sessions

        // When we delete sessions by the course
        courseSessionRepository.deleteByCourse(testCourse);

        // Then no sessions should exist for the course
        List<CourseSession> sessionsAfterDeletion = courseSessionRepository.findCourseSessionsByCourse(testCourse);
        assertTrue(sessionsAfterDeletion.isEmpty(), "No CourseSession entities should exist for the course after deletion.");
    }}

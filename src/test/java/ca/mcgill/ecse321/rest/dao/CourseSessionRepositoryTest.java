package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CourseSessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseSessionRepository repository;

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
        entityManager.persist(testCourse);

        // Creating 10 courses with the same timing but different days
        for(int day = 1; day < 11; day++){
            CourseSession session = new CourseSession();
            session.setStartTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, day, 9, 0)));
            session.setEndTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, day, 11, 0)));
            session.setCourse(testCourse);
            entityManager.persist(session);
        }
    }


    @Test
    public void testSessionCounts() {
        // Fetch sessions for the first day
        List<CourseSession> firstDaySessions = repository.findCourseSessionsByStartTimeBetween(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 2, 0, 0))
        );
        assertEquals(1, firstDaySessions.size(), "Should find 1 session on the first day");

        // Fetch all sessions across 10 days
        List<CourseSession> allSessions = repository.findCourseSessionsByStartTimeBetween(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 11, 0, 0)) // Adjusted to cover all 10 days
        );
        assertEquals(10, allSessions.size(), "Should find 10 sessions in total");
    }

//
//    @Test
//    public void testFindByInstructorId() {
//        // Assuming you have an instructor linked to the course
//        // You need to persist an Instructor and link it to the testCourse before this test
//        List<CourseSession> sessions = repository.findByInstructorId("instructorId"); // Replace with actual instructor ID after setup
//        assertFalse(sessions.isEmpty());
//    }
//
//    @Test
//    public void testCountByCourse() {
//        long sessionCount = repository.countByCourse(testCourse);
//        assertEquals(1, sessionCount);
//    }
//
//    @Test
//    public void testUpdateSessionTimes() {
//        CourseSession session = repository.findCourseSessionByCourse(testCourse).get();
//        Timestamp newStartTime = Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 10, 0));
//        Timestamp newEndTime = Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 12, 0));
//        repository.updateSessionTimes(session.getId(), newStartTime, newEndTime);
//
//        CourseSession updatedSession = entityManager.find(CourseSession.class, session.getId());
//        assertEquals(newStartTime, updatedSession.getStartTime());
//        assertEquals(newEndTime, updatedSession.getEndTime());
//    }

    // Additional tests for deleteById, deleteByCourse, etc., can be implemented following a similar pattern
}

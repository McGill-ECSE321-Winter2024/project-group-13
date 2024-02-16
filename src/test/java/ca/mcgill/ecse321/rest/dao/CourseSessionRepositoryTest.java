package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.LocalDateTime;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CourseSessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseSessionRepository repository;

    private Course testCourse = new Course();

    @BeforeEach
    public void setUp() {
        testCourse.setName("TestCourse");
        testCourse.setCourseStartDate(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 8, 0)));
        testCourse.setCourseEndDate(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 10, 0)));
        testCourse.setDescription("");
        testCourse.setLevel(Course.Level.Beginner);

        entityManager.persist(testCourse);

        System.out.println("Course ID: " + testCourse.getId());
        // Create and persist a CourseSession instance linked to the test course
        CourseSession session = new CourseSession(); // Use the appropriate constructor or setter methods
        session.setStartTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 8, 0)));
        session.setEndTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 10, 0)));
        // Assuming setter methods to set properties
        session.setCourse(testCourse);
        // Set other properties of session as needed
        entityManager.persist(session);
    }


    @Test
    public void whenFindByCourse_thenReturnsSessions() {
        List<CourseSession> sessions = repository.findCourseSessionsByCourse(testCourse);
        assertThat(sessions).hasSize(1); // Assuming you only set up one session for the course
    }

    @Test
    public void whenFindByDay_thenReturnsSessions() {
        Timestamp testDate = Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 8, 0));
        List<CourseSession> sessions = repository.findCourseSessionByStartTime(testDate);
        assertThat(sessions).isNotEmpty(); // Check that sessions are returned for the given date
    }
}

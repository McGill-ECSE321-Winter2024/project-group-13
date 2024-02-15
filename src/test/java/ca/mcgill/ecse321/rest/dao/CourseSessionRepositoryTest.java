package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Date;
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
        testCourse.setCourseStartDate(new Date(2024, 1, 1));
        testCourse.setCourseEndDate(new Date(2024, 5, 1));
        testCourse.setDescription("");
        testCourse.setLevel(Course.Level.Beginner);

        entityManager.persist(testCourse);
        // Create and persist a CourseSession instance linked to the test course
        CourseSession session = new CourseSession(); // Use the appropriate constructor or setter methods
        session.setDay(Date.valueOf("2023-01-01"));
        // Assuming setter methods to set properties
        session.setCourse(testCourse);
        // Set other properties of session as needed
        entityManager.persist(session);
    }


    @Test
    public void whenFindByCourse_thenReturnsSessions() {
        List<CourseSession> sessions = repository.findByCourse(testCourse);
        assertThat(sessions).hasSize(1); // Assuming you only set up one session for the course
    }

    @Test
    public void whenFindByDay_thenReturnsSessions() {
        Date testDate = Date.valueOf("2023-01-01");
        // Assuming findByDay exists and is correctly implemented in the repository
        List<CourseSession> sessions = repository.findByDayOfWeek(testDate);
        assertThat(sessions).isNotEmpty(); // Check that sessions are returned for the given date
    }
}

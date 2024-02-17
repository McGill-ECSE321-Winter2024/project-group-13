package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.rest.models.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Timestamp;

@SpringBootTest
public class CourseRepositoryTests {
    @Autowired
    private CourseRepository courseRepository;

    @AfterEach
    public void clearDatabase() {
        courseRepository.deleteAll();
    }

    @Test
    public void testCreateCourse() {
        // Create course.
        String name= "Health Plus";
        String description= "The best sports center";
        Course.Level level= Course.Level.Advanced;
        Timestamp courseStartDate= Timestamp.valueOf("2023-01-01 08:00:00");
        Timestamp courseEndDate= Timestamp.valueOf("2023-01-01 10:00:00");
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setLevel(level);
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);

        // Save person
        courseRepository.save(course);

        // Read course from database.
        course = courseRepository.findCourseByName(name);

        // Assert that course is not null and has correct attributes.
        assertNotNull(course);
        assertEquals(name, course.getName());
        assertEquals(description, course.getDescription());
        assertEquals(level, course.getLevel());
        assertEquals(name, course.getName());
        assertEquals(name, course.getName());
    }

}
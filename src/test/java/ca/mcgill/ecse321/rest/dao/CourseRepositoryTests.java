package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.rest.models.Course;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
public class CourseRepositoryTests {
    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void makeCourse() {
        String name= "Health Plus";
        String description= "The best sports center";
        Course.Level level= Course.Level.Advanced;
        Date courseStartDate= new Date(2024,1,1);
        Date courseEndDate= new Date(2024,1,1);
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setLevel(level);
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);

        // Save Course
        courseRepository.save(course);
    }

    @AfterEach
    public void clearDatabase() {
        courseRepository.deleteAll();
    }

    @Test
    public void testReadWriteCourse() {
        // Retrieve course from database.
        String name= "Health Plus";
        Course course = courseRepository.findCourseByName( name);
        String description= "The best sports center";
        Course.Level level= Course.Level.Advanced;
        Date courseStartDate= new Date(2024,1,1);
        Date courseEndDate= new Date(2024,1,1);

        // Assert that course is not null and has correct attributes.
        assertNotNull(course);
        assertEquals(name, course.getName());
        assertEquals(description, course.getDescription());
        assertEquals(level, course.getLevel());
        assertEquals(name, course.getName());
        assertEquals(name, course.getName());
    }

    @Test
    public void testEditCourseName() {
        //get course
        Course course = courseRepository.findCourseByName( "Health Plus");
        assertNotNull(course);
        course.setName("A random Name");
        // Save person
        courseRepository.save(course);
        assertEquals("A random Name", course.getName());
    }

}
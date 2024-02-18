package ca.mcgill.ecse321.rest.dao;

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
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseRepositoryTests {
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Makes a course before every test, setting most of its attributes
     * @author Mohamed Abdelrahman
     */
    @BeforeEach
    public void makeCourse() {
        String name= "Health Plus";
        String description= "The best sports center";
        Course.Level level= Course.Level.Advanced;
        Timestamp courseStartDate = new Timestamp(2024,1,1,6,15,0,0);
        Timestamp courseEndDate = new Timestamp(2024,1,1,6,15,0,0);
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setLevel(level);
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);

        // Save Course
        courseRepository.save(course);
    }
    /**
     * Clears all objets in the database after each test
     * @author Mohamed Abdelrahman
     */
    @AfterEach
    public void clearDatabase() {
        courseRepository.deleteAll();
    }

    /**
     * Meant to ensure successful course creation from makeCourse() and
     * that we can read the attributes in existing courses
     * @author Mohamed Abdelrahman
     */
    @Test
    public void testReadWriteCourse() {
        // Retrieve course from database.
        String name= "Health Plus";
        Course course = courseRepository.findCourseByName(name);
        // Assert that course is not null and has correct attributes.
        assertNotNull(course);

        String description= "The best sports center";
        Course.Level level= Course.Level.Advanced;
        Timestamp courseStartDate = new Timestamp(2024,1,1,6,15,0,0);
        Timestamp courseEndDate = new Timestamp(2024,1,1,6,15,0,0);

        assertEquals(name, course.getName());
        assertEquals(description, course.getDescription());
        assertEquals(level, course.getLevel());
        assertEquals(courseStartDate, course.getCourseStartDate());
        assertEquals(courseEndDate, course.getCourseEndDate());
    }

    /**
     * Meant to ensure that we can read and update the attributes in existing courses
     * @author Mohamed Abdelrahman
      */
    @Test
    public void testEditCourseAttributes() {
        //get course
        Course course = courseRepository.findCourseByName( "Health Plus");
        assertNotNull(course);
        String name= "New Center";
        String description= "The best yoga course";
        Course.Level level= Course.Level.Beginner;
        Timestamp courseStartDate = new Timestamp(2024,2,15,8,10,15,1);
        Timestamp courseEndDate = new Timestamp(2024,4,1,10,10,15,1);

        course.setName(name);
        course.setDescription(description);
        course.setLevel(level);
        course.setCourseStartDate(courseStartDate);
        course.setCourseEndDate(courseEndDate);

        // Save Course
        courseRepository.save(course);
        Course course1= courseRepository.findCourseByName("Health Plus");
        assertNull(course1);
        course = courseRepository.findCourseByName(name);
        assertNotNull(course);
        assertEquals(name, course.getName());
        assertEquals(description, course.getDescription());
        assertEquals(level, course.getLevel());
        assertEquals(name, course.getName());
        assertEquals(name, course.getName());
    }

    /**
     * Validates course deletion capabilities
     * @author Mohamed Abdelrahman
     */
    @Test
    public void testDeleteCourse(){
        Course course= courseRepository.findCourseByName("Health Plus");
        assertNotNull(course);
        courseRepository.deleteCourseById(course.getId());
        //Attempt to retrieve deleted course
        course= courseRepository.findCourseByName("Health Plus");
        //Assume course is null
        assertNull(course);
    }

}
package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.models.Course;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseRepositoryTests {
  @Autowired private CourseRepository courseRepository;

  /**
   * Makes a course before every test, setting most of its attributes
   *
   * @author Mohamed Abdelrahman
   */
  @BeforeEach
  public void makeCourse() {
    String name = "Health Plus";
    String description = "The best sports center";
    Course.Level level = Course.Level.Advanced;
    Timestamp courseStartDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);
    Timestamp courseEndDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);
    Course.CourseState courseState = Course.CourseState.AwaitingApproval;

    Course course = new Course();
    course.setName(name);
    course.setDescription(description);
    course.setLevel(level);
    course.setCourseStartDate(courseStartDate);
    course.setCourseEndDate(courseEndDate);
    course.setCourseState(courseState);

    // Save Course
    courseRepository.save(course);
  }

  /**
   * Clears all objets in the database after each test
   *
   * @author Mohamed Abdelrahman
   */
  @AfterEach
  public void clearDatabase() {
    courseRepository.deleteAll();
  }

  /**
   * Meant to ensure successful course creation from makeCourse() and that we can read the
   * attributes in existing courses
   *
   * @author Mohamed Abdelrahman
   */
  @Test
  public void testReadWriteCourse() {
    // Retrieve course from database.
    String name = "Health Plus";
    Course course = courseRepository.findCourseByName(name);
    // Assert that course is not null and has correct attributes.
    assertNotNull(course);

    String description = "The best sports center";
    Course.Level level = Course.Level.Advanced;
    Timestamp courseStartDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);
    Timestamp courseEndDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);
    Course.CourseState courseState = Course.CourseState.AwaitingApproval;

    assertEquals(name, course.getName());
    assertEquals(description, course.getDescription());
    assertEquals(level, course.getLevel());
    assertEquals(courseStartDate, course.getCourseStartDate());
    assertEquals(courseEndDate, course.getCourseEndDate());
    assertEquals(courseState, course.getCourseState());
  }

  /**
   * Meant to ensure that we can read and update the attributes in existing courses
   *
   * @author Mohamed Abdelrahman
   */
  @Test
  public void testEditCourseAttributes() {
    // get course
    Course course = courseRepository.findCourseByName("Health Plus");
    assertNotNull(course);
    String name = "New Center";
    String description = "The best yoga course";
    Course.Level level = Course.Level.Beginner;
    Timestamp courseStartDate = new Timestamp(2024, 2, 15, 8, 10, 15, 0);
    Timestamp courseEndDate = new Timestamp(2024, 4, 1, 10, 10, 15, 0);
    Course.CourseState courseState = Course.CourseState.Approved;

    course.setName(name);
    course.setDescription(description);
    course.setLevel(level);
    course.setCourseStartDate(courseStartDate);
    course.setCourseEndDate(courseEndDate);
    course.setCourseState(courseState);

    // Save Course
    courseRepository.save(course);
    Course course1 = courseRepository.findCourseByName("Health Plus");
    assertNull(course1);
    course = courseRepository.findCourseByName(name);
    assertNotNull(course);
    assertEquals(name, course.getName());
    assertEquals(description, course.getDescription());
    assertEquals(level, course.getLevel());
    assertEquals(courseStartDate, course.getCourseStartDate());
    assertEquals(courseEndDate, course.getCourseEndDate());
    assertEquals(courseState, course.getCourseState());
  }

  /** */
  @Test
  public void testFindCoursesByCourseState() {
    Course.CourseState courseState = Course.CourseState.Approved;
    Course course = courseRepository.findCourseByName("Health Plus");
    course.setName("First Course");
    course.setCourseState(courseState);
    courseRepository.save(course);
    makeCourse();
    Course course1 = courseRepository.findCourseByName("Health Plus");
    course1.setName("Second Course");
    course1.setCourseState(courseState);
    courseRepository.save(course1);
    assertNotEquals(course.getId(), course1.getId());

    List<Course> myApprovedCourses = new ArrayList<>();
    myApprovedCourses.add(course);
    myApprovedCourses.add(course1);
    List<Course> systemApprovedCourses = courseRepository.findCoursesByCourseState(courseState);
    // Still needs implementation
    //        for (int i=0;i<systemApprovedCourses.size();i++){
    //            System.out.println(myApprovedCourses.contains(systemApprovedCourses.get(i)));
    //        }
    assertEquals(myApprovedCourses.size(), systemApprovedCourses.size());
  }

  /**
   * Validates course deletion capabilities
   *
   * @author Mohamed Abdelrahman
   */
  @Test
  public void testDeleteCourse() {
    Course course = courseRepository.findCourseByName("Health Plus");
    assertNotNull(course);
    courseRepository.deleteCourseById(course.getId());
    // Attempt to retrieve deleted course
    course = courseRepository.findCourseByName("Health Plus");
    // Assume course is null
    assertNull(course);
  }
}

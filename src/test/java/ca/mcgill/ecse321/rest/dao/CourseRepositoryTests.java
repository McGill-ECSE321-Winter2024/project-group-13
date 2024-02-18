package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CourseRepositoryTests {
  @Autowired private CourseRepository courseRepository;
  @Autowired private CourseSessionRepository courseSessionRepository;

  /**
   * Sets the attributes of a course, saves it to the database, and ensures persistence.
   *
   * @param course The course whose attributes are to be set and saved.
   * @param name The new name for the course.
   * @param description The new description for the course.
   * @param level The new level for the course.
   * @param courseStartDate The new start date for the course.
   * @param courseEndDate The new end date for the course.
   * @param courseState The new state for the course.
   * @author Mohamed Abdelrahman
   */
  public void setAttributes(
      Course course,
      String name,
      String description,
      Course.Level level,
      Timestamp courseStartDate,
      Timestamp courseEndDate,
      Course.CourseState courseState) {
    // Set the new attributes for the course
    course.setName(name);
    course.setDescription(description);
    course.setLevel(level);
    course.setCourseStartDate(courseStartDate);
    course.setCourseEndDate(courseEndDate);
    course.setCourseState(courseState);

    // Save the modified course to the database
    courseRepository.save(course);
  }

  /**
   * Initializes and creates a sample course with predefined attributes for testing purposes. This
   * method is executed before each test method annotated with {@code @Test}.
   *
   * @author Mohamed Abdelrahman
   */
  @BeforeEach
  public void makeCourse() {
    // Define predefined attributes for the sample course
    String name = "Health Plus";
    String description = "The best sports center";
    Course.Level level = Course.Level.Advanced;
    Timestamp courseStartDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);
    Timestamp courseEndDate = new Timestamp(2024, 1, 1, 6, 15, 0, 0);
    Course.CourseState courseState = Course.CourseState.AwaitingApproval;

    // Create a new Course instance
    Course course = new Course();

    // Set attributes and save the course to the database
    setAttributes(course, name, description, level, courseStartDate, courseEndDate, courseState);
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

    // check attribute values
    assertEquals(name, course.getName());
    assertEquals(description, course.getDescription());
    assertEquals(level, course.getLevel());
    assertEquals(courseStartDate, course.getCourseStartDate());
    assertEquals(courseEndDate, course.getCourseEndDate());
    assertEquals(courseState, course.getCourseState());
  }

  /**
   * Test goal: Tests the functionality of editing course attributes. This test verifies that the
   * attributes of a course can be modified, saved to the database, and retrieved correctly with the
   * updated values.
   *
   * @author Mohamed Abdelrahman
   */
  @Test
  public void testEditCourseAttributes() {
    // Retrieve an existing course from the database
    Course course = courseRepository.findCourseByName("Health Plus");
    assertNotNull(course);

    // Define new attribute values
    String name = "New Center";
    String description = "The best yoga course";
    Course.Level level = Course.Level.Beginner;
    Timestamp courseStartDate = new Timestamp(2024, 2, 15, 8, 10, 15, 0);
    Timestamp courseEndDate = new Timestamp(2024, 4, 1, 10, 10, 15, 0);
    Course.CourseState courseState = Course.CourseState.Approved;

    // Set the new attribute values for the course
    setAttributes(course, name, description, level, courseStartDate, courseEndDate, courseState);

    // Ensure that the course with the original name is not found
    Course course1 = courseRepository.findCourseByName("Health Plus");
    assertNull(course1);

    // Retrieve the modified course by its new name
    course = courseRepository.findCourseByName(name);
    assertNotNull(course);

    // Assert that the attributes of the course match the expected values
    assertEquals(name, course.getName());
    assertEquals(description, course.getDescription());
    assertEquals(level, course.getLevel());
    assertEquals(courseStartDate, course.getCourseStartDate());
    assertEquals(courseEndDate, course.getCourseEndDate());
    assertEquals(courseState, course.getCourseState());
  }

  /**
   * Test goal: Tests the functionality of finding courses by their approval state. This test
   * verifies that courses with a specific approval state are retrieved correctly from the database
   * and that their attributes match the expected values.
   *
   * @author Mohamed Abdelrahman
   */
  @Test
  public void testFindCoursesByCourseState() {
    // Set the approval state for testing
    Course.CourseState courseState = Course.CourseState.Approved;

    // Create and save the first course with the specified approval state
    Course course = courseRepository.findCourseByName("Health Plus");
    course.setName("First Course");
    course.setCourseState(courseState);
    courseRepository.save(course);

    // Create another course with the same name but different attributes
    makeCourse();
    Course course1 = courseRepository.findCourseByName("Health Plus");
    course1.setName("Second Course");
    course1.setCourseState(courseState);
    courseRepository.save(course1);

    // Ensure that the IDs of the two courses are not equal
    assertNotEquals(course.getId(), course1.getId());

    // Create a list of locally approved courses
    List<Course> myApprovedCourses = new ArrayList<>();
    myApprovedCourses.add(course);
    myApprovedCourses.add(course1);

    // Retrieve approved courses from the system (database)
    List<Course> systemApprovedCourses = courseRepository.findCoursesByCourseState(courseState);

    // Assert that the sizes of the two lists are equal
    assertEquals(myApprovedCourses.size(), systemApprovedCourses.size());

    // Iterate over both lists and compare the IDs of the courses
    for (int i = 0; i < systemApprovedCourses.size(); i++) {
      assertEquals(myApprovedCourses.get(i).getId(), systemApprovedCourses.get(i).getId());
    }
  }

  /**
   * Validates course deletion capabilities
   *
   * @author Mohamed Abdelrahman, Omar Moussa
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

  /**
   * Creates and persists 10 course sessions related to a given course
   *
   * @author Mohamed Abdelrahman, Omar Moussa
   */
  private void createTenSessions(Course course){
    // Creating 10 courses with the same timing but different days
    for (int day = 1; day < 11; day++) {
      CourseSession session = new CourseSession();
      session.setStartTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, day, 9, 0)));
      session.setEndTime(Timestamp.valueOf(LocalDateTime.of(2023, 1, day, 11, 0)));
      session.setCourse(course);
      courseSessionRepository.save(session);
    }
  }

  /**
   * Validates course deletion capabilities along with associated sessions
   *
   * @author Mohamed Abdelrahman, Omar Moussa
   */
  @Test
  @Transactional
  public void testDeleteCourseAndAssociatedSessions() {
    Course course = courseRepository.findCourseByName("Health Plus");
    createTenSessions(course);

    assertNotNull(course);
    long courseSessionsCount = courseSessionRepository.countByCourse(course);
    assertEquals(10, courseSessionsCount);

    //Delete course and course sessions
    courseSessionRepository.deleteAllByCourseId(course.getId());
    courseRepository.deleteCourseById(course.getId());
    // Then no sessions should exist for the course
    List<CourseSession> sessionsAfterDeletion = courseSessionRepository.findCourseSessionsByCourse(course);
    assertTrue(
            sessionsAfterDeletion.isEmpty(),
            "No CourseSession entities should exist for the course after deletion.");
    // Attempt to retrieve deleted course
    course = courseRepository.findCourseByName("Health Plus");
    // Assume course is null
    assertNull(course);
  }
}

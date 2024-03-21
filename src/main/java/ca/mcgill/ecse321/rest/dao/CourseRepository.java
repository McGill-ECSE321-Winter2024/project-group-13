package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Room;
import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, String> {

  Course findCourseByName(String name);
  Course findCourseById(String id);

  List<Course> findCoursesByCourseState(Course.CourseState courseState);

  List<Course> findCoursesByInstructorId(String instructorId);
  List<Course> findCoursesByRoomName(String roomName);

  @Transactional
  void deleteCourseById(String id);
}

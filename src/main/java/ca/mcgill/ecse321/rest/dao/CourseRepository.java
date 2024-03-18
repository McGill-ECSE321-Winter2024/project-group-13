package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, String> {

  Course findCourseByName(String name);

  List<Course> findCoursesByCourseState(Course.CourseState courseState);

  @Transactional
  void deleteCourseById(String id);
}

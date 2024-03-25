package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends CrudRepository<Course, String> {

  Course findCourseByName(String name);
  Course findCourseById(String id);

  List<Course> findCoursesByCourseState(Course.CourseState courseState);

  List<Course> findCoursesByInstructorId(String instructorId);
  List<Course> findCoursesByRoomId(String roomId);


  @Query("SELECT c FROM Course c WHERE " +
          "(:state IS NULL OR c.courseState = :state) " +
          "AND (:instructorId IS NULL OR c.instructor.id = :instructorId) " +
          "AND ((cast(:startDate as date) IS NULL) OR c.courseStartDate >= :startDate)")
  List<Course> findCoursesByFilters(@Param("state") Course.CourseState state,
                                    @Param("instructorId") String instructorId,
                                    @Param("startDate") Timestamp startDate);

  @Transactional
  void deleteCourseById(String id);
}

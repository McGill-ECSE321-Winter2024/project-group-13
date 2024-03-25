package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CourseSessionRepository extends CrudRepository<CourseSession, String> {

  //
  CourseSession findCourseSessionById(String id);

  // Find all sessions for a specific course
  List<CourseSession> findCourseSessionsByCourse(Course course);

  // Overloaded methods to find sessions by date
  List<CourseSession> findCourseSessionsByStartTimeBefore(Timestamp endTime);

  List<CourseSession> findCourseSessionsByStartTimeAfter(Timestamp startTime);

  List<CourseSession> findCourseSessionsByStartTimeBetween(Timestamp start, Timestamp end);
  List<CourseSession> findCourseSessionByCourseRoomId(String roomID);

  // Count sessions for a specific course
  long countByCourse(Course course);

  // Deletion
  void deleteById(String id);

  void deleteAllByCourseId(String courseId);
}

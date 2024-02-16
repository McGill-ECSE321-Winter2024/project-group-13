package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface CourseSessionRepository extends CrudRepository<CourseSession, String> {

    // Find all sessions for a specific course
    List<CourseSession> findCourseSessionsByCourse(Course course);

    // Overloaded methods to find sessions by date
    List<CourseSession> findCourseSessionsByStartTimeBefore(Timestamp endTime);
    List<CourseSession> findCourseSessionsByStartTimeAfter(Timestamp startTime);
    List<CourseSession> findCourseSessionsByStartTimeBetween(Timestamp start, Timestamp end);

    // Find sessions by Instructor ID
    @Query("SELECT cs FROM CourseSession cs WHERE cs.course.instructor.id = :instructorId")
    List<CourseSession> findByInstructorId(@Param("instructorId") String instructorId);

    // Count sessions for a specific course
    long countByCourse(Course course);

    // Update session details (example: start time and end time)
    @Modifying
    @Transactional
    @Query("UPDATE CourseSession cs SET cs.startTime = :startTime, cs.endTime = :endTime WHERE cs.id = :id")
    void updateSessionTimes(@Param("id") String id, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);

    // Deletion
    void deleteById(String id);
    @Modifying
    @Query("DELETE FROM CourseSession cs WHERE cs.course = :course")
    void deleteByCourse(@Param("course") Course course);
}

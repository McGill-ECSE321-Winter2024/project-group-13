package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface CourseSessionRepository extends CrudRepository<CourseSession, String>{

    //Read methods
    CourseSession findCourseSessionById(String id);
    // Find all sessions for a specific course
    List<CourseSession> findByCourse(Course course);
    // Find sessions by date
    List<CourseSession> findByDay(Date day);
    // Find sessions within a specific time range on a specific day
    @Query("SELECT cs FROM CourseSession cs WHERE cs.day = :day AND cs.startTime >= :startTime AND cs.endTime <= :endTime")
    List<CourseSession> findSessionsByDayAndTimeRange(@Param("day") Date day, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    // Find all sessions for a course on a specific day
    List<CourseSession> findByCourseAndDay(Course course, Date day);


    // Deletion
    void deleteById(String id);
    @Modifying
    @Query("DELETE FROM CourseSession cs WHERE cs.course = :course")
    void deleteByCourse(@Param("course") Course course);

}
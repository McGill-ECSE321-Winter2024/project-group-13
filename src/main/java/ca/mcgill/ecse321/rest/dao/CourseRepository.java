package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, String>{

    Course findCourseById(String id);

    Course findCourseByName(String name);

    List<Course> findCoursesByCourseState(Course.CourseState courseState);

    @Transactional
    void deleteCourseById(String id);


}
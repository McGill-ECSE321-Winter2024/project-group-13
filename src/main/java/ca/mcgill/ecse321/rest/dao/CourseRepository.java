package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, String>{

    Course findCourseById(String id);

    Course findCourseByName(String name);

    @Transactional
    void deleteCourseById(String id);


}
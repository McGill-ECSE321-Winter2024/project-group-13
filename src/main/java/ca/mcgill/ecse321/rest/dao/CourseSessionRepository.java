package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.CourseSession;
import ca.mcgill.ecse321.rest.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface CourseSessionRepository extends CrudRepository<CourseSession, String>{

    CourseSession findCourseSessionById(String id);

    CourseSession findCourseSessionByCourseId(String name);

}
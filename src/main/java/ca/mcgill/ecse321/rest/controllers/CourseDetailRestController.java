package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Schedule;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class CourseDetailRestController {
    @Autowired
    private CourseDetailService courseDetailService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/courses/{course_id}/schedule")
    public Schedule getSchedule(@PathVariable("course_id") String courseId,
                                @RequestHeader("Authorization") String bearerToken) {
        return courseDetailService.getSchedule(courseId);
    }


    @GetMapping("/courses")
    public List<Course> getAllCourses(@RequestHeader("Authorization") String bearerToken){
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);
        // For owner, show any course
        if(personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return courseDetailService.getAllCourses();
        }
        // For instructor, show all active courses + courses created by them
        if(personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            return courseDetailService.getAllActiveAndInstructorSpecificCourses(personSession.getPersonId());
        }
        // For customer, only show active courses
        if(personSession.getPersonType().equals(PersonSession.PersonType.Customer)) {
            return courseDetailService.getActiveCourses();
        }
        throw new IllegalArgumentException("Unauthorized");
    }

    @GetMapping("/courses/{course_id}")
    public Course getCourse(@PathVariable("course_id") String courseId, @RequestHeader("Authorization") String bearerToken){
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        Course course = courseDetailService.getSpecificCourse(courseId);

        // For owner, show any course
        if(personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return course;
        }
        // For instructor, show all active courses + courses created by them
        if(personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            boolean isCreator = course.getInstructor().getId().equals(personSession.getPersonId());
            if(course.getCourseState().equals(Course.CourseState.Approved) || courseDetailService.isInstructorOfCourse(course.getId(), personSession.getPersonId())) {
                return course;
            } else {
                throw new IllegalArgumentException("Unauthorized");
            }
        }
        // For customer, only show active courses
        if(personSession.getPersonType().equals(PersonSession.PersonType.Customer)) {
            if(course.getCourseState().equals(Course.CourseState.Approved)){
                return course;
            } else {
                throw new IllegalArgumentException("Unauthorized");
            }
        }
        throw new IllegalArgumentException("Unauthorized");
    }

    @GetMapping("/courses/{course_id}/customers")
    public List<Customer> getCustomers(@PathVariable("course_id") String courseId, @RequestHeader("Authorization") String bearerToken){
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        // Only accessible to owner and instructor of the class
        boolean isOwner = personSession.getPersonType().equals(PersonSession.PersonType.Owner);
        boolean isInstructorOfCourse = courseDetailService.isInstructorOfCourse(courseId, personSession.getPersonId());
        if(isOwner || isInstructorOfCourse) {
            return courseDetailService.getCustomers(courseId);
        } else {
            throw new IllegalArgumentException("Unauthorized");
        }
    }
}

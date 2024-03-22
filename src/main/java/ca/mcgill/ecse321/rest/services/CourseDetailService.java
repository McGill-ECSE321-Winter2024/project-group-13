package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.InstructorRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseDetailService {
    @Autowired private CourseRepository courseRepository;
    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private InstructorRepository instructorRepository;

    public Schedule getSchedule(String courseId){
        Course course = courseRepository.findCourseById(courseId);
        return course != null ? course.getSchedule() : null;
    }

    public List<Course> getCoursesWithFilters(Course.CourseState state, String instructorName, Timestamp startDate) {
        if(state == null && instructorName == null && startDate == null){
            return (List<Course>) courseRepository.findAll();
        }
        String instructorId = null;
        if (instructorName != null) {
            Instructor instructor = (Instructor) instructorRepository.findInstructorByName(instructorName); // Implement this method as needed
            if (instructor != null) {
                instructorId = instructor.getId(); // Assuming getId() returns the UUID as String
            }
        }
        return courseRepository.findCoursesByFilters(state, instructorId, startDate);
    }


    public Course getSpecificCourse(String courseId){
        return courseRepository.findCourseById(courseId);
    }

    public List<Customer> getCustomers(String courseId){
        List<Registration> registrations = registrationRepository.findRegistrationByCourseId(courseId);
        List<Customer> customers = new ArrayList<>();
        for(Registration registration: registrations){
            customers.add(registration.getCustomer());
        }
        return customers;
    }

    public List<Course> getActiveCourses() {
        // This will retrieve only the courses with the state 'Approved'
        return courseRepository.findCoursesByCourseState(Course.CourseState.Approved);
    }

    public List<Course> getAllActiveAndInstructorSpecificCourses(String instructorId) {
        List<Course> activeCourses = getActiveCourses(); // get all active courses
        List<Course> instructorCourses = courseRepository.findCoursesByInstructorId(instructorId); // get all courses by the instructor

        // Combine both lists using a Set to remove duplicates
        Set<Course> combinedCourses = new HashSet<>(activeCourses);
        combinedCourses.addAll(instructorCourses); // This will not add duplicates

        // Convert the Set back to a List
        return new ArrayList<>(combinedCourses);
    }


    public boolean isInstructorOfCourse(String courseId, String instructorId) {
        // Check if the instructor is associated with the course
        Course course = courseRepository.findCourseById(courseId);
        if(course.getInstructor() == null){
            return false;
        }
        return course.getInstructor().getId().equals(instructorId);
    }

}

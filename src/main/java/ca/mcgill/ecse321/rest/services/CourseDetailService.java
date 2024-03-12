package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Registration;
import ca.mcgill.ecse321.rest.models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseDetailService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    public Schedule getSchedule(String courseId){
        Course course = courseRepository.findCourseById(courseId);
        return course != null ? course.getSchedule() : null;
    }

    public List<Course> getAllCourses(){
        return (List<Course>) courseRepository.findAll();
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
        return course != null && course.getInstructor().getId().equals(instructorId);
    }

}

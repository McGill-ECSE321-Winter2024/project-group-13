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
import java.util.List;

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

}

package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.CustomerRepository; // Assuming you have this for loading the Customer
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CourseRegistrationService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CustomerRepository customerRepository; // Assumes existence of CustomerRepository

    @Autowired
    private RegistrationRepository registrationRepository;

    @Transactional
    public String registerForCourse(String courseId, PersonSession personSession) {
        if (personSession.getPersonType() != PersonSession.PersonType.Customer) {
            return "Only customers can register for courses.";
        }

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (!courseOptional.isPresent()) {
            return "Course not found.";
        }

        Course course = courseOptional.get();
        if (course.getCourseState() != Course.CourseState.Approved || course.getCourseEndDate().before(new java.util.Date())) {
            return "Registration is only possible for approved courses with an end date in the future.";
        }

        Optional<Customer> customerOptional = customerRepository.findById(personSession.getPersonId());
        if (!customerOptional.isPresent()) {
            return "Customer not found.";
        }

        Customer customer = customerOptional.get();

        // Create new registration
        Registration registration = new Registration();
        registration.setCourse(course);
        registration.setCustomer(customer);
        // Assuming the rating is set later, not during registration
        registrationRepository.save(registration);

        return "Successfully registered for the course.";
    }
}

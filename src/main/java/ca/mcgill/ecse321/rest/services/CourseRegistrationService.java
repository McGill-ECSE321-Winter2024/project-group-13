package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.InvoiceRepository;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.CustomerRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Invoice;
import ca.mcgill.ecse321.rest.models.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CourseRegistrationService {
    @Autowired private CourseRepository courseRepository;

    @Autowired private CustomerRepository customerRepository;
    @Autowired private RegistrationRepository registrationRepository;
    @Autowired private InvoiceRepository invoiceRepository;

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
        registration.setRating(0);
        // Assuming the rating is set later, not during registration
        registrationRepository.save(registration);

        Invoice invoice = new Invoice();
        invoice.setRegistration(registration);
        invoice.setStatus(Invoice.Status.Open);
        // TODO: Set the amount based on the course's hourly rate
        invoice.setAmount(course.getHourlyRateAmount());
        invoiceRepository.save(invoice);


        TwilioService.sendSms(customer.getPhoneNumber(), "Hello " + customer.getName() + ". You have successfully registered for the course " + course.getName() + "!");

        return "Successfully registered for the course.";
    }
}

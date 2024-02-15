package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RegistrationRepositoryTests {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    /**
     * This method executes after each test. This is done by the "@AfterEach" JPA annotation
     * The method is used to clear the database after each test, so that we don't fill up our database tables
     * with unwanted data from tests.
     */
    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
    }


    /**
     * This method is a "@TEST" that tests the Persistance and Loading of the registration class.
     * A registration needs a customer and a course in order to be relevant.
     * Once the customer and course are set, we cannot delete registration, to do so
     * We have to delete course and customer first.
     * In this method, we first create a customer, then a course.
     * We save customer and course. Then, we create and save a registration.
     * Finally, we use "asserts" to check if the values saved in our registration table in the database are correct.
     */
    @Test
    public void testPersistAndLoadRegistration() {
        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddy");
        customer.setPhoneNumber("XXX-XXX-XXXX");
        customer.setEmail("teddy.el-husseini@mail.mcgill.ca");
        customer.setPassword("test");

        //Create Course.
        Course course = new Course();
        course.setId("456");
        course.setName("ecse321");
        course.setDescription("description");

        // Save Customer and Course
        personRepository.save(customer);
        courseRepository.save(course);

        // Create and save registration.
        Registration registration = new Registration();
        registration.setCustomer(customer);
        registration.setCourse(course);

        int rating = 1;
        registration.setRating(rating);
        registrationRepository.save(registration);



        //Get RegistrationID
        String registrationID = registration.getId();

        // Read registration from database.
        registration = registrationRepository.findRegistrationById(registrationID);

        //Asserts
        assertNotNull(registration);
        assertNotNull(registration.getId());
        assertEquals(rating, registration.getRating());

        assertEquals(customer.getName(), registration.getCustomer().getName());
        assertEquals(customer.getPhoneNumber(), registration.getCustomer().getPhoneNumber());
        assertEquals(customer.getEmail(), registration.getCustomer().getEmail());
        assertEquals(customer.getPassword(), registration.getCustomer().getPassword());
        assertEquals(customer.getId(), registration.getCustomer().getId());
        assertEquals(customer.getSportCenter(), registration.getCustomer().getSportCenter());

        assertEquals(course.getId(), registration.getCourse().getId());
        assertEquals(course.getName(), registration.getCourse().getName());
        assertEquals(course.getDescription(), registration.getCourse().getDescription());
        assertEquals(course.getCourseStartDate(), registration.getCourse().getCourseStartDate());
        assertEquals(course.getCourseEndDate(), registration.getCourse().getCourseEndDate());
        assertEquals(course.getRoom(), registration.getCourse().getRoom());
        assertEquals(course.getSportCenter(), registration.getCourse().getSportCenter());
        assertEquals(course.getInstructor(), registration.getCourse().getInstructor());

    }
}

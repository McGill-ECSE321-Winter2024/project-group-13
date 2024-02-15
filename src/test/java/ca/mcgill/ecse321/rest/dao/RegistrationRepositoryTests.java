package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
        customer.setPhoneNumber("111-222-3333");
        customer.setEmail("teddy.el-husseini@mail.mcgill.ca");
        customer.setPassword("test");

        //Create Course.
        Course course = new Course();
        course.setName("ecse321");
        course.setDescription("description");

        // Save Customer and Course
        personRepository.save(customer);
        courseRepository.save(course);

        // Create and save registration.
        Registration registration = new Registration();
        int rating = 1;
        registration.setRating(rating);
        registration.setCourse(course);
        registration.setCustomer(customer);
        registrationRepository.save(registration);

        String registrationID = registration.getId();

        // Read registration from database.
        registration = registrationRepository.findRegistrationById(registrationID);

        assertNotNull(registration);
        assertNotNull(registration.getId());
        assertEquals(rating, registration.getRating());

        assertEquals(customer.getId(), registration.getCustomer().getId());
        assertEquals(course.getId(), registration.getCourse().getId());
    }

    @Test
    public void testDeleteRegistrationWithInvalidId() {

        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddy");
        customer.setPhoneNumber("111-111-1111");
        customer.setEmail("teddyw.el-husseini@mailll.mcgill.ca");
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
        int rating = 1;
        registration.setRating(rating);
        registration.setCourse(course);
        registration.setCustomer(customer);
        registrationRepository.save(registration);

        String registrationID = registrationRepository.findRegistrationById(registration.getId()).getId();

        registrationID = "HEHEHE"+ registrationID + "HOHOHO";

        long beforeDelete = registrationRepository.count();

        registrationRepository.deleteById(registrationID);

        long afterDelete = registrationRepository.count();

        assertEquals(beforeDelete, afterDelete);

    }

    @Test
    public void testUpdateRegistrationWithNullValues() {
        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddy");
        customer.setPhoneNumber("XXX-XXX-XXXX");
        customer.setEmail("teddyw.el-husseini@mail.mcgill.ca");
        customer.setPassword("test");

        //Create Course.
        Course course = new Course();
        course.setId("456");
        course.setName("ecse321");
        course.setDescription("description");

        // Save Customer and Course
        personRepository.save(customer);
        courseRepository.save(course);

        Registration registration = new Registration("something", 1, customer,course);


        assertFalse(registration.setCourse(null));
        assertFalse(registration.setRating(0));
        assertFalse(registration.setRating(-1));
        assertFalse(registration.setCustomer(null));

        registrationRepository.save(registration);
    }

    @Test
    public void testCreateRegistrationWithNullValues() {

        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddy");
        customer.setPhoneNumber("XXX-XXX-XXXX");
        customer.setEmail("teddyw.el-husseini@mail.mcgill.ca");
        customer.setPassword("test");

        //Create Course.
        Course course = new Course();
        course.setId("456");
        course.setName("ecse321");
        course.setDescription("description");

        assertThrows(RuntimeException.class, () -> {
            // Create and save registration.
            Registration registration = new Registration("something", 1, customer, null);
            registrationRepository.save(registration);

        });

        assertThrows(RuntimeException.class, () -> {
            // Create and save registration.
            Registration registration = new Registration("something", 1, null, null);
            registrationRepository.save(registration);

        });

        assertThrows(RuntimeException.class, () -> {
            // Create and save registration.
            Registration registration = new Registration("something", 1, null, course);
            registrationRepository.save(registration);

        });


    }

    @Test
    public void testCreateRegistrationWithInvalidRating() {
        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddy");
        customer.setPhoneNumber("XXX-XXX-XXXX");
        customer.setEmail("teddyw.el-husseini@mail.mcgill.ca");
        customer.setPassword("test");

        //Create Course.
        Course course = new Course();
        course.setId("456");
        course.setName("ecse321");
        course.setDescription("description");

        assertThrows(IllegalArgumentException.class, () -> {
            // Create and save registration.
            Registration registration = new Registration("something", 0, customer, course);
            registrationRepository.save(registration);

        });

        assertThrows(IllegalArgumentException.class, () -> {
            // Create and save registration.
            Registration registration = new Registration("something", -10, customer, course);
            registrationRepository.save(registration);

        });

    }
}

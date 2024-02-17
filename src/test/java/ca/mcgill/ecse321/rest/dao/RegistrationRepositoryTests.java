package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.helpers.CourseCustomerTuple;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

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
     *
     * @Author Teddy El-Husseini
     */
    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
    }

    /*
        * This method is used to populate the database with test data.
     */

    /**
     * This method is a "@TEST" that tests the Persistance and Loading of the registration class.
     * A registration needs a customer and a course in order to be relevant.
     * Once the customer and course are set, we cannot delete registration, to do so
     * We have to delete course and customer first.
     * In this method, we first create a customer, then a course.
     * We save customer and course. Then, we create and save a registration.
     * Finally, we use "asserts" to check if the values saved in our registration table in the database are correct.
     *
     * @Author Teddy El-Husseini
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


    /**
     * This method tests the deletion of a registration from the database.
     * @author Achraf Ghellach
     */
    @Test
    public void testDeleteRegistration() {

        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);
        String registrationID = registration.getId();
        registrationRepository.deleteById(registrationID);
        assertNull(registrationRepository.findRegistrationById(registrationID));

    }

    /**
     * This method tests the update of a registration from the database.
     * @Author Achraf Ghellach
     */
    @Test
    public void testUpdateRegistrationValidRating() {

        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);
        String registrationID = registration.getId();

        int newRating = 5;
        registration.setRating(newRating);
        registrationRepository.save(registration);

        registration = registrationRepository.findRegistrationById(registrationID);
        assertEquals(1, registration.getRating());

    }

    /**
     * This method tests the update of a registration from the database with a negative rating.
     * @Author Achraf Ghellach
     */
    @Test
    public void testUpdateRegistrationNegativeRating() {

        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);
        String registrationID = registration.getId();

        int newRating = -1;
        registration.setRating(newRating);
        registrationRepository.save(registration);

        registration = registrationRepository.findRegistrationById(registrationID);
        System.out.println(registration.getRating());
        assertNotEquals(newRating, registration.getRating());

    }

    /**
     * This method tests the update of a registration from the database with a rating of 0.
     */
    @Test
    public void testCustomerId() {

        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);
        String registrationID = registration.getId();

        registration = registrationRepository.findRegistrationById(registrationID);
        assertEquals(testData.customer.getId(), registration.getCustomer().getId());

    }

    /**
     * This method tests the equivalence of the course id in the database and the course id in the registration.
     */
    @Test
    public void testCourseId() {

        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);
        String registrationID = registration.getId();

        registration = registrationRepository.findRegistrationById(registrationID);
        assertEquals(testData.course.getId(), registration.getCourse().getId());

    }

    /**
     * This method the foreign key constraint on the customer id in the registration table.
     * @Author Achraf Ghellach
     */

    @Test
    public void testDeleteCustomerWithRegistration() {

        assertThrows(DataIntegrityViolationException.class, () -> {
            CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

            Registration registration = new Registration();
            registration.setCourse(testData.course);
            registration.setCustomer(testData.customer);

            registrationRepository.save(registration);
            personRepository.deleteById(testData.customer.getId());
        });


    }


    /**
     * This method the foreign key constraint on the course id in the registration table.
     * @Author Achraf Ghellach
     */
    @Test
    public void testDeleteCourseWithRegistration() {

        assertThrows(DataIntegrityViolationException.class, () -> {
            CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

            Registration registration = new Registration();
            registration.setCourse(testData.course);
            registration.setCustomer(testData.customer);

            registrationRepository.save(registration);
            courseRepository.deleteById(testData.course.getId());
        });
    }

    /**
     * This method tests the impossibility of creating two registrations with the same course and customer.
     * @Author Achraf Ghellach
     */

    @Test
    public void testAttemptCreateTwoRegistrationsWithSameCourseAndCustomer() {
        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);

        Registration registration2 = new Registration();
        registration2.setCourse(testData.course);
        registration2.setCustomer(testData.customer);

        assertThrows(DataIntegrityViolationException.class, () -> {
            registrationRepository.save(registration2);
        });
    }

    /**
     * This method tests the possibility of creating two registrations with the same course and different customers.
     * @Author Achraf Ghellach
     */
    @Test
    public void testCreateTwoRegistrationsWithSameCourseAndDifferentCustomer() {
        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);

        Customer customer2 = new Customer();
        customer2.setName("Achraf");
        customer2.setPhoneNumber("111-222-3334");
        customer2.setEmail("achraf2@example.com");
        customer2.setPassword("test");

        Registration registration2 = new Registration();
        registration2.setCourse(testData.course);
        registration2.setCustomer(customer2);

        personRepository.save(customer2);
        registrationRepository.save(registration2);

        assertEquals(2, registrationRepository.count());

    }

    /**
     * This method tests the possibility of creating two registrations with the same customer and different courses.
     */
    @Test
    public void testCreateTwoRegistrationsWithSameCustomerAndDifferentCourse() {
        CourseCustomerTuple testData = CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

        Registration registration = new Registration();
        registration.setCourse(testData.course);
        registration.setCustomer(testData.customer);

        registrationRepository.save(registration);

        Course course2 = new Course();
        course2.setName("ecse331");
        course2.setDescription("description");

        Registration registration2 = new Registration();
        registration2.setCourse(course2);
        registration2.setCustomer(testData.customer);

        courseRepository.save(course2);
        registrationRepository.save(registration2);

        assertEquals(2, registrationRepository.count());

    }

    /**
     * This test checks that no registration is deleted when using deleteById with a non-existent id.
     *
     * @Author Teddy El-Husseini
     */
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

    /**
     * This test checks that the course, rating and customer will not update if they are set to null/invalid values.
     *
     * @Author Teddy El-Husseini
     */
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

    /**
     * This test checks that an error is thrown when a registration with null values is created and that
     * nothing is saved.
     *
     * @Author Teddy El-Husseini
     */
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

    /**
     * This test checks that an error is thrown when an invoice with invalid rating is created and that
     * nothing is saved.
     *
     * @Author Teddy El-Husseini
     */
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

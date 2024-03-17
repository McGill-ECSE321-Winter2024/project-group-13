package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.helpers.CourseCustomerTuple;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@SpringBootTest
public class RegistrationRepositoryTests {
  @Autowired private PersonRepository personRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private RegistrationRepository registrationRepository;

  /**
   * This method executes after each test. This is done by the "@AfterEach" JPA annotation The
   * method is used to clear the database after each test, so that we don't fill up our database
   * tables with unwanted data from tests.
   * @Author Teddy El-Husseini
   */
  @AfterEach
  public void clearDatabase() {
    registrationRepository.deleteAll();
    courseRepository.deleteAll();
    personRepository.deleteAll();
  }

  /**
   * This method is a "@TEST" that tests the Persistance and Loading of the registration class. A
   * registration needs a customer and a course in order to be relevant. Once the customer and
   * course are set, we cannot delete registration, to do so We have to delete course and customer
   * first. In this method, we first create a customer, then a course. We save customer and course.
   * Then, we create and save a registration. Finally, we use "asserts" to check if the values saved
   * in our registration table in the database are correct.
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

    // Create Course.
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
   * @Author Achraf Ghellach
   */
  @Test
  public void testDeleteRegistration() {

    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    Registration registration = new Registration();
    registration.setCourse(testData.course);
    registration.setCustomer(testData.customer);

    registrationRepository.save(registration);
    String registrationID = registration.getId();
    registrationRepository.deleteById(registrationID);
    assertNull(registrationRepository.findRegistrationById(registrationID));
  }

  /** This method tests the update of a registration from the database.
   * @Author Achraf Ghellach */
  @Test
  public void testUpdateRegistrationValidRating() {

    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    Registration registration = new Registration();
    registration.setCourse(testData.course);
    registration.setCustomer(testData.customer);

    registrationRepository.save(registration);
    String registrationID = registration.getId();

    int newRating = 5;
    registration.setRating(newRating);
    registrationRepository.save(registration);

    registration = registrationRepository.findRegistrationById(registrationID);
    assertEquals(newRating, registration.getRating());
  }

  /**
   * This method tests the update of a registration from the database with a negative
   * rating.
   * @Author Achraf Ghellach
   */
  @Test
  public void testUpdateRegistrationNegativeRating() {

    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

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

  /** This method tests the update of a registration from the database with a rating of 0.
   * @Author Achraf Ghellach
   * */
  @Test
  public void testCustomerId() {

    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    Registration registration = new Registration();
    registration.setCourse(testData.course);
    registration.setCustomer(testData.customer);

    registrationRepository.save(registration);
    String registrationID = registration.getId();

    registration = registrationRepository.findRegistrationById(registrationID);
    assertEquals(testData.customer.getId(), registration.getCustomer().getId());
  }

  /**
   * This method tests the equivalence of the course id in the database and the course id in the
   * registration.
   * @Author Achraf Ghellach
   */
  @Test
  public void testCourseId() {

    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

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

    assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          CourseCustomerTuple testData =
              CourseCustomerTuple.populateTestCustomersAndCourses(
                  personRepository, courseRepository);

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

    assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          CourseCustomerTuple testData =
              CourseCustomerTuple.populateTestCustomersAndCourses(
                  personRepository, courseRepository);

          Registration registration = new Registration();
          registration.setCourse(testData.course);
          registration.setCustomer(testData.customer);

          registrationRepository.save(registration);
          courseRepository.deleteById(testData.course.getId());
        });
  }

  /**
   * This method tests the impossibility of creating two registrations with the same course and
   * customer.
   * @Author Achraf Ghellach
   */
  @Test
  public void testAttemptCreateTwoRegistrationsWithSameCourseAndCustomer() {
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    Registration registration = new Registration();
    registration.setCourse(testData.course);
    registration.setCustomer(testData.customer);

    registrationRepository.save(registration);

    Registration registration2 = new Registration();
    registration2.setCourse(testData.course);
    registration2.setCustomer(testData.customer);

    assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          registrationRepository.save(registration2);
        });
  }

  /**
   * This method tests the possibility of creating two registrations with the same course and
   * different customers.
   * @Author Achraf Ghellach
   */
  @Test
  public void testCreateTwoRegistrationsWithSameCourseAndDifferentCustomer() {
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

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
   * This method tests the possibility of creating two registrations with the same customer and
   * different courses.
   * @Author Achraf Ghellach
   */
  @Test
  public void testCreateTwoRegistrationsWithSameCustomerAndDifferentCourse() {
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

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
   * This test checks that no registration is deleted when using deleteById with a non-existent
   * id.
   * @Author Teddy El-Husseini
   */
  @Test
  public void testDeleteRegistrationWithInvalidId() {
    // getting some test data from helper method
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    // creating and saving new registration.
    Registration registration = new Registration();
    registration.setCourse(testData.course);
    registration.setCustomer(testData.customer);
    registrationRepository.save(registration);

    // getting registration Id and changing it
    String registrationID =
        registrationRepository.findRegistrationById(registration.getId()).getId();
    String registrationID2 = "test" + registrationID + "test";

    // deleting by id and invalid registration
    long beforeDelete = registrationRepository.count();
    registrationRepository.deleteById(registrationID2);
    assertNotNull(registrationRepository.findRegistrationById(registrationID));
    long afterDelete = registrationRepository.count();

    // number of registration should not change
    assertEquals(beforeDelete, afterDelete);
  }

  /**
   * This test checks that the course, rating and customer will not update if they are set to
   * null/invalid values.
   * @Author Teddy El-Husseini
   */
  @Test
  public void testUpdateRegistrationWithNullAndInvalidValues() {
    // getting some test data from helper method
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    // creating and saving new registration.
    Registration registration = new Registration();
    registration.setCourse(testData.course);
    registration.setCustomer(testData.customer);
    registration.setRating(5);
    registrationRepository.save(registration);

    // changing course, registration and rating to invalid values, saving and then making sure they
    // did not change
    registration.setCourse(null);
    registrationRepository.save(registration);
    assertNotNull(registrationRepository.findRegistrationById(registration.getId()).getCourse());

    assertFalse(registration.setRating(0));
    registrationRepository.save(registration);
    assertNotEquals(
        0, registrationRepository.findRegistrationById(registration.getId()).getRating());

    assertFalse(registration.setRating(-1));
    registrationRepository.save(registration);
    assertNotEquals(
        -1, registrationRepository.findRegistrationById(registration.getId()).getRating());

    assertFalse(registration.setCustomer(null));
    registrationRepository.save(registration);
    assertNotNull(registrationRepository.findRegistrationById(registration.getId()).getCustomer());
  }

  /**
   * This test checks that an error is thrown when a registration with null values is created and
   * that nothing is saved.
   * @Author Teddy El-Husseini
   */
  @Test
  public void testCreateRegistrationWithNullValues() {
    // getting some test data from helper method
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    // making sure registration with invalid customer and course cannot be created or saved.
    assertThrows(
        RuntimeException.class,
        () -> {
          Registration registration = new Registration("something", 1, testData.customer, null);
          registrationRepository.save(registration);
        });

    assertThrows(
        RuntimeException.class,
        () -> {
          Registration registration = new Registration("something", 1, null, null);
          registrationRepository.save(registration);
        });

    assertThrows(
        RuntimeException.class,
        () -> {
          Registration registration = new Registration("something", 1, null, testData.course);
          registrationRepository.save(registration);
        });
  }

  /**
   * This test checks that an error is thrown when an invoice with invalid rating is created and
   * that nothing is saved.
   * @Author Teddy El-Husseini
   */
  @Test
  public void testCreateRegistrationWithInvalidRating() {
    // getting some test data from helper method
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    // making sure registration with invalid rating cannot be created or saved.
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Registration registration =
              new Registration("something", 0, testData.customer, testData.course);
          registrationRepository.save(registration);
        });

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Registration registration =
              new Registration("something", -10, testData.customer, testData.course);
          registrationRepository.save(registration);
        });
  }


  /**
   * Test to verify that finding registrations by course ID correctly returns all registrations for multiple customers.
   * This test creates a single course and two customers, registers both customers to the course,
   * and then retrieves the registrations using the course ID. It checks that both registrations are
   * correctly retrieved, ensuring the method works for multiple registrations associated with a single course.
   * @Author Omar Moussa
   */
  @Test
  public void testFindRegistrationsByCourseIdWithMultipleCustomers() {
    // Create and save a course
    Course course = new Course();
    course.setName("ecse321");
    course.setDescription("Software Engineering");
    courseRepository.save(course);

    // Create and save first customer
    Customer customer1 = new Customer();
    customer1.setName("Customer1");
    customer1.setPhoneNumber("123-456-7890");
    customer1.setEmail("customer1@mail.com");
    customer1.setPassword("password1");
    personRepository.save(customer1);

    // Create and save second customer
    Customer customer2 = new Customer();
    customer2.setName("Customer2");
    customer2.setPhoneNumber("098-765-4321");
    customer2.setEmail("customer2@mail.com");
    customer2.setPassword("password2");
    personRepository.save(customer2);

    // Create and save registrations for both customers in the same course
    Registration registration1 = new Registration();
    registration1.setCourse(course);
    registration1.setCustomer(customer1);
    registrationRepository.save(registration1);

    Registration registration2 = new Registration();
    registration2.setCourse(course);
    registration2.setCustomer(customer2);
    registrationRepository.save(registration2);

    // Attempt to retrieve registrations by the course ID
    List<Registration> registrations = registrationRepository.findRegistrationByCourseId(course.getId());

    // Assert that the retrieved list is not null, has exactly two elements and contains the expected registrations
    assertNotNull(registrations, "The retrieved registration list should not be null.");
    assertEquals(2, registrations.size(), "There should be exactly two registrations associated with the course ID.");

    // Verify that the list contains registrations for both customers
    assertTrue(registrations.stream().anyMatch(r -> r.getCustomer().getId().equals(customer1.getId())),
            "The list should contain a registration for customer1.");
    assertTrue(registrations.stream().anyMatch(r -> r.getCustomer().getId().equals(customer2.getId())),
            "The list should contain a registration for customer2.");
  }

}

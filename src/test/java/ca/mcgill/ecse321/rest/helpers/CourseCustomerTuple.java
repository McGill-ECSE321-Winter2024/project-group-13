package ca.mcgill.ecse321.rest.helpers;

import static ca.mcgill.ecse321.rest.helpers.RandomGenerator.generateRandomEmail;
import static ca.mcgill.ecse321.rest.helpers.RandomGenerator.generateRandomPhoneNumber;

import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;

public class CourseCustomerTuple {
  public Course course;
  public Customer customer;

  public static CourseCustomerTuple populateTestCustomersAndCourses(
      PersonRepository personRepository, CourseRepository courseRepository) {

    // Create Customer
    Customer customer = new Customer();
    customer.setName("Achraf");
    customer.setPhoneNumber(generateRandomPhoneNumber());
    customer.setEmail(generateRandomEmail());
    customer.setPassword("test");

    // Create Course.
    Course course = new Course();
    course.setName("ecse321");
    course.setDescription("description");

    // Save Customer and Course
    personRepository.save(customer);
    courseRepository.save(course);

    CourseCustomerTuple tuple = new CourseCustomerTuple();

    tuple.course = course;
    tuple.customer = customer;

    return tuple;
  }
}

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
        //@Autowired
        //private CourseRepository courseRepository;  //need to create courseRepository first
    @Autowired
    private RegistrationRepository registrationRepository;

    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
            //CourseRepository.deleteAll();
        personRepository.deleteAll();
    }
    @Test
    public void testPersistAndLoadRegistration() {
        // Create person.

        Customer customer = new Customer();

        customer.setName("Teddy");
        customer.setPhoneNumber("5147912844");
        customer.setEmail("teddy.el-husseini@mail.mcgill.ca");
        customer.setPassword("test");
        customer.setId("123");

        //Create Course.
        Course course = new Course();
        course.setId("456");
        course.setName("ecse321");
        course.setDescription("description");


        // Save EveryThing
        personRepository.save(customer);
        //courseRepository.save(course);

        // Create and save registration.
        Registration registration = new Registration();
        registration.setCustomer(customer);
        //registration.setCourse(course);
        registrationRepository.save(registration);


        //Get RegistrationID
        String registrationID = registration.getId();

        // Read registration from database.
        registration = registrationRepository.findRegistrationById(registrationID);

        assertNotNull(registration);
        assertNotNull(registration.getId());

        assertEquals(customer.getName(), registration.getCustomer().getName());
        assertEquals(customer.getPhoneNumber(), registration.getCustomer().getPhoneNumber());
        assertEquals(customer.getEmail(), registration.getCustomer().getEmail());
        assertEquals(customer.getPassword(), registration.getCustomer().getPassword());
        assertEquals(customer.getId(), registration.getCustomer().getId());

        //assertEquals(1,1); //this will be for course


        /**
        assertEquals(event.getName(), registration.getEvent().getName());
        assertEquals(event.getDate(), registration.getEvent().getDate());
        assertEquals(event.getStartTime(), registration.getEvent().getStartTime());
        assertEquals(event.getEndTime(), registration.getEvent().getEndTime());
        **/

    }




}

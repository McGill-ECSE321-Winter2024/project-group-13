package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class InvoiceRepositoryTests {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CourseRepository courseRepository;

    /**
     * This method executes after each test. This is done by the "@AfterEach" JPA annotation
     * The method is used to clear the database after each test, so that we don't fill up our database tables
     * with unwanted data from tests.
     */
    @AfterEach
    public void clearDatabase() {
        invoiceRepository.deleteAll();
    }

    public void fillDataBase() {
        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddyy");
        customer.setPhoneNumber("XXX-XXX-XXXXX");
        customer.setEmail("teddy.el-husseini@mail.mcgill.caaaa");
        customer.setPassword("testt");
        customer.setId("1233");

        //Create Course.
        Course course = new Course();
        course.setId("4566");
        course.setName("ecse3211");
        course.setDescription("descriptionn");

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
    }

    /**
     * This method is a "@TEST" that tests the Persistance and Loading of the invoice class.
     * An invoice needs a registration set to it.
     * A registration needs a customer and a course in order to be relevant.
     * Once the registration is set, we cannot delete invoice, we have to delete registration first.
     * In this method, we first create a customer, then a course.
     * We save customer and course. Then, we create and save a registration.
     * Finally, we use "asserts" to check if the values saved in our registration table in the database are correct.
     */
    @Test
    public void testPersistAndLoadInvoice() {

        // Run this one time to get data in table
        // Open Database Table to see registration: "SELECT * FROM registration;"
        // Then copy/paste Registration Id below
        // Need to find a better way to do this: have already existing table.
        //fillDataBase();

        //create and save invoice
        Invoice invoice = new Invoice();
        Registration registration = registrationRepository.findRegistrationById("5187c018-a79e-46ab-a243-3e88fadf2773"); //get this value from the database
        invoice.setStatus(Invoice.Status.Open);
        invoice.setRegistrations(registration);
        invoiceRepository.save(invoice);

        //Get invoiceID
        String invoiceID = invoice.getId();

        // Read invoice from database.
        invoice = invoiceRepository.findInvoiceById(invoiceID);

        //Asserts
        assertNotNull(invoice);
        assertNotNull(invoice.getId());
        assertEquals(Invoice.Status.Open, invoice.getStatus());
        assertEquals(registration.getId(), invoice.getRegistrations().getId());
        //assertEquals(registration.getCourse(), invoice.getRegistrations().getCourse());
        //assertEquals(registration.getCustomer(), invoice.getRegistrations().getCustomer());

        assertEquals(registration.getCustomer().getName(), invoice.getRegistrations().getCustomer().getName());
        assertEquals(registration.getCustomer().getPhoneNumber(), invoice.getRegistrations().getCustomer().getPhoneNumber());
        assertEquals(registration.getCustomer().getEmail(), invoice.getRegistrations().getCustomer().getEmail());
        assertEquals(registration.getCustomer().getPassword(), invoice.getRegistrations().getCustomer().getPassword());
        assertEquals(registration.getCustomer().getId(), invoice.getRegistrations().getCustomer().getId());
        assertEquals(registration.getCustomer().getSportCenter(), invoice.getRegistrations().getCustomer().getSportCenter());

        assertEquals(registration.getCourse().getId(), invoice.getRegistrations().getCourse().getId());
        assertEquals(registration.getCourse().getName(), invoice.getRegistrations().getCourse().getName());
        assertEquals(registration.getCourse().getDescription(), invoice.getRegistrations().getCourse().getDescription());
        assertEquals(registration.getCourse().getCourseStartDate(), invoice.getRegistrations().getCourse().getCourseStartDate());
        assertEquals(registration.getCourse().getCourseEndDate(), invoice.getRegistrations().getCourse().getCourseEndDate());
        assertEquals(registration.getCourse().getRoom(), invoice.getRegistrations().getCourse().getRoom());
        assertEquals(registration.getCourse().getSportCenter(), invoice.getRegistrations().getCourse().getSportCenter());
        assertEquals(registration.getCourse().getInstructor(), invoice.getRegistrations().getCourse().getInstructor());

    }
}

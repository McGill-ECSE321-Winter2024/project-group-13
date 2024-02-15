package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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

    Customer findCustomer;
    Course findCourse;

    /**
     * This method executes after each test. This is done by the "@AfterEach" JPA annotation
     * The method is used to clear the database after each test, so that we don't fill up our database tables
     * with unwanted data from tests.
     */
    @AfterEach
    public void clearDatabase() {
        invoiceRepository.deleteAll();
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        personRepository.deleteAll();
    }

    /**
     * e
     */
    public void fillDataBase() {
        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddyy");
        customer.setPhoneNumber("555-444-1111");
        customer.setEmail("teddy.el-husseini@mail.mcgill.cca");
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

        findCourse = course;
        findCustomer = customer;
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

        fillDataBase();

        //create and save invoice
        Invoice invoice = new Invoice();
        Registration registration = registrationRepository.findRegistrationByCourseAndCustomer(findCourse, findCustomer);

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

        assertEquals(registration.getCustomer().getId(), invoice.getRegistrations().getCustomer().getId());
        assertEquals(registration.getCustomer().getName(), invoice.getRegistrations().getCustomer().getName());
        assertEquals(registration.getCustomer().getPhoneNumber(), invoice.getRegistrations().getCustomer().getPhoneNumber());
        assertEquals(registration.getCustomer().getEmail(), invoice.getRegistrations().getCustomer().getEmail());
        assertEquals(registration.getCustomer().getPassword(), invoice.getRegistrations().getCustomer().getPassword());
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

    @Test
    public void testSuccessfullyCreateInvoice() {
        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddyy");
        customer.setPhoneNumber("555-444-2221");
        customer.setEmail("teddy.el-husseini@mail.mcga");
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
        Registration registrationTest = new Registration();
        registrationTest.setCustomer(customer);
        registrationTest.setCourse(course);
        registrationRepository.save(registrationTest);

        Invoice.Status statusTest1 = Invoice.Status.Open;

        //create and save invoice
        Invoice invoice1 = new Invoice("id1", statusTest1, registrationTest);
        invoiceRepository.save(invoice1);

        //Get invoiceIDs
        String invoiceID1 = invoice1.getId();

        // Read invoice from database.
        invoice1 = invoiceRepository.findInvoiceById(invoiceID1);

        //Asserts
        assertNotNull(invoice1);

        assertNotNull(invoice1.getId());

        assertEquals(Invoice.Status.Open, invoice1.getStatus());

        assertEquals(registrationTest.getId(), invoice1.getRegistrations().getId());
    }


    @Test
    public void testCreateInvoiceWithNullValues() {
        // Create Customer
        Customer customer = new Customer();
        customer.setName("Teddyy");
        customer.setPhoneNumber("555-444-1561");
        customer.setEmail("teddy.el-hei@mail.mcgill.cca");
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
        Registration registrationTest = new Registration();
        registrationTest.setCustomer(customer);
        registrationTest.setCourse(course);
        registrationRepository.save(registrationTest);

        Invoice.Status statusTest1 = Invoice.Status.Open;

        //create and save invoice
        assertThrows(RuntimeException.class, () -> {
            // Create and save registration.
            Invoice invoice1 = new Invoice("id1", statusTest1, null);
            invoiceRepository.save(invoice1);

        });

        assertThrows(RuntimeException.class, () -> {
            // Create and save registration.
            Invoice invoice1 = new Invoice("id1", null, registrationTest);
            invoiceRepository.save(invoice1);

        });

        assertThrows(RuntimeException.class, () -> {
            // Create and save registration.
            Invoice invoice1 = new Invoice("id1", null, null);
            invoiceRepository.save(invoice1);

        });
    }

    @Test
    public void testSuccessfullyUpdateInvoice() { //registration, all status

        Registration registrationTest1 = new Registration();
        registrationRepository.save(registrationTest1);

        Invoice.Status statusTest1 = Invoice.Status.Open;
        Invoice.Status statusTest2 = Invoice.Status.Failed;
        Invoice.Status statusTest3 = Invoice.Status.Void;
        Invoice.Status statusTest4 = Invoice.Status.Completed;
        Invoice.Status statusTest5 = Invoice.Status.Cancelled;

        Invoice invoice1 = new Invoice("id1", statusTest1, registrationTest1);
        invoiceRepository.save(invoice1);

        invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
        assertEquals(registrationTest1.getId(), invoice1.getRegistrations().getId());

        Registration registrationTest2 = new Registration();
        registrationRepository.save(registrationTest2);

        invoice1.setRegistrations(registrationTest2);
        invoiceRepository.save(invoice1);
        invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
        assertEquals(registrationTest2.getId(), invoice1.getRegistrations().getId());

        invoice1.setStatus(statusTest2);
        invoiceRepository.save(invoice1);
        invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
        assertEquals(statusTest2, invoice1.getStatus());

        invoice1.setStatus(statusTest3);
        invoiceRepository.save(invoice1);
        invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
        assertEquals(statusTest3, invoice1.getStatus());

        invoice1.setStatus(statusTest4);
        invoiceRepository.save(invoice1);
        invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
        assertEquals(statusTest4, invoice1.getStatus());

        invoice1.setStatus(statusTest5);
        invoiceRepository.save(invoice1);
        invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
        assertEquals(statusTest5, invoice1.getStatus());
    }


    @Test
    public void testUnSuccessfullyUpdateInvoice() { //status null and registration null
        Invoice invoice = new Invoice();

       assertFalse(invoice.setStatus(null));

       assertFalse(invoice.setRegistrations(null));

    }


    @Test
    public void testDeleteInvoice() {
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);

        String invoiceID = invoiceRepository.findInvoiceById(invoice.getId()).getId();

        long beforeDelete = invoiceRepository.count();

        invoiceRepository.deleteById(invoiceID);

        long afterDelete = invoiceRepository.count();

        assertEquals(beforeDelete-1, afterDelete);
    }

    @Test
    public void testDeleteInvoiceWithInvalidId() {

        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);

        String invoiceID = invoiceRepository.findInvoiceById(invoice.getId()).getId();

        invoiceID = "HEHEHE"+ invoiceID + "HOHOHO";

        long beforeDelete = registrationRepository.count();

        registrationRepository.deleteById(invoiceID);

        long afterDelete = registrationRepository.count();

        assertEquals(beforeDelete, afterDelete);
    }

}

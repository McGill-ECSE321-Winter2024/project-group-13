package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.helpers.CourseCustomerTuple;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
public class InvoiceRepositoryTests {

  @Autowired private InvoiceRepository invoiceRepository;
  @Autowired private RegistrationRepository registrationRepository;
  @Autowired private PersonRepository personRepository;
  @Autowired private CourseRepository courseRepository;

  /**
   * This method executes after each test. This is done by the "@AfterEach" JPA annotation The
   * method is used to clear the database after each test, so that we don't fill up our database
   * tables with unwanted data from tests. @Author Teddy El-Husseini
   */
  @AfterEach
  public void clearDatabase() {
    // delete every Repository used after each test
    invoiceRepository.deleteAll();
    registrationRepository.deleteAll();
    courseRepository.deleteAll();
    personRepository.deleteAll();
  }

  /**
   * This method fill the database with one customer, one course and one registration. It will be
   * used by tests. @Author Teddy El-Husseini
   */
  public Registration fillDataBase() {
    // Getting test data from helper function
    CourseCustomerTuple testData =
        CourseCustomerTuple.populateTestCustomersAndCourses(personRepository, courseRepository);

    // Create and save registration.
    Registration registration = new Registration();
    registration.setCustomer(testData.customer);
    registration.setCourse(testData.course);
    int rating = 4;
    registration.setRating(rating);
    registrationRepository.save(registration);

    return registration;
  }

  /**
   * This method is a "@TEST" that tests the Persistance and Loading of the invoice class. An
   * invoice needs a registration set to it. A registration needs a customer and a course in order
   * to be relevant. Once the registration is set, we cannot delete invoice, we have to delete
   * registration first. In this method, we first create a customer, then a course. We save customer
   * and course. Then, we create and save a registration. Finally, we use "asserts" to check if the
   * values saved in our registration table in the database are correct. @Author Teddy El-Husseini
   */
  @Test
  public void testPersistAndLoadInvoice() {

    // Getting test data from helper function
    Registration registration1 = fillDataBase();

    // create and save invoice
    Invoice invoice = new Invoice();
    Registration registration =
        registrationRepository.findRegistrationByCourseAndCustomer(
            registration1.getCourse(), registration1.getCustomer());
    invoice.setStatus(Invoice.Status.Open);
    invoice.setRegistration(registration);
    invoiceRepository.save(invoice);

    // Get invoiceID
    String invoiceID = invoice.getId();

    // Read invoice from database.
    invoice = invoiceRepository.findInvoiceById(invoiceID);

    // Asserts to make sure everything works.
    assertNotNull(invoice);
    assertNotNull(invoice.getId());
    assertEquals(Invoice.Status.Open, invoice.getStatus());
    assertEquals(registration.getId(), invoice.getRegistration().getId());

    assertEquals(
        registration.getCustomer().getId(), invoice.getRegistration().getCustomer().getId());
    assertEquals(registration.getCourse().getId(), invoice.getRegistration().getCourse().getId());
  }

  /**
   * This test checks that an invoice can be created and saved, if valid values are
   * inputted. @Author Teddy El-Husseini
   */
  @Test
  public void testSuccessfullyCreateInvoice() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating and saving invoice
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice1 = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice1);

    // Get invoice from database
    String invoiceID1 = invoice1.getId();
    invoice1 = invoiceRepository.findInvoiceById(invoiceID1);

    // Assert that the invoice and its ID are not null
    assertNotNull(invoice1);
    assertNotNull(invoice1.getId());

    // Assert that the status of the invoice matches the expected status
    assertEquals(Invoice.Status.Open, invoice1.getStatus());
  }

  /**
   * This method tests the equivalence of the course id in the database and the course id in the
   * registration. @Author Teddy El-Husseini
   */
  @Test
  public void testRegistrationId() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    // creating and saving new invoice
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice1 = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice1);

    // getting registration from repository
    String registrationID = registration.getId();
    registration = registrationRepository.findRegistrationById(registrationID);

    // assert registrationId is the same as the one saved in invoice
    assertEquals(registration.getId(), invoice1.getRegistration().getId());
  }

  /**
   * This test checks that an error is thrown when an invoice with null values is created and that
   * nothing is saved. @Author Teddy El-Husseini
   */
  @Test
  public void testCreateInvoiceWithNullCustomerAndCourseValues() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    Invoice.Status statusTest1 = Invoice.Status.Open;

    // asserting that creating and saving invalid invoice throws error
    assertThrows(
        RuntimeException.class,
        () -> {
          Invoice invoice1 = new Invoice(statusTest1, null, 0);
          invoiceRepository.save(invoice1);
        });

    assertThrows(
        RuntimeException.class,
        () -> {
          Invoice invoice1 = new Invoice(null, registration, 0);
          invoiceRepository.save(invoice1);
        });

    assertThrows(
        RuntimeException.class,
        () -> {
          Invoice invoice1 = new Invoice(null, null, 0);
          invoiceRepository.save(invoice1);
        });
  }

  /**
   * This test checks that an invoice can be updated to any status. (Part 1) and that the
   * registration can also be updated if provided a valid registration. @Author Teddy El-Husseini
   */
  @Test
  public void testSuccessfullyUpdateInvoice() { // registration, all status
    // Getting test data from helper function
    Registration registration = fillDataBase();

    // creating and saving invoice
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice.Status statusTest2 = Invoice.Status.Failed;
    Invoice invoice1 = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice1);

    // getting invoice from repository
    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
    assertEquals(registration.getId(), invoice1.getRegistration().getId());

    // create and save new registration
    Registration registrationTest2 = new Registration();
    registrationRepository.save(registrationTest2);

    // changing registration of invoice, saving invoice, getting invoice from repository
    // and assert that new registration is successfully set.
    invoice1.setRegistration(registrationTest2);
    invoiceRepository.save(invoice1);
    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
    assertEquals(registrationTest2.getId(), invoice1.getRegistration().getId());

    // testing that status can be set successfully
    invoice1.setStatus(statusTest2);
    invoiceRepository.save(invoice1);
    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
    assertEquals(statusTest2, invoice1.getStatus());
  }

  /**
   * This test creates two invoices associated to the same registration. @Author Teddy El-Husseini
   */
  @Test
  public void testCreateTwoInvoiceWithSameRegistration() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    Invoice.Status statusTest1 = Invoice.Status.Open;

    // making sure datatable is empty
    assertEquals(invoiceRepository.count(), 0);

    // create two invoice with the same registration and save them
    Invoice invoice1 = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice1);

    Invoice invoice2 = new Invoice();
    invoice2.setRegistration(registration);
    invoiceRepository.save(invoice2);

    // making sure datatable has two invoice with the same registration
    assertEquals(
        invoiceRepository.findInvoiceById(invoice2.getId()).getRegistration().getId(),
        invoiceRepository.findInvoiceById(invoice1.getId()).getRegistration().getId());
    assertEquals(invoiceRepository.count(), 2);
  }

  /**
   * This test checks that an invoice can be updated to any status. (Part 2) @Author Teddy
   * El-Husseini
   */
  @Test
  public void testSuccessfullyUpdateInvoice2() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice.Status statusTest3 = Invoice.Status.Void;
    Invoice.Status statusTest4 = Invoice.Status.Completed;
    Invoice.Status statusTest5 = Invoice.Status.Cancelled;
    // Getting test data from helper function
    // Creating, saving and then getting invoice in repository
    Invoice invoice1 = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice1);
    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());

    // testing that status can be set successfully for all status
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

  /**
   * This test checks that the status and registration will not update if they are set to
   * null. @Author Teddy El-Husseini
   */
  @Test
  public void testUnSuccessfullyUpdateInvoice() { // status null and registration null

    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice);

    // make sure status null does not set
    invoice.setStatus(null);
    invoiceRepository.save(invoice);
    assertNotNull(invoiceRepository.findInvoiceById(invoice.getId()).getStatus());

    // make sure registration null does not set
    invoice.setRegistration(null);
    invoiceRepository.save(invoice);
    assertNotNull(invoiceRepository.findInvoiceById(invoice.getId()).getRegistration());
  }

  /**
   * This test checks that the appropriate invoice is deleted when using deleteById with a valid
   * id. @Author Teddy El-Husseini
   */
  @Test
  public void testDeleteInvoice() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice);

    // getting number of invoice before the deletion
    long beforeDelete = invoiceRepository.count();

    // make sure delete invoice by id works
    String invoiceID = invoice.getId();
    invoiceRepository.deleteById(invoiceID);
    assertNull(invoiceRepository.findInvoiceById(invoiceID));

    // getting number of invoice after the deletion
    long afterDelete = invoiceRepository.count();

    // number of invoice before deletion = number of invoice after deletion + 1
    assertEquals(beforeDelete - 1, afterDelete);
  }

  /**
   * This test checks that no invoice is deleted when using deleteById with a non-existent
   * id. @Author Teddy El-Husseini
   */
  @Test
  public void testDeleteInvoiceWithInvalidId() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice);

    // getting number of invoice before the deletion
    long beforeDelete = invoiceRepository.count();

    // getting and changing id
    String invoiceID = invoiceRepository.findInvoiceById(invoice.getId()).getId();
    String invoiceID2 = "test" + invoiceID + "test";

    // deleting incorrect id
    invoiceRepository.deleteById(invoiceID2);

    // invoice should not delete
    assertNotNull(invoiceRepository.findInvoiceById(invoiceID));

    // getting number of invoice before the deletion
    long afterDelete = invoiceRepository.count();

    // before and after deletion, number of invoice stays the same
    assertEquals(beforeDelete, afterDelete);
  }

  /**
   * This test checks that an invoice amount is update if the amount is 0 or positive. @Author Teddy
   * El-Husseini
   */
  @Test
  public void testUpdateInvoiceWithValidAmount() {
    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice1 = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice1);

    // making sure amount in invoice is correct
    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
    assertEquals(0, invoice1.getAmount());

    // updating amount, then making sure it is still correct.
    invoice1.setAmount(1);
    invoiceRepository.save(invoice1);
    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
    assertEquals(1, invoice1.getAmount());
  }

  /**
   * This test checks that an invoice amount is not update if the amount is negative. @Author Teddy
   * El-Husseini
   */
  @Test
  public void testUpdateInvoiceWithInvalidAmount() {

    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice1 = new Invoice(statusTest1, registration, 0);
    invoiceRepository.save(invoice1);

    // setting invalid amount in invoice, then making sure it did not save.
    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());
    assertEquals(0, invoice1.getAmount());
    invoice1.setAmount(-1);
    invoiceRepository.save(invoice1);
    assertNotEquals(-1, invoice1.getAmount());
  }

  /**
   * This test checks if no error is thrown when an invoice is created with a valid amount. @Author
   * Teddy El-Husseini
   */
  @Test
  public void testCreateInvoiceWithValidAmount() {

    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;
    Invoice invoice1 = new Invoice(statusTest1, registration, 150);
    invoiceRepository.save(invoice1);

    invoice1 = invoiceRepository.findInvoiceById(invoice1.getId());

    // making sure an invoice has valid amount
    assertEquals(150, invoice1.getAmount());
  }

  /**
   * This test checks if an error is thrown when an invoice is created with an invalid
   * amount. @Author Teddy El-Husseini
   */
  @Test
  public void testCreateInvoiceWithInvalidAmount() {

    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;

    // making sure that creating invoice with invalid amount throws error
    assertThrows(
        RuntimeException.class,
        () -> {
          Invoice invoice1 = new Invoice(statusTest1, registration, -12);
          invoiceRepository.save(invoice1);
        });
  }

  /**
   * This test checks that you cannot delete a registration before deleting its invoice @Author
   * Teddy El-Husseini
   */
  @Test
  public void testDeleteRegistrationBeforeInvoice() {

    // Getting test data from helper function
    Registration registration = fillDataBase();

    // Creating, saving and then getting invoice in repository
    Invoice.Status statusTest1 = Invoice.Status.Open;

    Invoice invoice1 = new Invoice(statusTest1, registration, 120);
    invoiceRepository.save(invoice1);

    // making sure that deleting registration associated to invoice throws error
    assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          registrationRepository.deleteById(registration.getId());
        });
  }
}

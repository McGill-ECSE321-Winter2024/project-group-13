package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.helpers.RandomGenerator;
import ca.mcgill.ecse321.rest.models.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
public class CustomerRepositoryTests {
  private final String name = "Customer";
  private final String password = "test";
  @Autowired private CustomerRepository customerRepository;

  /** Clear the test database after tests have run. */
  @AfterEach
  public void clearDatabase() {
    customerRepository.deleteAll();
  }

  /**
   * @author Rafael Reis Test goal: create a customer successfully. This test creates a customer,
   *     saves it to the database, and asserts that the retrieved customer from the database is not
   *     null.
   */
  @Test
  public void testCreateCustomer() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
  }

  /**
   * @author Rafael Reis Test goal: create a customer and read its data from the database
   *     successfully. This test creates a customer, saves it to the database, retrieves it by id
   *     and asserts that the retrieved data related to this customer from the database is correct.
   */
  @Test
  public void testReadCustomerByEmail() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: create a customer and read its data from the database
   *     successfully using the phone number as identifier. This test creates a customer, saves it
   *     to the database, retrieves it by phone number and asserts that the retrieved data related
   *     to this customer from the database is correct.
   */
  @Test
  public void testReadCustomerByPhoneNumber() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByPhoneNumber(
            phoneNumber); // Get customer from database by phone number.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: update the customer password In this test we create a customer
   *     and save it to the database. Then we change the customer password and make sure the
   *     database reflects that change.
   */
  @Test
  public void testUpdateCustomerPassword() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);

    // Get customer with ID and update password.
    String id = customer.getId();
    customer.setPassword("password");
    customerRepository.save(customer);
    customer = customerRepository.findCustomerById(id);
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, "password");
  }

  /**
   * @author Rafael Reis Test goal: update the customer email In this test we create a customer and
   *     save it to the database. Then we change the customer email and make sure the database
   *     reflects that change.
   */
  @Test
  public void testUpdateCustomerEmail() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);

    // Get customer with ID and update email.
    String id = customer.getId();
    String newEmail = RandomGenerator.generateRandomEmail();
    customer.setEmail(newEmail);
    customerRepository.save(customer);

    customer = customerRepository.findCustomerById(id);
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, newEmail, password);
  }

  /**
   * @author Rafael Reis Test goal: update the customer name In this test we create a customer and
   *     save it to the database. Then we change the customer name and make sure the database
   *     reflects that change.
   */
  @Test
  public void testUpdateCustomerName() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);

    // Get customer with ID and update name.
    String id = customer.getId();
    customer.setName("Philippe Aprahamian");
    customerRepository.save(customer);
    customer = customerRepository.findCustomerById(id);
    assertNotNull(customer);
    checkAttributes(customer, "Philippe Aprahamian", phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: update the customer phone number In this test we create a
   *     customer and save it to the database. Then we change the customer phone number and make
   *     sure the database reflects that change.
   */
  @Test
  public void testUpdateCustomerPhoneNumber() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);

    // Get customer with ID and update phone number.
    String id = customer.getId();
    customer.setPhoneNumber("+33750901255");
    customerRepository.save(customer);
    customer = customerRepository.findCustomerById(id);
    // Check new phone number
    assertNotNull(customer);
    checkAttributes(customer, name, "+33750901255", email, password);
  }

  /**
   * @author Rafael Reis Test goal: delete the customer by id In this test we create a customer and
   *     save it to the database. Then we delete the customer and make sure the database reflects
   *     that change.
   */
  @Test
  public void testDeleteCustomerById() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);

    // Delete customer
    String id = customer.getId();
    customerRepository.deleteCustomerById(id);
    customer = customerRepository.findCustomerByEmail(email);
    assertNull(customer); // there should not be a customer with the specified email
  }

  /**
   * @author Rafael Reis Test goal: delete the customer by email In this test we create a customer
   *     and save it to the database. Then we delete the customer and make sure the database
   *     reflects that change.
   */
  @Test
  public void testDeleteCustomerByEmail() {
    // Create customer.
    Customer customer = createCustomer();
    String email = customer.getEmail();
    String phoneNumber = customer.getPhoneNumber();

    customerRepository.save(customer); // Save customer to database.
    customer =
        customerRepository.findCustomerByEmail(email); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer);
    checkAttributes(customer, name, phoneNumber, email, password);

    // Delete customer
    customerRepository.deleteCustomerByEmail(email);
    customer = customerRepository.findCustomerByEmail(email);
    assertNull(customer); // there should not be a customer with the specified email
  }

  /**
   * @author Rafael Reis Test goal: check for duplicate customer email. In this test we create a
   *     customer and save it to the database. Then we try to create another customer with the same
   *     email. We should detect this and not allow it.
   */
  @Test
  public void testDuplicateEmailThrowsException() {
    // Assuming createCustomer() correctly sets a unique email that can be duplicated for the test
    Customer customer1 = createCustomer();
    customer1.setEmail("duplicate@example.com");
    customerRepository.save(customer1);

    // Attempt to create another customer with the same email
    Customer customer2 = createCustomer();
    customer2.setEmail("duplicate@example.com");

    // Assert that saving the second customer throws a DataIntegrityViolationException
    assertThrows(DataIntegrityViolationException.class, () -> {
      customerRepository.save(customer2);
    }, "Expected DataIntegrityViolationException to be thrown due to duplicate email");
  }

  /**
   * @author Rafael Reis Test goal: check for duplicate customer phone number. In this test we
   *     create a customer and save it to the database. Then we try to create another customer with
   *     the same phone number. We should detect this and not allow it.
   */
  @Test
  public void testCustomerDuplicatePhoneNumber() {
    // Create customer.
    Customer customer1 = createCustomer();
    String email1 = customer1.getEmail();
    String phoneNumber1 = customer1.getPhoneNumber();

    customerRepository.save(customer1); // Save customer to database.
    customer1 =
        customerRepository.findCustomerByEmail(email1); // Get customer from database by email.

    // Assert that customer is not null (i.e. customer was created and added to the database
    // successfully)
    assertNotNull(customer1);
    checkAttributes(customer1, name, phoneNumber1, email1, password);

    // Create second customer
    Customer customer2 = createCustomer();
    String phoneNumber2 = customer2.getPhoneNumber();
    customer2.setEmail(
        phoneNumber1); // Attempt to change the phone number of customer2 to be that of customer1;

    assertEquals(
        phoneNumber2,
        customer2
            .getPhoneNumber()); // the phone number should still be email2, i.e. the change was not
    // allowed
  }

  /**
   * @author Rafael Reis Create a new customer object and return it.
   * @return the new customer object.
   */
  private Customer createCustomer() {
    String phoneNumber = RandomGenerator.generateRandomPhoneNumber();
    String email = RandomGenerator.generateRandomEmail();

    Customer customer = new Customer();
    setAttributes(customer, phoneNumber, email);
    return customer;
  }

  /**
   * @author Rafael Reis Set attributes of the customer parameter.
   * @param customer the customer whose attributes we will set.
   * @param phoneNumber the new customer phone number.
   * @param email the new customer email.
   */
  private void setAttributes(Customer customer, String phoneNumber, String email) {
    customer.setName(name);
    customer.setPhoneNumber(phoneNumber);
    customer.setEmail(email);
    customer.setPassword(password);
  }

  /**
   * @author Rafael Reis Perform JUnit tests to see whether the customer attributes are correct or
   *     not.
   * @param customer the customer whose attributes we are checking.
   * @param name the expected name.
   * @param phoneNumber the expected phone number.
   * @param email the expected email address.
   * @param password the expected password.
   */
  private void checkAttributes(
      Customer customer, String name, String phoneNumber, String email, String password) {
    assertEquals(name, customer.getName());
    assertEquals(phoneNumber, customer.getPhoneNumber());
    assertEquals(email, customer.getEmail());
    assertEquals(password, customer.getPassword());
  }
}

package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Instructor;
import ca.mcgill.ecse321.rest.models.Person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Clear the test database after all tests have run.
     */
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    /**
     * @author Rafael Reis
     * This test creates a customer, saves it to the database,
     * and asserts that the retrieved data related to this customer from the database is correct.
     */
    @Test
    public void testCreateCustomer() {
        // Create customer.
        String name = "Slim Sid";
        String phoneNumber = "4384934907";
        String email = "slim.sid@gmail.com";
        String password = "test";
        String id;

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);
        customer.setPassword(password);

        // Save customer
        customerRepository.save(customer);

        // Read customer from database.
        customer = customerRepository.findCustomerByEmail(email);

        // Assert that customer is not null and has correct attributes.
        assertNotNull(customer);
        assertEquals(name, customer.getName());
        assertEquals(phoneNumber, customer.getPhoneNumber());
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());

        id = customer.getId();
    }

    /*
     * //TODO
     * Update password test
     * Update email test
     * Update name test
     * Update phone number test
     * Delete customer test
     * Duplicate email test
     * Duplicate phone number test
     */


}

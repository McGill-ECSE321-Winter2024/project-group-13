package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.rest.models.Instructor;
import ca.mcgill.ecse321.rest.models.Owner;
import ca.mcgill.ecse321.rest.models.Person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepository;

    /**
     * Clear the test database after all tests have run.
     */
    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
    }

    /**
     * @author Rafael Reis
     * This test creates an owner, saves it to the database,
     * and asserts that the retrieved data related to this owner from the database is correct.
     */
    @Test
    public void testCreateOwner() {
        // Create owner.
        String name = "Owner Owen";
        String phoneNumber = "4384934907";
        String email = "owner.owen@gmail.com";
        String password = "test";
        String id;

        Owner owner = new Owner();
        owner.setName(name);
        owner.setPhoneNumber(phoneNumber);
        owner.setEmail(email);
        owner.setPassword(password);

        // Save owner
        ownerRepository.save(owner);

        // Read owner from database.
        owner = ownerRepository.findOwnerByName(name);

        // Assert that owner is not null and has correct attributes.
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertEquals(phoneNumber, owner.getPhoneNumber());
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());

        id = owner.getId();
    }

    /*
     * //TODO
     * Update password test
     * Update email test
     * Update name test
     * Update phone number test
     * Delete owner test
     */
}

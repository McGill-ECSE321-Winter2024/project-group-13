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
     * Clear the test database after tests have run.
     */
    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
    }

    /**
     * @author Rafael Reis
     * Test goal: create an owner successfully.
     * This test creates an owner, saves it to the database,
     * and asserts that the retrieved owner from the database is not null.
     */
    @Test
    public void testCreateOwner() {
        // Create owner.
        String name = "Owner Owen";
        String phoneNumber = "4384934907";
        String email = "owner.owen@gmail.com";
        String password = "test";

        Owner owner = new Owner();
        owner.setName(name);
        owner.setPhoneNumber(phoneNumber);
        owner.setEmail(email);
        owner.setPassword(password);

        // Save owner to database.
        ownerRepository.save(owner);

        // Get owner from database.
        owner = ownerRepository.findOwnerByName(name);

        // Assert that owner is not null (i.e. owner was created and added to the database successfully)
        assertNotNull(owner);
    }

    /**
     * @author Rafael Reis
     * Test goal: create an owner and read its data from the database successfully.
     * This test creates an owner, saves it to the database, retrieves it by name
     * and asserts that the retrieved data related to this owner from the database is correct.
     */
    @Test
    public void testReadOwnerByName() {
        // Create owner.
        String name = "Owner Owen";
        String phoneNumber = "4384934907";
        String email = "owner.owen@gmail.com";
        String password = "test";

        Owner owner = new Owner();
        owner.setName(name);
        owner.setPhoneNumber(phoneNumber);
        owner.setEmail(email);
        owner.setPassword(password);

        // Save owner to database.
        ownerRepository.save(owner);

        // Get owner from database by name.
        owner = ownerRepository.findOwnerByName(name);

        // Assert that owner is not null (i.e. owner was created and added to the database successfully)
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertEquals(phoneNumber, owner.getPhoneNumber());
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());
    }

    /**
     * @author Rafael Reis
     * Test goal: create an owner and read its data from the database successfully.
     * This test creates an owner, saves it to the database, retrieves it by id
     * and asserts that the retrieved data related to this owner from the database is correct.
     */
    @Test
    public void testReadOwnerByEmail() {
        // Create owner.
        String name = "Owner Owen";
        String phoneNumber = "4384934907";
        String email = "owner.owen@gmail.com";
        String password = "test";

        Owner owner = new Owner();
        owner.setName(name);
        owner.setPhoneNumber(phoneNumber);
        owner.setEmail(email);
        owner.setPassword(password);

        // Save owner to database.
        ownerRepository.save(owner);

        // Get owner from database by email.
        owner = ownerRepository.findOwnerByEmail(email);

        // Assert that owner is not null (i.e. owner was created and added to the database successfully)
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertEquals(phoneNumber, owner.getPhoneNumber());
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());
    }

    /**
     * @author Rafael Reis
     * Test goal: update the owner password
     * In this test we create an owner and save it to the database.
     * Then we change the owner password and make sure the database reflects that change.
     */
    @Test
    public void testUpdateOwnerPassword() {
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

        // Save owner to database.
        ownerRepository.save(owner);

        // Get owner from database.
        owner = ownerRepository.findOwnerByName(name);

        // Assert that owner is not null and has correct attributes.
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertEquals(phoneNumber, owner.getPhoneNumber());
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());

        // Get owner with ID and update password.
        id = owner.getId();
        owner.setPassword("password");
        ownerRepository.save(owner);
        owner = ownerRepository.findOwnerById(id);
        assertNotNull(owner);
        assertEquals("password", owner.getPassword());
    }

    /**
     * @author Rafael Reis
     * Test goal: update the owner email
     * In this test we create an owner and save it to the database.
     * Then we change the owner email and make sure the database reflects that change.
     */
    @Test
    public void testUpdateOwnerEmail() {
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

        // Save owner to database.
        ownerRepository.save(owner);

        // Get owner from database.
        owner = ownerRepository.findOwnerByName(name);

        // Assert that owner is not null and has correct attributes.
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertEquals(phoneNumber, owner.getPhoneNumber());
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());

        // Get owner with ID and update email.
        id = owner.getId();
        owner.setEmail("owner@gmail.com");
        ownerRepository.save(owner);
        owner = ownerRepository.findOwnerById(id);
        assertNotNull(owner);
        assertEquals("owner@gmail.com", owner.getEmail());
    }

    /**
     * @author Rafael Reis
     * Test goal: update the owner name
     * In this test we create an owner and save it to the database.
     * Then we change the owner name and make sure the database reflects that change.
     */
    @Test
    public void testUpdateOwnerName() {
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

        // Save owner to database.
        ownerRepository.save(owner);

        // Get owner from database.
        owner = ownerRepository.findOwnerByName(name);

        // Assert that owner is not null and has correct attributes.
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertEquals(phoneNumber, owner.getPhoneNumber());
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());

        // Get owner with ID and update name.
        id = owner.getId();
        owner.setName("Owen");
        ownerRepository.save(owner);
        owner = ownerRepository.findOwnerById(id);
        assertNotNull(owner);
        assertEquals("Owen", owner.getName());
    }

    /*
     * //TODO
     * Update name test
     * Update phone number test
     * Delete owner test
     * Duplicate email test
     * Duplicate phone number test
     */
}

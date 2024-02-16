package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.rest.models.Owner;
import ca.mcgill.ecse321.rest.models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class PersonRepositoryTests {
    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
    }

    @Test
    public void testCreateOwner() {
        // Create person.
        String name = "Muffin Man";
        String phoneNumber = "4384934907";
        String email = "muffin.man@gmail.com";
        String password = "test";
        Integer age = 40;
        String address = "123 Drury Lane";

        Person person = new Owner();
        person.setName(name);
        person.setPhoneNumber(phoneNumber);
        person.setEmail(email);
        person.setPassword(password);

        // Save person
        personRepository.save(person);

        // Read person from database.
        person = personRepository.findPersonByName(name);

        // Assert that person is not null and has correct attributes.
        assertNotNull(person);
        assertEquals(name, person.getName());
        assertEquals(phoneNumber, person.getPhoneNumber());
        assertEquals(email, person.getEmail());
        assertEquals(password, person.getPassword());
    }

    /*
     * //TODO
     * Update password test
     * Update email test
     * Update name test
     * Update phone number test
     * Delete person test
     */

}
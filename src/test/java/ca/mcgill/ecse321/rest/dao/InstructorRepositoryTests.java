package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.rest.models.Instructor;
import ca.mcgill.ecse321.rest.models.Person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InstructorRepositoryTests {
    @Autowired
    private InstructorRepository instructorRepository;

    /**
     * Clear the test database after all tests have run.
     */
    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
    }

    /**
     * @author Rafael Reis
     * This test creates an instructor, saves it to the database,
     * and asserts that the retrieved data related to this instructor from the database is correct.
     */
    @Test
    public void testCreateInstructor() {
        // Create instructor.
        String name = "Buff Bill";
        String phoneNumber = "4384934907";
        String email = "buff.bill@gmail.com";
        String password = "test";
        String id;

        Instructor instructor = new Instructor();
        instructor.setName(name);
        instructor.setPhoneNumber(phoneNumber);
        instructor.setEmail(email);
        instructor.setPassword(password);

        // Save instructor
        instructorRepository.save(instructor);

        // Read instructor from database.
        instructor = instructorRepository.findInstructorByName(name);

        // Assert that instructor is not null and has correct attributes.
        assertNotNull(instructor);
        assertEquals(name, instructor.getName());
        assertEquals(phoneNumber, instructor.getPhoneNumber());
        assertEquals(email, instructor.getEmail());
        assertEquals(password, instructor.getPassword());

        id = instructor.getId();
    }

    /*
     * //TODO
     * Update password test
     * Update email test
     * Update name test
     * Update phone number test
     * Delete instructor test
     * Duplicate email test
     * Duplicate phone number test
     */
}

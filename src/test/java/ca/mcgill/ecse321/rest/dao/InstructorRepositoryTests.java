package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.helpers.RandomGenerator;
import ca.mcgill.ecse321.rest.models.Instructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class InstructorRepositoryTests {
    @Autowired
    private InstructorRepository instructorRepository;
    private final String name = "Instructor";
    private final String password = "test";

    /**
     * Clear the test database after tests have run.
     */
    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
    }

    /**
     * @author Rafael Reis
     * Test goal: create an instructor successfully.
     * This test creates an instructor, saves it to the database,
     * and asserts that the retrieved instructor from the database is not null.
     */
    @Test
    public void testCreateInstructor() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
    }

    /**
     * @author Rafael Reis
     * Test goal: create an instructor and read its data from the database successfully.
     * This test creates an instructor, saves it to the database, retrieves it by id
     * and asserts that the retrieved data related to this instructor from the database is correct.
     */
    @Test
    public void testReadInstructorByEmail() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();
        String phoneNumber = instructor.getPhoneNumber();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, password);
    }

    /**
     * @author Rafael Reis
     * Test goal: update the instructor password
     * In this test we create an instructor and save it to the database.
     * Then we change the instructor password and make sure the database reflects that change.
     */
    @Test
    public void testUpdateInstructorPassword() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();
        String phoneNumber = instructor.getPhoneNumber();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, password);

        // Get instructor with ID and update password.
        String id = instructor.getId();
        instructor.setPassword("password");
        instructorRepository.save(instructor);
        instructor = instructorRepository.findInstructorById(id);
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, "password");
    }

    /**
     * @author Rafael Reis
     * Test goal: update the instructor email
     * In this test we create an instructor and save it to the database.
     * Then we change the instructor email and make sure the database reflects that change.
     */
    @Test
    public void testUpdateInstructorEmail() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();
        String phoneNumber = instructor.getPhoneNumber();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, password);

        // Get instructor with ID and update email.
        String id = instructor.getId();
        String newEmail = RandomGenerator.generateRandomEmail();
        instructor.setEmail(newEmail);
        instructorRepository.save(instructor);

        instructor = instructorRepository.findInstructorById(id);
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, newEmail, password);
    }

    /**
     * @author Rafael Reis
     * Test goal: update the instructor name
     * In this test we create an instructor and save it to the database.
     * Then we change the instructor name and make sure the database reflects that change.
     */
    @Test
    public void testUpdateInstructorName() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();
        String phoneNumber = instructor.getPhoneNumber();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, password);

        // Get instructor with ID and update name.
        String id = instructor.getId();
        instructor.setName("Philippe Aprahamian");
        instructorRepository.save(instructor);
        instructor = instructorRepository.findInstructorById(id);
        assertNotNull(instructor);
        checkAttributes(instructor, "Philippe Aprahamian", phoneNumber, email,password);
    }

    /**
     * @author Rafael Reis
     * Test goal: update the instructor phone number
     * In this test we create an instructor and save it to the database.
     * Then we change the instructor phone number and make sure the database reflects that change.
     */
    @Test
    public void testUpdateInstructorPhoneNumber() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();
        String phoneNumber = instructor.getPhoneNumber();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, password);

        // Get instructor with ID and update phone number.
        String id = instructor.getId();
        instructor.setPhoneNumber("+33750901255");
        instructorRepository.save(instructor);
        instructor = instructorRepository.findInstructorById(id);
        // Check new phone number
        assertNotNull(instructor);
        checkAttributes(instructor, name, "+33750901255", email, password);
    }

    /**
     * @author Rafael Reis
     * Test goal: delete the instructor
     * In this test we create an instructor and save it to the database.
     * Then we delete the instructor and make sure the database reflects that change.
     */
    @Test
    public void testDeleteInstructor() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();
        String phoneNumber = instructor.getPhoneNumber();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, password);

        // Delete instructor
        String id = instructor.getId();
        instructorRepository.deleteById(id);
        instructor = instructorRepository.findInstructorByEmail(email);
        assertNull(instructor); // there should not be an instructor with the email raf@gmail.com
    }

    /**
     * @author Rafael Reis
     * Test goal: check for duplicate instructor email.
     * In this test we create an instructor and save it to the database.
     * Then we try to create another instructor.
     * We should detect this and not allow it.
     */
    @Test
    public void testMoreThanOneInstructor() {
        // Create instructor.
        Instructor instructor = createInstructor();
        String email = instructor.getEmail();
        String phoneNumber = instructor.getPhoneNumber();

        instructorRepository.save(instructor); // Save instructor to database.
        instructor = instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

        // Assert that instructor is not null (i.e. instructor was created and added to the database successfully)
        assertNotNull(instructor);
        checkAttributes(instructor, name, phoneNumber, email, password);

        // Create second instructor.
        Instructor instructor2 = createInstructor();
        if (instructorRepository.count()==0) instructorRepository.save(instructor2); // if there is no instructor in the DB, add the instructor.
        instructor2 = instructorRepository.findInstructorByEmail(instructor2.getEmail());
        assertNull(instructor2); // instructor2 shouldn't be in the DB since we can't have more than 1 instructor.
    }

    /**
     * @author Rafael Reis
     * Create a new instructor object and return it.
     * @return the new instructor object.
     */
    private Instructor createInstructor() {
        String phoneNumber = RandomGenerator.generateRandomPhoneNumber();
        String email = RandomGenerator.generateRandomEmail();

        Instructor instructor = new Instructor();
        setAttributes(instructor, phoneNumber, email);
        return instructor;
    }

    /**
     * @author Rafael Reis
     * Set attributes of the instructor parameter.
     * @param instructor the instructor whose attributes we will set.
     * @param phoneNumber the new instructor phone number.
     * @param email the new instructor email.
     */
    private void setAttributes(Instructor instructor, String phoneNumber, String email) {
        instructor.setName("Instructor");
        instructor.setPhoneNumber(phoneNumber);
        instructor.setEmail(email);
        instructor.setPassword("test");
    }

    /**
     * @author Rafael Reis
     * Perform JUnit tests to see whether the instructor attributes are correct or not.
     * @param instructor the instructor whose attributes we are checking.
     * @param name the expected name.
     * @param phoneNumber the expected phone number.
     * @param email the expected email address.
     * @param password the expected password.
     */
    private void checkAttributes(Instructor instructor, String name, String phoneNumber, String email, String password) {
        assertEquals(name, instructor.getName());
        assertEquals(phoneNumber, instructor.getPhoneNumber());
        assertEquals(email, instructor.getEmail());
        assertEquals(password, instructor.getPassword());
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

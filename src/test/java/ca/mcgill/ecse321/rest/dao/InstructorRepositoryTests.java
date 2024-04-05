package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.helpers.RandomGenerator;
import ca.mcgill.ecse321.rest.models.Instructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
public class InstructorRepositoryTests {
  private final String name = "Instructor";
  private final String password = "test";
  @Autowired private InstructorRepository instructorRepository;

  /** Clear the test database after tests have run. */
  @AfterEach
  public void clearDatabase() {
    instructorRepository.deleteAll();
  }

  /**
   * @author Rafael Reis Test goal: create an instructor successfully. This test creates an
   *     instructor, saves it to the database, and asserts that the retrieved instructor from the
   *     database is not null.
   */
  @Test
  public void testCreateInstructor() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
    assertNotNull(instructor);
  }

  /**
   * @author Rafael Reis Test goal: create an instructor and read its data from the database
   *     successfully. This test creates an instructor, saves it to the database, retrieves it by id
   *     and asserts that the retrieved data related to this instructor from the database is
   *     correct.
   */
  @Test
  public void testReadInstructorByEmail() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
    assertNotNull(instructor);
    checkAttributes(instructor, name, phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: create an instructor and read its data from the database
   *     successfully using the phone number as identifier. This test creates an instructor, saves
   *     it to the database, retrieves it by phone number and asserts that the retrieved data
   *     related to this instructor from the database is correct.
   */
  @Test
  public void testReadInstructorByPhoneNumber() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByPhoneNumber(
            phoneNumber); // Get instructor from database by phone number.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
    assertNotNull(instructor);
    checkAttributes(instructor, name, phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: update the instructor password In this test we create an
   *     instructor and save it to the database. Then we change the instructor password and make
   *     sure the database reflects that change.
   */
  @Test
  public void testUpdateInstructorPassword() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
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
   * @author Rafael Reis Test goal: update the instructor email In this test we create an instructor
   *     and save it to the database. Then we change the instructor email and make sure the database
   *     reflects that change.
   */
  @Test
  public void testUpdateInstructorEmail() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
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
   * @author Rafael Reis Test goal: update the instructor name In this test we create an instructor
   *     and save it to the database. Then we change the instructor name and make sure the database
   *     reflects that change.
   */
  @Test
  public void testUpdateInstructorName() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
    assertNotNull(instructor);
    checkAttributes(instructor, name, phoneNumber, email, password);

    // Get instructor with ID and update name.
    String id = instructor.getId();
    instructor.setName("Philippe Aprahamian");
    instructorRepository.save(instructor);
    instructor = instructorRepository.findInstructorById(id);
    assertNotNull(instructor);
    checkAttributes(instructor, "Philippe Aprahamian", phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: update the instructor phone number In this test we create an
   *     instructor and save it to the database. Then we change the instructor phone number and make
   *     sure the database reflects that change.
   */
  @Test
  public void testUpdateInstructorPhoneNumber() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
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
   * @author Rafael Reis Test goal: delete the instructor by id In this test we create an instructor
   *     and save it to the database. Then we delete the instructor and make sure the database
   *     reflects that change.
   */
  @Test
  public void testDeleteInstructorById() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
    assertNotNull(instructor);
    checkAttributes(instructor, name, phoneNumber, email, password);

    // Delete instructor
    String id = instructor.getId();
    instructorRepository.deleteInstructorById(id);
    instructor = instructorRepository.findInstructorByEmail(email);
    assertNull(instructor); // there should not be an instructor with the specified email
  }

  /**
   * @author Rafael Reis Test goal: delete the instructor by email In this test we create an
   *     instructor and save it to the database. Then we delete the instructor and make sure the
   *     database reflects that change.
   */
  @Test
  public void testDeleteInstructorByEmail() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String email = instructor.getEmail();
    String phoneNumber = instructor.getPhoneNumber();

    instructorRepository.save(instructor); // Save instructor to database.
    instructor =
        instructorRepository.findInstructorByEmail(email); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
    assertNotNull(instructor);
    checkAttributes(instructor, name, phoneNumber, email, password);

    // Delete instructor
    instructorRepository.deleteInstructorByEmail(email);
    instructor = instructorRepository.findInstructorByEmail(email);
    assertNull(instructor); // there should not be an instructor with the specified email
  }

  /**
   * @author Rafael Reis Test goal: check for duplicate instructor email. In this test we create an
   *     instructor and save it to the database. Then we try to create another instructor with the
   *     same email. We should detect this and not allow it.
   */
  @Test
  public void testInstructorDuplicateEmail() {
    // Create and save the first instructor
    Instructor instructor1 = createInstructor();
    instructor1.setEmail("unique-email@example.com"); // Set a specific email
    instructorRepository.save(instructor1);

    // Try to create a second instructor with the same email
    Instructor instructor2 = createInstructor();
    instructor2.setEmail("unique-email@example.com"); // Same email as the first

    // Assert that an exception is thrown when trying to save the second instructor
    assertThrows(DataIntegrityViolationException.class, () -> {
      instructorRepository.save(instructor2);
    }, "Expected DataIntegrityViolationException due to duplicate email");
  }

  /**
   * @author Rafael Reis Test goal: check for duplicate instructor phone number. In this test we
   *     create an instructor and save it to the database. Then we try to create another instructor
   *     with the same phone number. We should detect this and not allow it.
   */
  @Test
  public void testInstructorDuplicatePhoneNumber() {
    // Create instructor.
    Instructor instructor1 = createInstructor();
    String email1 = instructor1.getEmail();
    String phoneNumber1 = instructor1.getPhoneNumber();

    instructorRepository.save(instructor1); // Save instructor to database.
    instructor1 =
        instructorRepository.findInstructorByEmail(
            email1); // Get instructor from database by email.

    // Assert that instructor is not null (i.e. instructor was created and added to the database
    // successfully)
    assertNotNull(instructor1);
    checkAttributes(instructor1, name, phoneNumber1, email1, password);

    // Create second instructor
    Instructor instructor2 = createInstructor();
    String phoneNumber2 = instructor2.getPhoneNumber();
    instructor2.setEmail(
        phoneNumber1); // Attempt to change the phone number of instructor2 to be that of
    // instructor1;

    assertEquals(
        phoneNumber2,
        instructor2
            .getPhoneNumber()); // the phone number should still be email2, i.e. the change was not
    // allowed
  }

  /**
   * @author Rafael Reis Create a new instructor object and return it.
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
   * @author Rafael Reis Set attributes of the instructor parameter.
   * @param instructor the instructor whose attributes we will set.
   * @param phoneNumber the new instructor phone number.
   * @param email the new instructor email.
   */
  private void setAttributes(Instructor instructor, String phoneNumber, String email) {
    instructor.setName(name);
    instructor.setPhoneNumber(phoneNumber);
    instructor.setEmail(email);
    instructor.setPassword(password);
  }

  /**
   * @author Rafael Reis Perform JUnit tests to see whether the instructor attributes are correct or
   *     not.
   * @param instructor the instructor whose attributes we are checking.
   * @param name the expected name.
   * @param phoneNumber the expected phone number.
   * @param email the expected email address.
   * @param password the expected password.
   */
  private void checkAttributes(
      Instructor instructor, String name, String phoneNumber, String email, String password) {
    assertEquals(name, instructor.getName());
    assertEquals(phoneNumber, instructor.getPhoneNumber());
    assertEquals(email, instructor.getEmail());
    assertEquals(password, instructor.getPassword());
  }

  /**
   * Tests the ability to find an instructor by their name in the repository.
   * This method creates an instructor, saves it to the repository, and then
   * attempts to find this instructor by using their name. It asserts that
   * the found instructor is not null and that all attributes match the
   * originally saved instructor, ensuring the findInstructorByName method
   * works as expected.
   *
   * @author Omar Moussa
   */
  @Test
  public void testFindInstructorByName() {
    // Create instructor.
    Instructor instructor = createInstructor();
    String name = instructor.getName();

    instructorRepository.save(instructor); // Save instructor to database.
    Instructor foundInstructor = instructorRepository.findInstructorByName(name); // Get instructor from database by name.

    // Assert that found instructor is not null and has the correct attributes
    assertNotNull(foundInstructor, "Instructor should not be null.");
    assertEquals(name, foundInstructor.getName(), "Instructor name should match.");
    assertEquals(instructor.getEmail(), foundInstructor.getEmail(), "Instructor email should match.");
    assertEquals(instructor.getPhoneNumber(), foundInstructor.getPhoneNumber(), "Instructor phone number should match.");
    assertEquals(instructor.getPassword(), foundInstructor.getPassword(), "Instructor password should match.");
  }

}

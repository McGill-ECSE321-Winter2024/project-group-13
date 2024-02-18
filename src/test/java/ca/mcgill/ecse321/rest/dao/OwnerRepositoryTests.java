package ca.mcgill.ecse321.rest.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.rest.helpers.RandomGenerator;
import ca.mcgill.ecse321.rest.models.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OwnerRepositoryTests {
  private final String name = "Owner";
  private final String password = "test";
  @Autowired private OwnerRepository ownerRepository;

  /** Clear the test database after tests have run. */
  @AfterEach
  public void clearDatabase() {
    ownerRepository.deleteAll();
  }

  /**
   * @author Rafael Reis Test goal: create an owner successfully. This test creates an owner, saves
   *     it to the database, and asserts that the retrieved owner from the database is not null.
   */
  @Test
  public void testCreateOwner() {
    // Create owner.
    Owner owner = createOwner();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(email); // Get owner from database by email.

    // Assert that owner is not null (i.e. owner was created and added to the database successfully)
    assertNotNull(owner);
  }

  /**
   * @author Rafael Reis Test goal: create an owner and read its data from the database
   *     successfully. This test creates an owner, saves it to the database, retrieves it by name
   *     and asserts that the retrieved data related to this owner from the database is correct.
   */
  @Test
  public void testReadOwnerByName() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByName(name); // Get owner from database by name.

    // Assert that owner is not null (i.e. owner was created and added to the database successfully)
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: create an owner and read its data from the database
   *     successfully. This test creates an owner, saves it to the database, retrieves it by id and
   *     asserts that the retrieved data related to this owner from the database is correct.
   */
  @Test
  public void testReadOwnerByEmail() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(email); // Get owner from database by email.

    // Assert that owner is not null (i.e. owner was created and added to the database successfully)
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: create an owner and read its data from the database
   *     successfully. This test creates an owner, saves it to the database, retrieves it by
   *     phoneNumber and asserts that the retrieved data related to this owner from the database is
   *     correct.
   */
  @Test
  public void testReadOwnerByPhoneNumber() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner =
        ownerRepository.findOwnerByPhoneNumber(
            phoneNumber); // Get owner from database by phoneNumber.

    // Assert that owner is not null (i.e. owner was created and added to the database successfully)
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: update the owner password In this test we create an owner and
   *     save it to the database. Then we change the owner password and make sure the database
   *     reflects that change.
   */
  @Test
  public void testUpdateOwnerPassword() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(owner.getEmail()); // Get owner from database.

    // Assert that owner is not null and has correct attributes.
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);

    // Get owner with ID and update password.
    String id = owner.getId();
    owner.setPassword("password");
    ownerRepository.save(owner);
    owner = ownerRepository.findOwnerById(id);
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, "password");
  }

  /**
   * @author Rafael Reis Test goal: update the owner email In this test we create an owner and save
   *     it to the database. Then we change the owner email and make sure the database reflects that
   *     change.
   */
  @Test
  public void testUpdateOwnerEmail() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(owner.getEmail()); // Get owner from database.

    // Assert that owner is not null and has correct attributes.
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);

    // Get owner with ID and update email.
    String id = owner.getId();
    String newEmail = RandomGenerator.generateRandomEmail();
    owner.setEmail(newEmail);
    ownerRepository.save(owner);

    owner = ownerRepository.findOwnerById(id);
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, newEmail, password);
  }

  /**
   * @author Rafael Reis Test goal: update the owner name In this test we create an owner and save
   *     it to the database. Then we change the owner name and make sure the database reflects that
   *     change.
   */
  @Test
  public void testUpdateOwnerName() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(owner.getEmail()); // Get owner from database.

    // Assert that owner is not null and has correct attributes.
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);

    // Get owner with ID and update name.
    String id = owner.getId();
    owner.setName("Philippe Aprahamian");
    ownerRepository.save(owner);
    owner = ownerRepository.findOwnerById(id);
    assertNotNull(owner);
    checkAttributes(owner, "Philippe Aprahamian", phoneNumber, email, password);
  }

  /**
   * @author Rafael Reis Test goal: update the owner phone number In this test we create an owner
   *     and save it to the database. Then we change the owner phone number and make sure the
   *     database reflects that change.
   */
  @Test
  public void testUpdateOwnerPhoneNumber() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(owner.getEmail()); // Get owner from database.

    // Assert that owner is not null and has correct attributes.
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);

    // Get owner with ID and update phone number.
    String id = owner.getId();
    owner.setPhoneNumber("+33750901255");
    ownerRepository.save(owner);
    owner = ownerRepository.findOwnerById(id);
    // Check new phone number
    assertNotNull(owner);
    checkAttributes(owner, name, "+33750901255", email, password);
  }

  /**
   * @author Rafael Reis Test goal: delete the owner In this test we create an owner and save it to
   *     the database. Then we delete the owner and make sure the database reflects that change.
   */
  @Test
  public void testDeleteOwner() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(owner.getEmail()); // Get owner from database.

    // Assert that owner is not null and has correct attributes.
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);

    // Delete Owner
    String id = owner.getId();
    ownerRepository.deleteOwnerById(id);
    owner = ownerRepository.findOwnerByEmail(email);
    assertNull(owner); // there should not be an owner with the specified email
  }

  /**
   * @author Rafael Reis Test goal: check for duplicate owner email. In this test we create an owner
   *     and save it to the database. Then we try to create another owner. We should detect this and
   *     not allow it.
   */
  @Test
  public void testMoreThanOneOwner() {
    // Create owner.
    Owner owner = createOwner();
    String phoneNumber = owner.getPhoneNumber();
    String email = owner.getEmail();

    ownerRepository.save(owner); // Save owner to database.
    owner = ownerRepository.findOwnerByEmail(owner.getEmail()); // Get owner from database.

    // Assert that owner is not null and has correct attributes.
    assertNotNull(owner);
    checkAttributes(owner, name, phoneNumber, email, password);

    // Create second owner.
    Owner owner2 = createOwner();
    if (ownerRepository.count() == 0)
      ownerRepository.save(owner2); // if there is no owner in the DB, add the owner.
    owner2 = ownerRepository.findOwnerByEmail(owner2.getEmail());
    assertNull(owner2); // owner2 shouldn't be in the DB since we can't have more than 1 Owner.
  }

  /**
   * @author Rafael Reis Create a new owner object and return it.
   * @return the new Owner object.
   */
  private Owner createOwner() {
    String phoneNumber = RandomGenerator.generateRandomPhoneNumber();
    String email = RandomGenerator.generateRandomEmail();

    Owner owner = new Owner();
    setAttributes(owner, phoneNumber, email);
    return owner;
  }

  /**
   * @author Rafael Reis Set attributes of the owner parameter.
   * @param owner the owner whose attributes we will set.
   * @param phoneNumber the new owner phone number.
   * @param email the new owner email.
   */
  private void setAttributes(Owner owner, String phoneNumber, String email) {
    owner.setName(name);
    owner.setPhoneNumber(phoneNumber);
    owner.setEmail(email);
    owner.setPassword(password);
  }

  /**
   * @author Rafael Reis Perform JUnit tests to see whether the owner attributes are correct or not.
   * @param owner the owner whose attributes we are checking.
   * @param name the expected name.
   * @param phoneNumber the expected phone number.
   * @param email the expected email address.
   * @param password the expected password.
   */
  private void checkAttributes(
      Owner owner, String name, String phoneNumber, String email, String password) {
    assertEquals(name, owner.getName());
    assertEquals(phoneNumber, owner.getPhoneNumber());
    assertEquals(email, owner.getEmail());
    assertEquals(password, owner.getPassword());
  }
}

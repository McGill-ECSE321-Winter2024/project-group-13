package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public abstract class Person {

  private static Map<String, Person> personsByEmail = new HashMap<String, Person>();
  private static Map<String, Person> personsByPhoneNumber = new HashMap<String, Person>();

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Person Attributes
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;

  @Column(unique = true, nullable = false)
  @Email
  private String email;

  @Column(unique = true, nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Person() {}


  /* Code from template attribute_GetUnique */
  public static Person getWithEmail(String aEmail) {
    return personsByEmail.get(aEmail);
  }

  /* Code from template attribute_HasUnique */
  public static boolean hasWithEmail(String aEmail) {
    return getWithEmail(aEmail) != null;
  }

  /* Code from template attribute_GetUnique */
  public static Person getWithPhoneNumber(String aPhoneNumber) {
    return personsByPhoneNumber.get(aPhoneNumber);
  }

  /* Code from template attribute_HasUnique */
  public static boolean hasWithPhoneNumber(String aPhoneNumber) {
    return getWithPhoneNumber(aPhoneNumber) != null;
  }

  public boolean setId(String aId) {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail) {
    email = aEmail;
    return true;
  }

  public boolean setPhoneNumber(String aPhoneNumber) {
    if (phoneNumber != aPhoneNumber) {
      phoneNumber = aPhoneNumber;
      return true;
    }
    return false;
  }

  public boolean setPassword(String aPassword) {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public void delete() {
    personsByEmail.remove(getEmail());
    personsByPhoneNumber.remove(getPhoneNumber());
  }

  public String toString() {
    return super.toString()
        + "["
        + "id"
        + ":"
        + getId()
        + ","
        + "email"
        + ":"
        + getEmail()
        + ","
        + "phoneNumber"
        + ":"
        + getPhoneNumber()
        + ","
        + "password"
        + ":"
        + getPassword()
        + ","
        + "name"
        + ":"
        + getName()
        + "]";
  }


}

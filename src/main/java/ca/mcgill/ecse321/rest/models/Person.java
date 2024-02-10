/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

// line 15 "../../../../../DomainModel.ump"


@Entity
public abstract class Person
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Person> personsByEmail = new HashMap<String, Person>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Person Attributes
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)

  private String name;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Person(String aId, String aEmail, String aPassword, String aName)
  {
    id = aId;
    password = aPassword;
    name = aName;
    if (!setEmail(aEmail))
    {
      throw new RuntimeException("Cannot create due to duplicate email. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
  }

  public Person() {

  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(String aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    String anOldEmail = getEmail();
    if (anOldEmail != null && anOldEmail.equals(aEmail)) {
      return true;
    }
    if (hasWithEmail(aEmail)) {
      return wasSet;
    }
    email = aEmail;
    wasSet = true;
    if (anOldEmail != null) {
      personsByEmail.remove(anOldEmail);
    }
    personsByEmail.put(aEmail, this);
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getId()
  {
    return id;
  }

  public String getEmail()
  {
    return email;
  }
  /* Code from template attribute_GetUnique */
  public static Person getWithEmail(String aEmail)
  {
    return personsByEmail.get(aEmail);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithEmail(String aEmail)
  {
    return getWithEmail(aEmail) != null;
  }

  public String getPassword()
  {
    return password;
  }

  public String getName()
  {
    return name;
  }

  public void delete()
  {
    personsByEmail.remove(getEmail());
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "," +
            "name" + ":" + getName()+ "]";
  }
}

package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Registration
{

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;
  private int rating;

  @ManyToOne
  private Customer customer;
  @ManyToOne
  private Course course;

  public Registration(String aId, int aRating, Customer aCustomer, Course aCourse)
  {
    id = aId;
    rating = aRating;
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create Registration due to aCustomer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setCourse(aCourse))
    {
      throw new RuntimeException("Unable to create Registration due to aCourse. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Registration() {

  }


  public boolean setId(String aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRating(int aRating)
  {
    boolean wasSet = false;
    rating = aRating;
    wasSet = true;
    return wasSet;
  }

  public String getId()
  {
    return id;
  }

  public int getRating()
  {
    return rating;
  }
  public Customer getCustomer()
  {
    return customer;
  }
  public Course getCourse()
  {
    return course;
  }
  public boolean setCustomer(Customer aNewCustomer)
  {
    boolean wasSet = false;
    if (aNewCustomer != null)
    {
      customer = aNewCustomer;
      wasSet = true;
    }
    return wasSet;
  }
  public boolean setCourse(Course aNewCourse)
  {
    boolean wasSet = false;
    if (aNewCourse != null)
    {
      course = aNewCourse;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    customer = null;
    course = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "rating" + ":" + getRating()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "course = "+(getCourse()!=null?Integer.toHexString(System.identityHashCode(getCourse())):"null");
  }
}
package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.GenericGenerator;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "course_id"}))
@Entity
public class Registration {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;

  @Min(0)
  @Max(5)
  private int rating = 0;

  @ManyToOne private Customer customer;
  @ManyToOne private Course course;

  public Registration(String aId, int aRating, Customer aCustomer, Course aCourse) {

    if (aRating <= 0) {
      throw new IllegalArgumentException("Rating should be strictly positive.");
    }
    id = aId;

    rating = aRating;
    if (!setCustomer(aCustomer)) {
      throw new RuntimeException(
          "Unable to create Registration due to aCustomer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setCourse(aCourse)) {
      throw new RuntimeException(
          "Unable to create Registration due to aCourse. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Registration() {}

  public boolean setRating(int aRating) {
    boolean wasSet = false;

    if (aRating <= 0) {
      return wasSet;
    }

    rating = aRating;
    wasSet = true;
    return wasSet;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getRating() {
    return rating;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Course getCourse() {
    return course;
  }

  public boolean setCustomer(Customer aNewCustomer) {
    boolean wasSet = false;
    if (aNewCustomer != null) {
      customer = aNewCustomer;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setCourse(Course aNewCourse) {
    boolean wasSet = false;
    if (aNewCourse != null) {
      course = aNewCourse;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    customer = null;
    course = null;
  }

  public String toString() {
    return super.toString()
        + "["
        + "id"
        + ":"
        + getId()
        + ","
        + "rating"
        + ":"
        + getRating()
        + "]"
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "customer = "
        + (getCustomer() != null
            ? Integer.toHexString(System.identityHashCode(getCustomer()))
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "course = "
        + (getCourse() != null
            ? Integer.toHexString(System.identityHashCode(getCourse()))
            : "null");
  }
}

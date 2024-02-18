package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class CourseSession {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp startTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp endTime;

  @ManyToOne private Course course;

  public CourseSession() {}

  public boolean setId(String aId) {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public String getId() {
    return id;
  }

  public boolean setStartTime(Timestamp aStartTime) {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public Timestamp getStartTime() {
    return startTime;
  }

  public boolean setEndTime(Timestamp aEndTime) {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public Timestamp getEndTime() {
    return endTime;
  }

  public Course getCourse() {
    return course;
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
    course = null;
  }

  public String toString() {
    return super.toString()
        + "["
        + "id"
        + ":"
        + getId()
        + "]"
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "startTime"
        + "="
        + (getStartTime() != null
            ? !getStartTime().equals(this)
                ? getStartTime().toString().replaceAll("  ", "    ")
                : "this"
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "endTime"
        + "="
        + (getEndTime() != null
            ? !getEndTime().equals(this) ? getEndTime().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "course = "
        + (getCourse() != null
            ? Integer.toHexString(System.identityHashCode(getCourse()))
            : "null");
  }
}

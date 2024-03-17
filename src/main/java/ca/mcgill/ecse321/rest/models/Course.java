package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Course {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;

  @Column(unique = true)
  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  private Level level;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp courseStartDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp courseEndDate;

  @ManyToOne private Room room;
  @ManyToOne private SportCenter sportCenter;
  @ManyToOne private Instructor instructor;
  @OneToOne private Schedule schedule;
  private Double hourlyRateAmount;
  private CourseState courseState;

  public Course() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public Timestamp getCourseStartDate() {
    return courseStartDate;
  }

  public void setCourseStartDate(Timestamp courseStartDate) {
    this.courseStartDate = courseStartDate;
  }

  public Timestamp getCourseEndDate() {
    return courseEndDate;
  }

  public void setCourseEndDate(Timestamp courseEndDate) {
    this.courseEndDate = courseEndDate;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public SportCenter getSportCenter() {
    return sportCenter;
  }

  public void setSportCenter(SportCenter sportCenter) {
    this.sportCenter = sportCenter;
  }

  public Instructor getInstructor() {
    return instructor;
  }

  public void setInstructor(Instructor instructor) {
    this.instructor = instructor;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public Double getHourlyRateAmount() {
    return hourlyRateAmount;
  }

  public void setHourlyRateAmount(Double hourlyRateAmount) {
    this.hourlyRateAmount = hourlyRateAmount;
  }

  public CourseState getCourseState() {
    return courseState;
  }
  public void setLevel(String level) {
    this.level = Level.valueOf(level);
  }
  public void setCourseState(String courseState) {
    this.courseState = CourseState.valueOf(courseState);
  }
  public void setCourseState(CourseState courseState) {
    this.courseState = courseState;
  }

  public String toString() {
    return super.toString()
        + "["
        + "id"
        + ":"
        + getId()
        + ","
        + "name"
        + ":"
        + getName()
        + ","
        + "description"
        + ":"
        + getDescription()
        + "]"
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "level"
        + "="
        + (getLevel() != null
            ? !getLevel().equals(this) ? getLevel().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "courseStartDate"
        + "="
        + (getCourseStartDate() != null
            ? !getCourseStartDate().equals(this)
                ? getCourseStartDate().toString().replaceAll("  ", "    ")
                : "this"
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "courseEndDate"
        + "="
        + (getCourseEndDate() != null
            ? !getCourseEndDate().equals(this)
                ? getCourseEndDate().toString().replaceAll("  ", "    ")
                : "this"
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "room = "
        + (getRoom() != null ? Integer.toHexString(System.identityHashCode(getRoom())) : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "sportCenter = "
        + (getSportCenter() != null
            ? Integer.toHexString(System.identityHashCode(getSportCenter()))
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "instructor = "
        + (getInstructor() != null
            ? Integer.toHexString(System.identityHashCode(getInstructor()))
            : "null");
  }

  public enum CourseState {
    Approved,
    Denied,
    Inactive,
    AwaitingApproval
  }

  public enum Level {
    Beginner,
    Intermediate,
    Advanced
  }
}

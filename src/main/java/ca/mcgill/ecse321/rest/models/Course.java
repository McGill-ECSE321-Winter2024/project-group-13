
package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.*;

@Entity
public class Course
{
  public Course() {

  }
  public enum CourseState { Approved, Denied, Inactive }
  public enum Level { Beginner, Intermediate, Advanced }

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
  @ManyToOne
  private Room room;
  @ManyToOne
  private SportCenter sportCenter;
  @ManyToOne
  private Instructor instructor;
  @OneToOne
  private Schedule schedule;
  private Double hourlyRateAmount;
  private CourseState courseState;

  public void setId(String id) {
    this.id = id;
  }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setCourseStartDate(Timestamp courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public Timestamp getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseEndDate(Timestamp courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public Timestamp getCourseEndDate() {
        return courseEndDate;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setSportCenter(SportCenter sportCenter) {
        this.sportCenter = sportCenter;
    }

    public SportCenter getSportCenter() {
        return sportCenter;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }
    public void setHourlyRateAmount(Double hourlyRateAmount) {
        this.hourlyRateAmount = hourlyRateAmount;
    }

    public Double getHourlyRateAmount() {
        return hourlyRateAmount;
  }
    public CourseState getCourseState() {
        return courseState;
    }

    public void setCourseState(CourseState courseState) {
        this.courseState = courseState;
    }
  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "level" + "=" + (getLevel() != null ? !getLevel().equals(this)  ? getLevel().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "courseStartDate" + "=" + (getCourseStartDate() != null ? !getCourseStartDate().equals(this)  ? getCourseStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "courseEndDate" + "=" + (getCourseEndDate() != null ? !getCourseEndDate().equals(this)  ? getCourseEndDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "room = "+(getRoom()!=null?Integer.toHexString(System.identityHashCode(getRoom())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "sportCenter = "+(getSportCenter()!=null?Integer.toHexString(System.identityHashCode(getSportCenter())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "instructor = "+(getInstructor()!=null?Integer.toHexString(System.identityHashCode(getInstructor())):"null");
  }
}

package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.*;
import java.sql.Date;

@Entity
public class Course
{
  public Course() {

  }

  public enum Level { Beginner, Intermediate, Advanced }
  private static Map<String, Course> coursesByName = new HashMap<String, Course>();
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
  @Temporal(TemporalType.DATE)
  private Date courseStartDate;
  @Temporal(TemporalType.DATE)
  private Date courseEndDate;
  @ManyToOne
  private Room room;
  @ManyToOne
  private SportCenter sportCenter;
  @ManyToOne
  private Instructor instructor;

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

    public void setCourseStartDate(Date courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public Date getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseEndDate(Date courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public Date getCourseEndDate() {
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
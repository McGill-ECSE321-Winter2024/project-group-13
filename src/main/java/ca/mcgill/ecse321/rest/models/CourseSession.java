package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;

@Entity
public class CourseSession
{

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;
  @Temporal(TemporalType.DATE)
  private Date dayOfWeek;
  @Temporal(TemporalType.TIME)
  private Time startTime;
  @Temporal(TemporalType.TIME)
  private Time endTime;

  @ManyToOne
  private Course course;


  public CourseSession(String aId, Date dayOfWeek, Time aStartTime, Time aEndTime, Course aCourse)
  {
    id = aId;
    this.dayOfWeek = dayOfWeek;
    startTime = aStartTime;
    endTime = aEndTime;
    boolean didAddCourse = setCourse(aCourse);
    if (!didAddCourse)
    {
      throw new RuntimeException("Unable to create session due to course. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public CourseSession() {

  }

  public boolean setId(String aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setDay(Date aDay)
  {
    boolean wasSet = false;
    this.dayOfWeek = aDay;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public String getId()
  {
    return id;
  }

  public Date getADay()
  {
    return dayOfWeek;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }
  public Course getCourse()
  {
    return course;
  }
  public boolean setCourse(Course aCourse)
  {
    boolean wasSet = false;
    if (aCourse == null)
    {
      return wasSet;
    }

    Course existingCourse = course;
    course = aCourse;
    if (existingCourse != null && !existingCourse.equals(aCourse))
    {
      existingCourse.removeSession(this);
    }
    course.addSession(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Course placeholderCourse = course;
    this.course = null;
    if(placeholderCourse != null)
    {
      placeholderCourse.removeSession(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "day" + "=" + (getADay() != null ? !getADay().equals(this)  ? getADay().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "course = "+(getCourse()!=null?Integer.toHexString(System.identityHashCode(getCourse())):"null");
  }
}
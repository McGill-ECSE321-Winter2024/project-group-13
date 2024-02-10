/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;

// line 49 "../../../../../DomainModel.ump"
@Entity
public class CourseSession
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CourseSession Attributes
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;
  @Temporal(TemporalType.DATE)
  private Date day;
  @Temporal(TemporalType.DATE)
  private Date startTime;
  @Temporal(TemporalType.DATE)
  private Date endTime;

  //CourseSession Associations
  @ManyToOne
  private Course course;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CourseSession(String aId, Date aDay, Date aStartTime, Date aEndTime, Course aCourse)
  {
    id = aId;
    day = aDay;
    startTime = aStartTime;
    endTime = aEndTime;
    boolean didAddCourse = setCourse(aCourse);
    if (!didAddCourse)
    {
      throw new RuntimeException("Unable to create session due to course. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  public boolean setDay(Date aDay)
  {
    boolean wasSet = false;
    day = aDay;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Date aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Date aEndTime)
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

  public Date getDay()
  {
    return day;
  }

  public Date getStartTime()
  {
    return startTime;
  }

  public Date getEndTime()
  {
    return endTime;
  }
  /* Code from template association_GetOne */
  public Course getCourse()
  {
    return course;
  }
  /* Code from template association_SetOneToMany */
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
            "  " + "day" + "=" + (getDay() != null ? !getDay().equals(this)  ? getDay().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "course = "+(getCourse()!=null?Integer.toHexString(System.identityHashCode(getCourse())):"null");
  }
}
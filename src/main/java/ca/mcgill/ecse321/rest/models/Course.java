
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
    this.sessions = new ArrayList<>();

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

  @OneToMany(
      mappedBy = "course",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<CourseSession> sessions;
  @ManyToOne
  private Room room;
  @ManyToOne
  private SportCenter sportCenter;
  @ManyToOne
  private Instructor instructor;

  public Course(String aId, String aName, String aDescription, Level aLevel, Date aCourseStartDate, Date aCourseEndDate, Room aRoom, SportCenter aSportCenter, Instructor aInstructor)
  {
    id = aId;
    description = aDescription;
    level = aLevel;
    courseStartDate = aCourseStartDate;
    courseEndDate = aCourseEndDate;
    if (!setName(aName))
    {
      throw new RuntimeException("Cannot create due to duplicate name. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    sessions = new ArrayList<CourseSession>();
    if (!setRoom(aRoom))
    {
      throw new RuntimeException("Unable to create Course due to aRoom. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter)
    {
      throw new RuntimeException("Unable to create course due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setInstructor(aInstructor))
    {
      throw new RuntimeException("Unable to create Course due to aInstructor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }


  public boolean setId(String aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    String anOldName = getName();
    if (anOldName != null && anOldName.equals(aName)) {
      return true;
    }
    if (hasWithName(aName)) {
      return wasSet;
    }
    name = aName;
    wasSet = true;
    if (anOldName != null) {
      coursesByName.remove(anOldName);
    }
    coursesByName.put(aName, this);
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setLevel(Level aLevel)
  {
    boolean wasSet = false;
    level = aLevel;
    wasSet = true;
    return wasSet;
  }

  public boolean setCourseStartDate(Date aCourseStartDate)
  {
    boolean wasSet = false;
    courseStartDate = aCourseStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setCourseEndDate(Date aCourseEndDate)
  {
    boolean wasSet = false;
    courseEndDate = aCourseEndDate;
    wasSet = true;
    return wasSet;
  }

  public String getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }
  public static Course getWithName(String aName)
  {
    return coursesByName.get(aName);
  }
  public static boolean hasWithName(String aName)
  {
    return getWithName(aName) != null;
  }

  public String getDescription()
  {
    return description;
  }

  public Level getLevel()
  {
    return level;
  }

  public Date getCourseStartDate()
  {
    return courseStartDate;
  }

  public Date getCourseEndDate()
  {
    return courseEndDate;
  }
  public CourseSession getSession(int index)
  {
    CourseSession aSession = sessions.get(index);
    return aSession;
  }

  public List<CourseSession> getSessions()
  {
    List<CourseSession> newSessions = Collections.unmodifiableList(sessions);
    return newSessions;
  }

  public int numberOfSessions()
  {
    int number = sessions.size();
    return number;
  }

  public boolean hasSessions()
  {
    boolean has = sessions.size() > 0;
    return has;
  }

  public int indexOfSession(CourseSession aSession)
  {
    int index = sessions.indexOf(aSession);
    return index;
  }
  public Room getRoom()
  {
    return room;
  }
  public SportCenter getSportCenter()
  {
    return sportCenter;
  }
  public Instructor getInstructor()
  {
    return instructor;
  }
  public static int minimumNumberOfSessions()
  {
    return 0;
  }
  public CourseSession addSession(String aId, Date aDay, Time aStartTime, Time aEndTime)
  {
    return new CourseSession(aId, aDay, aStartTime, aEndTime, this);
  }

  public boolean addSession(CourseSession aSession)
  {
    boolean wasAdded = false;
    if (sessions.contains(aSession)) { return false; }
    Course existingCourse = aSession.getCourse();
    boolean isNewCourse = existingCourse != null && !this.equals(existingCourse);
    if (isNewCourse)
    {
      aSession.setCourse(this);
    }
    else
    {
      sessions.add(aSession);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSession(CourseSession aSession)
  {
    boolean wasRemoved = false;
    if (!this.equals(aSession.getCourse()))
    {
      sessions.remove(aSession);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  public boolean addSessionAt(CourseSession aSession, int index)
  {
    boolean wasAdded = false;
    if(addSession(aSession))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSessions()) { index = numberOfSessions() - 1; }
      sessions.remove(aSession);
      sessions.add(index, aSession);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSessionAt(CourseSession aSession, int index)
  {
    boolean wasAdded = false;
    if(sessions.contains(aSession))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSessions()) { index = numberOfSessions() - 1; }
      sessions.remove(aSession);
      sessions.add(index, aSession);
      wasAdded = true;
    }
    else
    {
      wasAdded = addSessionAt(aSession, index);
    }
    return wasAdded;
  }
  public boolean setRoom(Room aNewRoom)
  {
    boolean wasSet = false;
    if (aNewRoom != null)
    {
      room = aNewRoom;
      wasSet = true;
    }
    return wasSet;
  }
  public boolean setSportCenter(SportCenter aSportCenter)
  {
    boolean wasSet = false;
    if (aSportCenter == null)
    {
      return wasSet;
    }

    SportCenter existingSportCenter = sportCenter;
    sportCenter = aSportCenter;
    if (existingSportCenter != null && !existingSportCenter.equals(aSportCenter))
    {
      existingSportCenter.removeCourse(this);
    }
    sportCenter.addCourse(this);
    wasSet = true;
    return wasSet;
  }
  public boolean setInstructor(Instructor aNewInstructor)
  {
    boolean wasSet = false;
    if (aNewInstructor != null)
    {
      instructor = aNewInstructor;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    coursesByName.remove(getName());
    while (sessions.size() > 0)
    {
      CourseSession aSession = sessions.get(sessions.size() - 1);
      aSession.delete();
      sessions.remove(aSession);
    }

    room = null;
    SportCenter placeholderSportCenter = sportCenter;
    this.sportCenter = null;
    if(placeholderSportCenter != null)
    {
      placeholderSportCenter.removeCourse(this);
    }
    instructor = null;
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
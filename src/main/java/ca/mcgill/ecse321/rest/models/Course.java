/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

package ca.mcgill.ecse321.rest.models;
import java.util.*;
import java.sql.Date;

// line 36 "../../../../../../model.ump"
// line 128 "../../../../../../model.ump"
public class Course
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Level { Beginner, Intermediate, Advanced }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Course> coursesByName = new HashMap<String, Course>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Course Attributes
  private String id;
  private String name;
  private String description;
  private Level level;
  private Date courseStartDate;
  private Date courseEndDate;

  //Course Associations
  private List<CourseSession> sessions;
  private SportCenter sportCenter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Course(String aId, String aName, String aDescription, Level aLevel, Date aCourseStartDate, Date aCourseEndDate, SportCenter aSportCenter)
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
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter)
    {
      throw new RuntimeException("Unable to create course due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
  /* Code from template attribute_GetUnique */
  public static Course getWithName(String aName)
  {
    return coursesByName.get(aName);
  }
  /* Code from template attribute_HasUnique */
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
  /* Code from template association_GetMany */
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
  /* Code from template association_GetOne */
  public SportCenter getSportCenter()
  {
    return sportCenter;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSessions()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CourseSession addSession(String aId, Date aDay, Date aStartTime, Date aEndTime, Room aRoom, Instructor aInstructor)
  {
    return new CourseSession(aId, aDay, aStartTime, aEndTime, aRoom, this, aInstructor);
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
    //Unable to remove aSession, as it must always have a course
    if (!this.equals(aSession.getCourse()))
    {
      sessions.remove(aSession);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
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
  /* Code from template association_SetOneToMany */
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

  public void delete()
  {
    coursesByName.remove(getName());
    while (sessions.size() > 0)
    {
      CourseSession aSession = sessions.get(sessions.size() - 1);
      aSession.delete();
      sessions.remove(aSession);
    }
    
    SportCenter placeholderSportCenter = sportCenter;
    this.sportCenter = null;
    if(placeholderSportCenter != null)
    {
      placeholderSportCenter.removeCourse(this);
    }
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
            "  " + "sportCenter = "+(getSportCenter()!=null?Integer.toHexString(System.identityHashCode(getSportCenter())):"null");
  }
}
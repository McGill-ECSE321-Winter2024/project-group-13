package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;
import java.sql.Date;

@Entity
public class SportCenter
{

  @Id
  @GeneratedValue(generator = "UUID")
  private String id;
  private String openingHour;
  private String closingHour;
  private String address;

  @OneToMany(mappedBy = "sportCenter", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Room> rooms;
  @OneToOne
  private Owner owner;
  @OneToMany(mappedBy = "sportCenter", cascade = CascadeType.ALL, orphanRemoval = true)

  private List<Instructor> instructors;
  @OneToMany(mappedBy = "sportCenter", cascade = CascadeType.ALL, orphanRemoval = true)

  private List<Customer> customers;
  @OneToMany(mappedBy = "sportCenter", cascade = CascadeType.ALL, orphanRemoval = true)

  private List<Course> courses;


  public SportCenter(String aOpeningHour, String aClosingHour, String aAddress, Owner aOwner)
  {
    openingHour = aOpeningHour;
    closingHour = aClosingHour;
    address = aAddress;
    rooms = new ArrayList<Room>();
    if (aOwner == null || aOwner.getSportCenter() != null)
    {
      throw new RuntimeException("Unable to create SportCenter due to aOwner. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    owner = aOwner;
    instructors = new ArrayList<Instructor>();
    customers = new ArrayList<Customer>();
    courses = new ArrayList<Course>();
  }

  public SportCenter(String aOpeningHour, String aClosingHour, String aAddress, String aIdForOwner, String aEmailForOwner, String aPhoneNumberForOwner, String aPasswordForOwner, String aNameForOwner)
  {
    openingHour = aOpeningHour;
    closingHour = aClosingHour;
    address = aAddress;
    rooms = new ArrayList<Room>();
    owner = new Owner(aIdForOwner, aEmailForOwner, aPhoneNumberForOwner, aPasswordForOwner, aNameForOwner, this);
    instructors = new ArrayList<Instructor>();
    customers = new ArrayList<Customer>();
    courses = new ArrayList<Course>();
  }


  public SportCenter() {

  }

  public boolean setOpeningHour(String aOpeningHour)
  {
    boolean wasSet = false;
    openingHour = aOpeningHour;
    wasSet = true;
    return wasSet;
  }

  public boolean setClosingHour(String aClosingHour)
  {
    boolean wasSet = false;
    closingHour = aClosingHour;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public String getOpeningHour()
  {
    return openingHour;
  }

  public String getClosingHour()
  {
    return closingHour;
  }

  public String getAddress()
  {
    return address;
  }
  /* Code from template association_GetMany */
  public Room getRoom(int index)
  {
    Room aRoom = rooms.get(index);
    return aRoom;
  }

  public List<Room> getRooms()
  {
    List<Room> newRooms = Collections.unmodifiableList(rooms);
    return newRooms;
  }

  public int numberOfRooms()
  {
    int number = rooms.size();
    return number;
  }

  public boolean hasRooms()
  {
    boolean has = rooms.size() > 0;
    return has;
  }

  public int indexOfRoom(Room aRoom)
  {
    int index = rooms.indexOf(aRoom);
    return index;
  }
  /* Code from template association_GetOne */
  public Owner getOwner()
  {
    return owner;
  }
  /* Code from template association_GetMany */
  public Instructor getInstructor(int index)
  {
    Instructor aInstructor = instructors.get(index);
    return aInstructor;
  }

  public List<Instructor> getInstructors()
  {
    List<Instructor> newInstructors = Collections.unmodifiableList(instructors);
    return newInstructors;
  }

  public int numberOfInstructors()
  {
    int number = instructors.size();
    return number;
  }

  public boolean hasInstructors()
  {
    boolean has = instructors.size() > 0;
    return has;
  }

  public int indexOfInstructor(Instructor aInstructor)
  {
    int index = instructors.indexOf(aInstructor);
    return index;
  }
  /* Code from template association_GetMany */
  public Customer getCustomer(int index)
  {
    Customer aCustomer = customers.get(index);
    return aCustomer;
  }

  public List<Customer> getCustomers()
  {
    List<Customer> newCustomers = Collections.unmodifiableList(customers);
    return newCustomers;
  }

  public int numberOfCustomers()
  {
    int number = customers.size();
    return number;
  }

  public boolean hasCustomers()
  {
    boolean has = customers.size() > 0;
    return has;
  }

  public int indexOfCustomer(Customer aCustomer)
  {
    int index = customers.indexOf(aCustomer);
    return index;
  }
  /* Code from template association_GetMany */
  public Course getCourse(int index)
  {
    Course aCourse = courses.get(index);
    return aCourse;
  }

  public List<Course> getCourses()
  {
    List<Course> newCourses = Collections.unmodifiableList(courses);
    return newCourses;
  }

  public int numberOfCourses()
  {
    int number = courses.size();
    return number;
  }

  public boolean hasCourses()
  {
    boolean has = courses.size() > 0;
    return has;
  }

  public int indexOfCourse(Course aCourse)
  {
    int index = courses.indexOf(aCourse);
    return index;
  }
  public static int minimumNumberOfRooms()
  {
    return 0;
  }
  public Room addRoom(String aId, String aRoomName)
  {
    return new Room(aId, aRoomName, this);
  }

  public boolean addRoom(Room aRoom)
  {
    boolean wasAdded = false;
    if (rooms.contains(aRoom)) { return false; }
    SportCenter existingSportCenter = aRoom.getSportCenter();
    boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
    if (isNewSportCenter)
    {
      aRoom.setSportCenter(this);
    }
    else
    {
      rooms.add(aRoom);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRoom(Room aRoom)
  {
    boolean wasRemoved = false;
    if (!this.equals(aRoom.getSportCenter()))
    {
      rooms.remove(aRoom);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  public boolean addRoomAt(Room aRoom, int index)
  {
    boolean wasAdded = false;
    if(addRoom(aRoom))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRooms()) { index = numberOfRooms() - 1; }
      rooms.remove(aRoom);
      rooms.add(index, aRoom);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRoomAt(Room aRoom, int index)
  {
    boolean wasAdded = false;
    if(rooms.contains(aRoom))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRooms()) { index = numberOfRooms() - 1; }
      rooms.remove(aRoom);
      rooms.add(index, aRoom);
      wasAdded = true;
    }
    else
    {
      wasAdded = addRoomAt(aRoom, index);
    }
    return wasAdded;
  }
  public static int minimumNumberOfInstructors()
  {
    return 0;
  }
  public Instructor addInstructor(String aId, String aEmail, String aPhoneNumber, String aPassword, String aName)
  {
    return new Instructor(aId, aEmail, aPhoneNumber, aPassword, aName, this);
  }

  public boolean addInstructor(Instructor aInstructor)
  {
    boolean wasAdded = false;
    if (instructors.contains(aInstructor)) { return false; }
    SportCenter existingSportCenter = aInstructor.getSportCenter();
    boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
    if (isNewSportCenter)
    {
      aInstructor.setSportCenter(this);
    }
    else
    {
      instructors.add(aInstructor);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeInstructor(Instructor aInstructor)
  {
    boolean wasRemoved = false;
    //Unable to remove aInstructor, as it must always have a sportCenter
    if (!this.equals(aInstructor.getSportCenter()))
    {
      instructors.remove(aInstructor);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  public boolean addInstructorAt(Instructor aInstructor, int index)
  {
    boolean wasAdded = false;
    if(addInstructor(aInstructor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInstructors()) { index = numberOfInstructors() - 1; }
      instructors.remove(aInstructor);
      instructors.add(index, aInstructor);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveInstructorAt(Instructor aInstructor, int index)
  {
    boolean wasAdded = false;
    if(instructors.contains(aInstructor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInstructors()) { index = numberOfInstructors() - 1; }
      instructors.remove(aInstructor);
      instructors.add(index, aInstructor);
      wasAdded = true;
    }
    else
    {
      wasAdded = addInstructorAt(aInstructor, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Customer addCustomer(String aId, String aEmail, String aPhoneNumber, String aPassword, String aName)
  {
    return new Customer(aId, aEmail, aPhoneNumber, aPassword, aName, this);
  }

  public boolean addCustomer(Customer aCustomer)
  {
    boolean wasAdded = false;
    if (customers.contains(aCustomer)) { return false; }
    SportCenter existingSportCenter = aCustomer.getSportCenter();
    boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
    if (isNewSportCenter)
    {
      aCustomer.setSportCenter(this);
    }
    else
    {
      customers.add(aCustomer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomer(Customer aCustomer)
  {
    boolean wasRemoved = false;
    //Unable to remove aCustomer, as it must always have a sportCenter
    if (!this.equals(aCustomer.getSportCenter()))
    {
      customers.remove(aCustomer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  public boolean addCustomerAt(Customer aCustomer, int index)
  {
    boolean wasAdded = false;
    if(addCustomer(aCustomer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomers()) { index = numberOfCustomers() - 1; }
      customers.remove(aCustomer);
      customers.add(index, aCustomer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerAt(Customer aCustomer, int index)
  {
    boolean wasAdded = false;
    if(customers.contains(aCustomer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomers()) { index = numberOfCustomers() - 1; }
      customers.remove(aCustomer);
      customers.add(index, aCustomer);
      wasAdded = true;
    }
    else
    {
      wasAdded = addCustomerAt(aCustomer, index);
    }
    return wasAdded;
  }
  public static int minimumNumberOfCourses()
  {
    return 0;
  }
  public Course addCourse(String aId, String aName, String aDescription, Course.Level aLevel, Date aCourseStartDate, Date aCourseEndDate, Room aRoom, Instructor aInstructor)
  {
    return new Course(aId, aName, aDescription, aLevel, aCourseStartDate, aCourseEndDate, aRoom, this, aInstructor);
  }

  public boolean addCourse(Course aCourse)
  {
    boolean wasAdded = false;
    if (courses.contains(aCourse)) { return false; }
    SportCenter existingSportCenter = aCourse.getSportCenter();
    boolean isNewSportCenter = existingSportCenter != null && !this.equals(existingSportCenter);
    if (isNewSportCenter)
    {
      aCourse.setSportCenter(this);
    }
    else
    {
      courses.add(aCourse);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCourse(Course aCourse)
  {
    boolean wasRemoved = false;
    if (!this.equals(aCourse.getSportCenter()))
    {
      courses.remove(aCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  public boolean addCourseAt(Course aCourse, int index)
  {
    boolean wasAdded = false;
    if(addCourse(aCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCourses()) { index = numberOfCourses() - 1; }
      courses.remove(aCourse);
      courses.add(index, aCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCourseAt(Course aCourse, int index)
  {
    boolean wasAdded = false;
    if(courses.contains(aCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCourses()) { index = numberOfCourses() - 1; }
      courses.remove(aCourse);
      courses.add(index, aCourse);
      wasAdded = true;
    }
    else
    {
      wasAdded = addCourseAt(aCourse, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (rooms.size() > 0)
    {
      Room aRoom = rooms.get(rooms.size() - 1);
      aRoom.delete();
      rooms.remove(aRoom);
    }

    Owner existingOwner = owner;
    owner = null;
    if (existingOwner != null)
    {
      existingOwner.delete();
    }
    while (instructors.size() > 0)
    {
      Instructor aInstructor = instructors.get(instructors.size() - 1);
      aInstructor.delete();
      instructors.remove(aInstructor);
    }

    while (customers.size() > 0)
    {
      Customer aCustomer = customers.get(customers.size() - 1);
      aCustomer.delete();
      customers.remove(aCustomer);
    }

    while (courses.size() > 0)
    {
      Course aCourse = courses.get(courses.size() - 1);
      aCourse.delete();
      courses.remove(aCourse);
    }

  }


  public String toString()
  {
    return super.toString() + "["+
            "openingHour" + ":" + getOpeningHour()+ "," +
            "closingHour" + ":" + getClosingHour()+ "," +
            "address" + ":" + getAddress()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "owner = "+(getOwner()!=null?Integer.toHexString(System.identityHashCode(getOwner())):"null");
  }
}
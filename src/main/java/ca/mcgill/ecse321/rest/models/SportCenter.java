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
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;
  private String openingHour;
  private String closingHour;
  private String address;
  private String name;
  @OneToOne
  private Schedule schedule;


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
  public String getName()
  {
    return name;
  }

    public void setName(String aName)
    {
        name = aName;
    }

  public static int minimumNumberOfRooms()
  {
    return 0;
  }
  public Room addRoom(String aId, String aRoomName)
  {
    return new Room(aId, aRoomName, this);
  }



  public static int minimumNumberOfInstructors()
  {
    return 0;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */

  public static int minimumNumberOfCourses()
  {
    return 0;
  }

  public Schedule getSchedule()
  {
    return schedule;
  }

    public void setSchedule(Schedule aSchedule)
    {
        schedule = aSchedule;
    }


  public String toString()
  {
    return super.toString() + "["+
            "openingHour" + ":" + getOpeningHour()+ "," +
            "closingHour" + ":" + getClosingHour()+ "," +
            "address" + ":" + getAddress()+ "]" + System.getProperties().getProperty("line.separator");
  }
}
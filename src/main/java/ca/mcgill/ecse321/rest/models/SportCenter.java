package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class SportCenter {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;

  private String address;
  private String name;
  @OneToOne private Schedule schedule;

  public SportCenter() {}
  public SportCenter(String id) {
    this.id=id;
  }

  public static int minimumNumberOfRooms() {
    return 0;
  }

  public static int minimumNumberOfInstructors() {
    return 0;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomers() {
    return 0;
  }

  public static int minimumNumberOfCourses() {
    return 0;
  }

  public boolean setAddress(String aAddress) {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public String getAddress() {
    return address;
  }

  /* Code from template association_GetMany */
  public String getName() {
    return name;
  }

  public void setName(String aName) {
    name = aName;
  }

  /* Code from template association_AddManyToOne */

  public Room addRoom(String aId, String aRoomName) {
    return new Room(aId, aRoomName, this);
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule aSchedule) {
    schedule = aSchedule;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  public String toString() {
    return super.toString()
        + "["
        + "address"
        + ":"
        + getAddress()
        + "]"
        + System.getProperties().getProperty("line.separator");
  }


}

package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Room {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;

  private String roomName;

  @ManyToOne private SportCenter sportCenter;

  public Room(String aId, String aRoomName, SportCenter aSportCenter) {
    id = aId;
    roomName = aRoomName;
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter) {
      throw new RuntimeException(
          "Unable to create room due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Room() {}

  public boolean setId(String aId) {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomName(String aRoomName) {
    boolean wasSet = false;
    roomName = aRoomName;
    wasSet = true;
    return wasSet;
  }

  public String getId() {
    return id;
  }

  public String getRoomName() {
    return roomName;
  }

  /* Code from template association_GetOne */
  public SportCenter getSportCenter() {
    return sportCenter;
  }

  /* Code from template association_SetOneToMany */
  public boolean setSportCenter(SportCenter aSportCenter) {
    this.sportCenter = aSportCenter;
    return true;
  }

  public String toString() {
    return super.toString()
        + "["
        + "id"
        + ":"
        + getId()
        + ","
        + "roomName"
        + ":"
        + getRoomName()
        + "]"
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "sportCenter = "
        + (getSportCenter() != null
            ? Integer.toHexString(System.identityHashCode(getSportCenter()))
            : "null");
  }
}

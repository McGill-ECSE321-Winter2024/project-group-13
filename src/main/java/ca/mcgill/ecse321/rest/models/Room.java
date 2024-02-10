/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

package ca.mcgill.ecse321.rest.models;

// line 76 "../../../../../DomainModel.ump"

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Room
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;
  private String roomName;

  //Room Associations
  @ManyToOne
  private SportCenter sportCenter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(String aId, String aRoomName, SportCenter aSportCenter)
  {
    id = aId;
    roomName = aRoomName;
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter)
    {
      throw new RuntimeException("Unable to create room due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Room() {

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

  public boolean setRoomName(String aRoomName)
  {
    boolean wasSet = false;
    roomName = aRoomName;
    wasSet = true;
    return wasSet;
  }

  public String getId()
  {
    return id;
  }

  public String getRoomName()
  {
    return roomName;
  }
  /* Code from template association_GetOne */
  public SportCenter getSportCenter()
  {
    return sportCenter;
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
      existingSportCenter.removeRoom(this);
    }
    sportCenter.addRoom(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    SportCenter placeholderSportCenter = sportCenter;
    this.sportCenter = null;
    if(placeholderSportCenter != null)
    {
      placeholderSportCenter.removeRoom(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "roomName" + ":" + getRoomName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "sportCenter = "+(getSportCenter()!=null?Integer.toHexString(System.identityHashCode(getSportCenter())):"null");
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

package ca.mcgill.ecse321.rest.models;

// line 75 "../../../../../../model.ump"
// line 144 "../../../../../../model.ump"
public class Room
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  private String id;
  private String roomName;

  //Room Associations
  private SportCenter sportCenter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(String aId, String aRoomName, SportCenter aSportCenter)
  {
    id = aId;
    roomName = aRoomName;
    if (aSportCenter == null || aSportCenter.getRoom() != null)
    {
      throw new RuntimeException("Unable to create Room due to aSportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    sportCenter = aSportCenter;
  }

  public Room(String aId, String aRoomName, String aOpeningHourForSportCenter, String aClosingHourForSportCenter, String aAddressForSportCenter, Owner aOwnerForSportCenter)
  {
    id = aId;
    roomName = aRoomName;
    sportCenter = new SportCenter(aOpeningHourForSportCenter, aClosingHourForSportCenter, aAddressForSportCenter, this, aOwnerForSportCenter);
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

  public void delete()
  {
    SportCenter existingSportCenter = sportCenter;
    sportCenter = null;
    if (existingSportCenter != null)
    {
      existingSportCenter.delete();
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
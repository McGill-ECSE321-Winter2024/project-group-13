/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

package ca.mcgill.ecse321.rest.models;
import java.util.*;

// line 23 "../../../../../../model.ump"
// line 113 "../../../../../../model.ump"
public class Owner extends Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Owner Associations
  private SportCenter sportCenter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Owner(String aId, String aEmail, String aPassword, String aName, SportCenter aSportCenter)
  {
    super(aId, aEmail, aPassword, aName);
    if (aSportCenter == null || aSportCenter.getOwner() != null)
    {
      throw new RuntimeException("Unable to create Owner due to aSportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    sportCenter = aSportCenter;
  }

  public Owner(String aId, String aEmail, String aPassword, String aName, String aOpeningHourForSportCenter, String aClosingHourForSportCenter, String aAddressForSportCenter, Room aRoomForSportCenter)
  {
    super(aId, aEmail, aPassword, aName);
    sportCenter = new SportCenter(aOpeningHourForSportCenter, aClosingHourForSportCenter, aAddressForSportCenter, aRoomForSportCenter, this);
  }

  //------------------------
  // INTERFACE
  //------------------------
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
    super.delete();
  }

}
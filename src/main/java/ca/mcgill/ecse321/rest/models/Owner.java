/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.*;

// line 23 "../../../../../DomainModel.ump"
@Entity
public class Owner extends Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Owner Associations
  @OneToOne(cascade = CascadeType.ALL)
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

  public Owner(String aId, String aEmail, String aPassword, String aName, String aOpeningHourForSportCenter, String aClosingHourForSportCenter, String aAddressForSportCenter)
  {
    super(aId, aEmail, aPassword, aName);
    sportCenter = new SportCenter(aOpeningHourForSportCenter, aClosingHourForSportCenter, aAddressForSportCenter, this);
  }

  public Owner() {

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
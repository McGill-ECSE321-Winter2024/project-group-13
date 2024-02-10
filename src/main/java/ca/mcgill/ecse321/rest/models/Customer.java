/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.*;

// line 31 "../../../../../DomainModel.ump"
@Entity
public class Customer extends Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Associations
  @ManyToOne
  private SportCenter sportCenter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(String aId, String aEmail, String aPassword, String aName, SportCenter aSportCenter)
  {
    super(aId, aEmail, aPassword, aName);
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter)
    {
      throw new RuntimeException("Unable to create customer due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Customer() {

  }

  //------------------------
  // INTERFACE
  //------------------------
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
      existingSportCenter.removeCustomer(this);
    }
    sportCenter.addCustomer(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    SportCenter placeholderSportCenter = sportCenter;
    this.sportCenter = null;
    if(placeholderSportCenter != null)
    {
      placeholderSportCenter.removeCustomer(this);
    }
    super.delete();
  }

}
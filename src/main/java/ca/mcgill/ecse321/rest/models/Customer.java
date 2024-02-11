
package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity
public class Customer extends Person
{

  @ManyToOne
  private SportCenter sportCenter;

  public Customer(String aId, String aEmail, String aPhoneNumber, String aPassword, String aName, SportCenter aSportCenter)
  {
    super(aId, aEmail, aPhoneNumber, aPassword, aName);
    boolean didAddSportCenter = setSportCenter(aSportCenter);
    if (!didAddSportCenter)
    {
      throw new RuntimeException("Unable to create customer due to sportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }
  public Customer() {

  }
  public SportCenter getSportCenter()
  {
    return sportCenter;
  }
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
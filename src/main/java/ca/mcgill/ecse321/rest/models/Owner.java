
package ca.mcgill.ecse321.rest.models;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;


@Entity
public class Owner extends Person
{

  @OneToOne(cascade = CascadeType.ALL)
  private SportCenter sportCenter;

  public Owner() {

  }

  public Owner(String aId, String aEmail, String aPhoneNumber, String aPassword, String aName, SportCenter aSportCenter)
  {
    super(aId, aEmail, aPhoneNumber, aPassword, aName);
    if (aSportCenter == null || aSportCenter.getOwner() != null)
    {
      throw new RuntimeException("Unable to create Owner due to aSportCenter. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    sportCenter = aSportCenter;
  }

  public Owner(String aId, String aEmail, String aPhoneNumber, String aPassword, String aName, String aOpeningHourForSportCenter, String aClosingHourForSportCenter, String aAddressForSportCenter)
  {
    super(aId, aEmail, aPhoneNumber, aPassword, aName);
    sportCenter = new SportCenter(aOpeningHourForSportCenter, aClosingHourForSportCenter, aAddressForSportCenter, this);
  }

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
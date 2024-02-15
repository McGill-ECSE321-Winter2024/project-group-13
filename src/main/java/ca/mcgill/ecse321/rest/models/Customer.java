
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
    this.sportCenter = aSportCenter;
    return true;
  }

}
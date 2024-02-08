/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

package ca.mcgill.ecse321.rest.models;

// line 81 "../../../../../../model.ump"
// line 149 "../../../../../../model.ump"
public class Invoice
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Status { Open, Failed, Void, Completed, Cancelled }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Invoice Attributes
  private String id;
  private Status status;

  //Invoice Associations
  private Registration registrations;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Invoice(String aId, Status aStatus, Registration aRegistrations)
  {
    id = aId;
    status = aStatus;
    if (!setRegistrations(aRegistrations))
    {
      throw new RuntimeException("Unable to create Invoice due to aRegistrations. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  public boolean setStatus(Status aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public String getId()
  {
    return id;
  }

  public Status getStatus()
  {
    return status;
  }
  /* Code from template association_GetOne */
  public Registration getRegistrations()
  {
    return registrations;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setRegistrations(Registration aNewRegistrations)
  {
    boolean wasSet = false;
    if (aNewRegistrations != null)
    {
      registrations = aNewRegistrations;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    registrations = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "registrations = "+(getRegistrations()!=null?Integer.toHexString(System.identityHashCode(getRegistrations())):"null");
  }
}
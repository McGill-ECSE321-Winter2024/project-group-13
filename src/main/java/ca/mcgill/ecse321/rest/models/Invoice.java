
package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Invoice
{
  public Invoice() {

  }

  public enum Status { Open, Failed, Void, Completed, Cancelled }

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;
  @Enumerated(EnumType.STRING)
  private Status status;

  @ManyToOne
  private Registration registrations;

  private int amount;


  public Invoice(String aId, Status aStatus, Registration aRegistrations, int aAmount)
  {
    if (aStatus ==null){
      throw new RuntimeException("Set valid Status.");

    }
    if (!setAmount(aAmount))
    {
      throw new RuntimeException("Unable to create Invoice due to aAmount. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }

    id = aId;
    status = aStatus;
    amount = aAmount;
    if (!setRegistrations(aRegistrations))
    {
      throw new RuntimeException("Unable to create Invoice due to aRegistrations. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }


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
    if (aStatus != null) {
      status = aStatus;
      wasSet = true;
    }
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
  public Registration getRegistrations()
  {
    return registrations;
  }
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

  public int getAmount()
  {
    return amount;
  }

  public boolean setAmount(int aAmount)
  {
    boolean wasSet = false;
    if (aAmount >= 0)
    {
      amount = aAmount;
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
package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Invoice {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false, unique = true)
  private String id;

  @Enumerated(EnumType.STRING)
  private Status status;

  @ManyToOne private Registration registration;
  private double amount;

  public Invoice() {}

  public Invoice(Status aStatus, Registration aRegistration, int aAmount) {
    if (aStatus == null) {
      throw new RuntimeException("Set valid Status.");
    }
    if (!setAmount(aAmount)) {
      throw new RuntimeException(
          "Unable to create Invoice due to aAmount. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }

    status = aStatus;
    amount = aAmount;
    if (!setRegistration(aRegistration)) {
      throw new RuntimeException(
          "Unable to create Invoice due to aRegistration. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public boolean setStatus(Status aStatus) {
    boolean wasSet = false;
    if (aStatus != null) {
      status = aStatus;
      wasSet = true;
    }
    return wasSet;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Status getStatus() {
    return status;
  }

  public Registration getRegistration() {
    return registration;
  }

  public boolean setRegistration(Registration aNewRegistration) {
    boolean wasSet = false;
    if (aNewRegistration != null) {

      registration = aNewRegistration;

      wasSet = true;
    }
    return wasSet;
  }

  public double getAmount() {
    return amount;
  }

  public boolean setAmount(double aAmount) {
    boolean wasSet = false;
    if (aAmount >= 0) {
      amount = aAmount;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    registration = null;
  }

  public String toString() {
    return super.toString()
        + "["
        + "id"
        + ":"
        + getId()
        + "]"
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "status"
        + "="
        + (getStatus() != null
            ? !getStatus().equals(this) ? getStatus().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator")
        + "  "
        + "registration = "
        + (getRegistration() != null
            ? Integer.toHexString(System.identityHashCode(getRegistration()))
            : "null");
  }

  public enum Status {
    Open,
    Failed,
    Void,
    Completed,
    Cancelled
  }
}

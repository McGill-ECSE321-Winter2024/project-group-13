package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Customer extends Person {

  @ManyToOne private SportCenter sportCenter;

  public Customer() {}

  public SportCenter getSportCenter() {
    return sportCenter;
  }

  public boolean setSportCenter(SportCenter aSportCenter) {
    this.sportCenter = aSportCenter;
    return true;
  }
}

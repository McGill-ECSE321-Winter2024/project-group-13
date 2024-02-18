package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Owner extends Person {

  @OneToOne(cascade = CascadeType.ALL)
  private SportCenter sportCenter;

  public Owner() {}

  public SportCenter getSportCenter() {
    return sportCenter;
  }

  public boolean setSportCenter(SportCenter aSportCenter) {
    this.sportCenter = aSportCenter;
    return true;
  }
}

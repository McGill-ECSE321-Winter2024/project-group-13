package ca.mcgill.ecse321.rest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Instructor extends Person {
  @ManyToOne private SportCenter sportCenter;

  public Instructor() {
    super();
  }



  public SportCenter getSportCenter() {
    return sportCenter;
  }

  public boolean setSportCenter(SportCenter aSportCenter) {
    this.sportCenter = aSportCenter;
    return true;
  }
}

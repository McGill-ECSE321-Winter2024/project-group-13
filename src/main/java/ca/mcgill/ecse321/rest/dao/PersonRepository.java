package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {

  Person findPersonByEmail(String email);

  Person findPersonByName(String name);
}

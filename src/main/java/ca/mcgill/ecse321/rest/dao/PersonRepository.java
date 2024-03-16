package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {
  Person findPersonById(String id);
  Person findPersonByName(String name);
  Person findPersonByEmail(String email);
  Person findPersonByPhoneNumber(String phoneNumber);
  Person findPersonByEmailAndPassword(String username, String password);
}

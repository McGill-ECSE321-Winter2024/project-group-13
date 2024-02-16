package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Person;
import ca.mcgill.ecse321.rest.models.Owner;

import org.springframework.data.repository.CrudRepository;
public interface OwnerRepository extends CrudRepository<Owner, String> {

    Owner findOwnerById(String id);
    Owner findOwnerByName(String name);
    Owner findOwnerByEmail(String email);

}

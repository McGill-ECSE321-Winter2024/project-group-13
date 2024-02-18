package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Owner;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<Owner, String> {

  Owner findOwnerById(String id);

  Owner findOwnerByEmail(String email);

  Owner findOwnerByName(String name);

  Owner findOwnerByPhoneNumber(String phoneNumber);

  @Transactional
  void deleteOwnerById(String id);

  long count();
}

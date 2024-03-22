package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Instructor;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface InstructorRepository extends CrudRepository<Instructor, String> {
  Instructor findInstructorById(String id);

  Instructor findInstructorByEmail(String email);

  Instructor findInstructorByPhoneNumber(String phoneNumber);

  Instructor findInstructorByName(String name);

  @Transactional
  void deleteInstructorById(String id);

  @Transactional
  void deleteInstructorByEmail(String email);
}

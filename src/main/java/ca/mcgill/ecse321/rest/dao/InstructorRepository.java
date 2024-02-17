package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Person;
import ca.mcgill.ecse321.rest.models.Instructor;

import org.springframework.data.repository.CrudRepository;
public interface InstructorRepository extends CrudRepository<Instructor, String> {
    Instructor findInstructorById(String id);
    Instructor findInstructorByEmail(String email);
    Instructor findInstructorByPhoneNumber(String phoneNumber);


    void deleteInstructorById(String id);
    void deleteInstructorByEmail(String email);
}

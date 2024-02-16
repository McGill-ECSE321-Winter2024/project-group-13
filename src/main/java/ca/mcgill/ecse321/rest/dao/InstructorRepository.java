package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Person;
import ca.mcgill.ecse321.rest.models.Instructor;

import org.springframework.data.repository.CrudRepository;
public interface InstructorRepository extends CrudRepository<Instructor, String> {
    Instructor findInstructorById(String id);
    Instructor findInstructorByName(String name);
    Instructor findInstructorByEmail(String email);


    void deleteInstructorById(String id);
    void deleteInstructorByName(String name);
    void deleteInstructorByEmail(String email);
}

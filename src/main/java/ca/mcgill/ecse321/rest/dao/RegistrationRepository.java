package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Registration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistrationRepository extends CrudRepository<Registration, String> {

  Registration findRegistrationById(String registrationId);

  Registration findRegistrationByIdAndCustomerId(String registrationId, String customerId);

  Registration findRegistrationByCourseAndCustomer(Course course, Customer customer);

  List<Registration> findRegistrationByCourseId(String courseId);

}

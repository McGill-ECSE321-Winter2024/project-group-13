package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
  Customer findCustomerById(String id);

  Customer findCustomerByEmail(String email);

  Customer findCustomerByPhoneNumber(String phoneNumber);
  @Transactional
  void deleteCustomerById(String id);

  @Transactional
  void deleteCustomerByEmail(String email);
}

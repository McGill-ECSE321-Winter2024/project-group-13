package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Person;
import ca.mcgill.ecse321.rest.models.Customer;

import org.springframework.data.repository.CrudRepository;


public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer findCustomerById(String id);
    Customer findCustomersByName(String name);
    Customer findCustomerByEmail(String email);

    void deleteCustomerById(String id);
    void deleteCustomerByName(String name);
    void deleteCustomerByEmail(String email);
}

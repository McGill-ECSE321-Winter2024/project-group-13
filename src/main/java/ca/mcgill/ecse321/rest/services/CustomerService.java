package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.CustomerRepository;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
import ca.mcgill.ecse321.rest.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDTO> findAll(PersonSession personSession) {
        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)
                || personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            List<CustomerDTO> customersDTOs = new ArrayList<>() ;
            for (Customer customer : customerRepository.findAll()) {
                customersDTOs.add(new CustomerDTO(customer));
            }
            return customersDTOs;
        } else {
            return null;
        }

    }

}


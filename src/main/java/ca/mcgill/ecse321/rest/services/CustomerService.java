package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.CustomerRepository;
import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
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

//    @Autowired
//    private SportCenterRepository sportCenterRepository;

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

//    public CustomerDTO save(CustomerDTO customerDTO) {
//        Customer customer = convertToEntity(customerDTO);
//        Customer savedCustomer = customerRepository.save(customer);
//        return convertToDto(savedCustomer);
//    }

//    private Customer convertToEntity(CustomerDTO customerDTO) {
//        Customer customer = new Customer();
//        customer.setName(customerDTO.getName());
//        customer.setPhoneNumber(customerDTO.getPhoneNumber());
//        customer.setEmail(customerDTO.getEmail());
//        customer.setSportCenter(sportCenterRepository.findSportCenterById(customerDTO.getSportCenterId()));
//        return customer;
//    }
//
//    private CustomerDTO convertToDto(Customer customer) {
//        // Conversion logic here
//        return new CustomerDTO(customer);
//    }
}


package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Customer;

public class CustomerDTO extends PersonDTO {

    // Constructor accepting a Customer entity
    public CustomerDTO(Customer customer) {
        super(customer);
    }
}


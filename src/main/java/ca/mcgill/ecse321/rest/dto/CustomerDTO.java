package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Customer;

public class CustomerDTO extends PersonDTO{

    private String sportCenterId;
    public CustomerDTO(Customer customer) {
        super(customer);
        this.sportCenterId = customer.getSportCenter().getId();
    }
    public String getSportCenterId() {
        return sportCenterId;
    }

}

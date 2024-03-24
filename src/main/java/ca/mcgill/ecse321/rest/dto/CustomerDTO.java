package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Customer;

public class CustomerDTO extends PersonDTO{

    private String sportCenterId;

    public CustomerDTO(){}
    public CustomerDTO(Customer customer) {
        super(customer);
        this.sportCenterId = customer.getSportCenter().getId();
    }
    public String getSportCenterId() {
        return sportCenterId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CustomerDTO customerDTO = (CustomerDTO) obj;
        return super.equals(obj) &&(sportCenterId.equals(customerDTO.sportCenterId)||sportCenterId==customerDTO.sportCenterId);
    }
}


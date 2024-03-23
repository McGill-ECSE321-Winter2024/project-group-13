package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Customer;

public class CustomerDTO extends PersonDTO{

    private String sportCenter;
    private String id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;

    // Constructor accepting a Customer entity
    public CustomerDTO(Customer customer) {
        super(customer);
        if (customer.getId() != null){
        this.id = customer.getId();}
            if (customer.getSportCenter().getId()!= null){
        this.sportCenter = customer.getSportCenter().getId();}
                if (customer.getName() != null){
        this.name = customer.getName();}
                    if (customer.getPassword()!= null){
        this. password = customer.getPassword();}
                        if (customer.getEmail() != null){
        this.email = customer.getEmail();}
                            if (customer.getPhoneNumber() != null){
        this.phoneNumber = customer.getPhoneNumber();}

    }

    public String getName(){return this.name;}
    public String getID(){return this.id;}
    public String getSportCenter(){return this.sportCenter;}
    public String getEmail(){return this.email;}
    public String getPhoneNumber(){return this.phoneNumber;}
    public String getPassword(){return this.password;}
}


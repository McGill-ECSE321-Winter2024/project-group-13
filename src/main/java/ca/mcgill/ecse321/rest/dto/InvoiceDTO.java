package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Invoice;

public class InvoiceDTO {

    private RegistrationDTO registrationDTO;

    private double amount;

    private String id;
    public InvoiceDTO(){};

    public InvoiceDTO(Invoice invoice){
       // this.registrationDTO = registrationDTO;
        this.id = invoice.getId();
        this.amount = invoice.getAmount();
    }

    public RegistrationDTO getRegistrationDTO(){return registrationDTO;}

    public void setRegistrationDTO(RegistrationDTO registrationDTO){this.registrationDTO = registrationDTO;}
    public void setAmount(int amount){this.amount= amount;}
    public double getAmount(){return this.amount;}
    public void setid(String id){this.id= id;}
    public String getid(){return this.id;}

}

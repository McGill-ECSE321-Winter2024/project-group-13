package ca.mcgill.ecse321.rest.dto;

public class InvoiceDTO {

    private RegistrationDTO registrationDTO;

    public InvoiceDTO(){};

    public InvoiceDTO(RegistrationDTO registrationDTO){
        this.registrationDTO = registrationDTO;
    }

    public RegistrationDTO getRegistrationDTO(){return registrationDTO;}

    public void setRegistrationDTO(RegistrationDTO registrationDTO){this.registrationDTO = registrationDTO;}

}

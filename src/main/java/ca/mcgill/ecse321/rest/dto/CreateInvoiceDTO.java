package ca.mcgill.ecse321.rest.dto;

public class CreateInvoiceDTO {
    private double amount;
    private String registrationId;

    public CreateInvoiceDTO() {
    }

    public CreateInvoiceDTO(double amount, String registrationId) {
        this.amount = amount;
        this.registrationId = registrationId;
    }

    public double getAmount() {
        return amount;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }


}

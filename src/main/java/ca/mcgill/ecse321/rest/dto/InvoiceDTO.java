package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Invoice;

public class InvoiceDTO {

    private String id;
    private String registrationId;

    private String customerId;

    private String status;

    private Number amount;

    public InvoiceDTO(){};

    public InvoiceDTO(Invoice invoice){
        this.setId(invoice.getId());
        this.registrationId = invoice.getRegistration().getId();
        this.customerId = invoice.getRegistration().getCustomer().getId();
        this.status = invoice.getStatus().toString();
        this.amount = invoice.getAmount();
    }

    public String getId() {
        return this.id;
    }

    public String getRegistrationId() {
        return this.registrationId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public String getStatus() {
        return this.status;
    }

    public Number getAmount() {
        return this.amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmount(Number amount) {
        this.amount = amount;
    }


}

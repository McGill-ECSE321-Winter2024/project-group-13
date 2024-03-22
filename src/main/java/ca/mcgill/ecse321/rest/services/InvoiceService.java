package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.InvoiceRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.models.Invoice;
import ca.mcgill.ecse321.rest.models.Registration;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    public List<InvoiceDTO> getAllInvoices(PersonSession personSession){
        List<InvoiceDTO> invoices = new ArrayList<>();
        if(personSession.getPersonType().equals(PersonSession.PersonType.Customer)) {
            for(Invoice invoice : invoiceRepository.findInvoicesByRegistration_Customer_Id(personSession.getPersonId())) {
                invoices.add(new InvoiceDTO(invoice));
            }
        }
        else if(personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            for(Invoice invoice : invoiceRepository.findAll()) {
                invoices.add(new InvoiceDTO(invoice));
            }
        }
        return invoices;
    }

    public InvoiceDTO getInvoice(PersonSession personSession, String invoiceId) {
        if(PersonSession.PersonType.Instructor.equals(personSession.getPersonType())) {
            return null;
        }
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        if(invoice == null) {
            return null;
        }
        if(personSession.getPersonType().equals(PersonSession.PersonType.Customer) && invoice.getRegistration().getCustomer().getId().equals(personSession.getPersonId())) {
            return new InvoiceDTO(invoice);
        }
        else if(personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return new InvoiceDTO(invoice);
        }
        return null;
    }

    public String generateCheckoutSession(PersonSession personSession, String invoiceId) throws StripeException {

        return "";
    }

    public InvoiceDTO createInvoice(PersonSession personSession, String registrationId, double amount){
        if(PersonSession.PersonType.Instructor.equals(personSession.getPersonType()) || PersonSession.PersonType.Customer.equals(personSession.getPersonType())) {
            return null;
        }
        Registration registration = registrationRepository.findRegistrationById(registrationId);
        if(registration == null) return null;
        Invoice invoice = new Invoice();
        invoice.setRegistration(registration);
        invoice.setAmount(amount);
        invoice.setStatus(Invoice.Status.Open);
        invoiceRepository.save(invoice);
        return new InvoiceDTO(invoice);
    }

    public boolean updateInvoiceStatus(PersonSession personSession, String invoiceId, String status) {
        Invoice invoice = getOpenInvoice(personSession, invoiceId);
        if(invoice == null) {
            return false;
        }
        invoice.setStatus(Invoice.Status.valueOf(status));
        invoiceRepository.save(invoice);
        return true;
    }

    public boolean updateInvoiceAmount(PersonSession personSession, String invoiceId, double amount) {
        Invoice invoice = getOpenInvoice(personSession, invoiceId);
        if(invoice == null) {
            return false;
        }
        invoice.setAmount(amount);
        invoiceRepository.save(invoice);
        return true;
    }

    private Invoice getOpenInvoice(PersonSession personSession, String invoiceId) {
        if(
            PersonSession.PersonType.Instructor.equals(personSession.getPersonType())
            || PersonSession.PersonType.Customer.equals(personSession.getPersonType())
        ) {
            return null;
        }
        return invoiceRepository.findInvoiceById(invoiceId);
    }

}

package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.InvoiceRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.models.Invoice;
import ca.mcgill.ecse321.rest.models.Person;
import ca.mcgill.ecse321.rest.models.Registration;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
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

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PersonRepository personRepository;

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

        Stripe.apiKey = "sk_test_51OwsFP02hfuuBSxWj16DChFROwjVHzfurPVWh5cBvi3acfDCQ13KNAeOiqoDiQCqsRtlKdiN9bbjFGvLEZCSspth00BnFTL6Hu";
        String YOUR_DOMAIN = "http://localhost:8080";

        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        if(invoice == null) {
            throw new IllegalArgumentException("Invoice not found");
        }

        Long amount = (long) (invoice.getAmount() * 100);

        PriceCreateParams priceParams =
                PriceCreateParams.builder()
                        .setCurrency("cad")
                        .setUnitAmount(amount)
                        .setProductData(
                                PriceCreateParams.ProductData.builder().setName(invoice.getRegistration().getCourse().getName() + " | Sport Center Invoice").build()
                        )
                        .build();

        Price price = Price.create(priceParams);

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(YOUR_DOMAIN + "/invoices/payment/success/" + invoiceId + "?personId=" + personSession.getPersonId())
                        .setCancelUrl("http://localhost:3000" + "/invoices/")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                                        .setPrice(price.getId())
                                        .build())
                        .build();
        Session session = Session.create(params);

        return session.getUrl();
    }

    public InvoiceDTO createInvoice(PersonSession personSession, String registrationId, double amount){
        if(PersonSession.PersonType.Instructor.equals(personSession.getPersonType()) || PersonSession.PersonType.Customer.equals(personSession.getPersonType())) {
            throw new IllegalArgumentException("Only owners can create invoices");
        }
        Registration registration = registrationRepository.findRegistrationById(registrationId);
        if(registration == null) {
            throw new IllegalArgumentException("Registration not found");
        }
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
        Person person = personRepository.findPersonById(invoice.getRegistration().getCustomer().getId());
        // make sure it has double digits in cents xxx.xx
        String amount = String.format("%.2f", invoice.getAmount());
        TwilioService.sendSms(person.getPhoneNumber(), "Hello " + person.getName() + ", your invoice of amount $" + amount + " has been paid successfully");
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
        ) {
            return null;
        }
        return invoiceRepository.findInvoiceById(invoiceId);
    }

}

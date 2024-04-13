package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.InvoiceRepository;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private RegistrationRepository registrationRepository;
    @InjectMocks
    private RegistrationService registrationService;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void getInvoices_valid_customer() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "customer_id",
                PersonSession.PersonType.Customer,
                sportCenter.getId()
        );
        List<Invoice> invoices = new ArrayList<>();
        Invoice invoice = new Invoice();
        invoice.setRegistration(new Registration());
        invoice.getRegistration().setCustomer(customer);
        invoice.setStatus(Invoice.Status.Completed);
        invoices.add(invoice);
        when(invoiceRepository.findInvoicesByRegistration_Customer_Id(anyString())).thenReturn(invoices);
        List<InvoiceDTO> invoiceDTOS = invoiceService.getAllInvoices(personSession);

        assertEquals(1, invoiceDTOS.size());

        verify(invoiceRepository, times(1)).findInvoicesByRegistration_Customer_Id(anyString());
    }


    @Test
    public void getInvoices_valid_owner() {
        SportCenter sportCenter = new SportCenter();
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        List<Invoice> invoices = new ArrayList<>();
        Invoice invoice = new Invoice();
        invoice.setRegistration(new Registration());
        invoice.getRegistration().setCustomer(new Customer());
        invoice.setStatus(Invoice.Status.Completed);
        invoices.add(invoice);
        when(invoiceRepository.findAll()).thenReturn(invoices);
        List<InvoiceDTO> invoiceDTOS = invoiceService.getAllInvoices(personSession);

        assertEquals(1, invoiceDTOS.size());

        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    public void getInvoices_instructor() {
        SportCenter sportCenter = new SportCenter();
        PersonSession personSession = new PersonSession(
                "instructor_id",
                PersonSession.PersonType.Instructor,
                sportCenter.getId()
        );
        List<InvoiceDTO> invoiceDTOS = invoiceService.getAllInvoices(personSession);

        assertEquals(0, invoiceDTOS.size());
        verify(invoiceRepository, times(0)).findAll();
    }

    @Test
    public void getSpecificInvoiceAsCustomer() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "customer_id",
                PersonSession.PersonType.Customer,
                sportCenter.getId()
        );
        Invoice invoice = new Invoice();
        invoice.setId("invoice_id");
        invoice.setRegistration(new Registration());
        invoice.getRegistration().setCustomer(customer);
        invoice.setStatus(Invoice.Status.Completed);
        when(invoiceRepository.findInvoiceById(anyString())).thenReturn(invoice);
        InvoiceDTO invoiceDTO = invoiceService.getInvoice(personSession, "invoice_id");

        assertNotNull(invoiceDTO);
        assertEquals("invoice_id", invoiceDTO.getId());
        verify(invoiceRepository, times(1)).findInvoiceById(anyString());
    }

    @Test
    public void getSpecificInvoiceAsOwner() {
        SportCenter sportCenter = new SportCenter();
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        Invoice invoice = new Invoice();
        invoice.setId("invoice_id");
        invoice.setRegistration(new Registration());
        invoice.getRegistration().setCustomer(new Customer());
        invoice.setStatus(Invoice.Status.Completed);
        when(invoiceRepository.findInvoiceById(anyString())).thenReturn(invoice);
        InvoiceDTO invoiceDTO = invoiceService.getInvoice(personSession, "invoice_id");

        assertNotNull(invoiceDTO);
        assertEquals("invoice_id", invoiceDTO.getId());
        verify(invoiceRepository, times(1)).findInvoiceById(anyString());
    }

    @Test
    public void getSpecificInvoiceAsInstructor() {
        SportCenter sportCenter = new SportCenter();
        PersonSession personSession = new PersonSession(
                "instructor_id",
                PersonSession.PersonType.Instructor,
                sportCenter.getId()
        );
        InvoiceDTO invoiceDTO = invoiceService.getInvoice(personSession, "invoice_id");
        assertNull(invoiceDTO);
        verify(invoiceRepository, times(0)).findInvoiceById(anyString());
    }

    @Test
    public void createInvoice_valid() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        Registration registration = new Registration();
        registration.setCustomer(customer);
        registration.setId("registration_id");
        when(registrationRepository.findRegistrationById(anyString())).thenReturn(registration);
        InvoiceDTO invoiceDTO = invoiceService.createInvoice(personSession, "registration_id", 100.0);

        assertNotNull(invoiceDTO);
        assertEquals("registration_id", invoiceDTO.getRegistrationId());
        assertEquals(100.0, invoiceDTO.getAmount());
        assertEquals("customer_id", invoiceDTO.getCustomerId());
        assertEquals(Invoice.Status.Open.toString(), invoiceDTO.getStatus());
        verify(registrationRepository, times(1)).findRegistrationById(anyString());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void createInvoice_invalid_registration_does_not_exist() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        when(registrationRepository.findRegistrationById(anyString())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(personSession, "registration_id", 100.0);
        });

        verify(registrationRepository, times(1)).findRegistrationById(anyString());
        verify(invoiceRepository, times(0)).save(any(Invoice.class));

        //expect error to be thrown
    }

    @Test
    public void updateInvoiceAmount_valid() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        Invoice invoice = new Invoice();
        invoice.setId("invoice_id");
        invoice.setRegistration(new Registration());
        invoice.getRegistration().setCustomer(customer);
        invoice.setStatus(Invoice.Status.Open);
        when(invoiceRepository.findInvoiceById(anyString())).thenReturn(invoice);
        boolean updatedInvoice = invoiceService.updateInvoiceAmount(personSession, "invoice_id", 100.0);

        assertTrue(updatedInvoice);
        verify(invoiceRepository, times(1)).findInvoiceById(anyString());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void updateInvoiceAmount_invalid_invoice_does_not_exist() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "customer_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        when(invoiceRepository.findInvoiceById(anyString())).thenReturn(null);
        boolean updatedInvoice = invoiceService.updateInvoiceAmount(personSession, "invoice_id", 100.0);

        assertFalse(updatedInvoice);
        verify(invoiceRepository, times(1)).findInvoiceById(anyString());
        verify(invoiceRepository, times(0)).save(any(Invoice.class));
    }

    @Test
    public void updateInvoiceAmount_invalid_invoice_not_open() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        Invoice invoice = new Invoice();
        invoice.setId("invoice_id");
        invoice.setRegistration(new Registration());
        invoice.getRegistration().setCustomer(customer);
        invoice.setStatus(Invoice.Status.Completed);
        boolean updatedInvoice = invoiceService.updateInvoiceAmount(personSession, "invoice_id", 100.0);

        assertFalse(updatedInvoice);
    }


    @Test
    public void updateInvoiceStatus_invalid_invoice_does_not_exist() {
        SportCenter sportCenter = new SportCenter();
        Customer customer = new Customer();
        customer.setId("customer_id");
        PersonSession personSession = new PersonSession(
                "owner",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );
        when(invoiceRepository.findInvoiceById(anyString())).thenReturn(null);
        boolean updatedInvoice = invoiceService.updateInvoiceStatus(personSession, "invoice_id", "Completed");

        assertFalse(updatedInvoice);
        verify(invoiceRepository, times(1)).findInvoiceById(anyString());
        verify(invoiceRepository, times(0)).save(any(Invoice.class));
    }

}
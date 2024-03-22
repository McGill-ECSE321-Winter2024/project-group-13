package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Invoice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, String> {

  Invoice findInvoiceById(String id);

  List<Invoice> findInvoicesByRegistration_Customer_Id(String customerId);

  List<Invoice> findAllByIdIsNotNull ();

}

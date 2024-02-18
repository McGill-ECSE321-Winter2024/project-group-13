package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, String> {

  Invoice findInvoiceById(String id);
}

package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dto.*;
import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * @Author Teddy El-Husseini
     * @param bearerToken
     * @return ResponseEntity
     */
    @GetMapping(value = { "/registrations", "/registrations/"})
    ResponseEntity<?> getRegistrations(@RequestHeader (HttpHeaders.AUTHORIZATION) String bearerToken){

        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            List<Registration> registrations = registrationService.getAllRegistrations();
            return new ResponseEntity<>(convertRegistrationListToDTO(registrations), HttpStatus.OK) ;}

        else if (personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            List<Registration> registrations = registrationService.getRegistrationsForInstructor(personSession.getPersonId());
            if (registrations==null){return DefaultHTTPResponse.badRequest("InstructorID cannot be empty!");}
            return new ResponseEntity<>(convertRegistrationListToDTO(registrations), HttpStatus.OK) ;}

        else if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
            List<Registration> registrations = registrationService.getRegistrationsForCustomer(personSession.getPersonId());
            if (registrations==null){return DefaultHTTPResponse.badRequest("CustomerID cannot be empty!");}
            return new ResponseEntity<>(convertRegistrationListToDTO(registrations), HttpStatus.OK) ;}

        else {
           return DefaultHTTPResponse.badRequest("Invalid Credentials!");}
        }

    /**
     * @Author Teddy El-Husseini
     * @param registration_id
     * @param bearerToken
     * @return ResponseEntity
     */
    @GetMapping(value = {"/registrations/{registration_id}", "/registrations/{registration_id}/"})
public ResponseEntity<?> getSpecificRegistration(@PathVariable String registration_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String bearerToken) {

        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

    if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
        Registration registrations = registrationService.getSpecificRegistrationByID(registration_id);
        if (registrations==null){return DefaultHTTPResponse.badRequest("Please check RegistrationID");}
        return new ResponseEntity<>(convertToDTO(registrations), HttpStatus.OK) ;}

    else if (personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
        Registration registrations = registrationService.getSpecificRegistrationByIDForInstructor(personSession.getPersonId(), registration_id);
        if (registrations==null){return DefaultHTTPResponse.badRequest("Please check InstructorID or RegistrationID");}
        return new ResponseEntity<>(convertToDTO(registrations), HttpStatus.OK) ;}

    else if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
        Registration registrations = registrationService.getSpecificRegistrationByIDForCustomer(personSession.getPersonId(), registration_id);
        if (registrations==null){return DefaultHTTPResponse.badRequest("Please check CustomerID or RegistrationID");}
        return new ResponseEntity<>(convertToDTO(registrations), HttpStatus.OK) ;}

    else {
        return DefaultHTTPResponse.badRequest("Invalid Credentials!");}

}

    /**
     * @Author Teddy El-Husseini
     * @param registration_id
     * @param bearerToken
     * @return Boolean
     */
    @RequestMapping(value = {"/registrations/{registration_id}/cancel","/registrations/{registration_id}/cancel/"} , method = RequestMethod.POST)
    Boolean cancelSpecificRegistration(@PathVariable("registration_id") String registration_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String bearerToken) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return registrationService.cancelRegistration(registration_id);}

        else if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
            return registrationService.cancelRegistrationByCustomer(personSession.getPersonId(), registration_id);}

        else {
            return false;}

    }

    /**
     * @Author Teddy El-Husseini
     * @param registration_id
     * @param rating
     * @param bearerToken
     * @return Boolean
     */
    @RequestMapping(value = {"/registrations/{registration_id}/{rating}","/registrations/{registration_id}/{rating}/" }, method = RequestMethod.POST)
    Boolean updateRegistrationRating(@PathVariable("registration_id") String registration_id ,@PathVariable("rating") Integer rating, @RequestHeader (HttpHeaders.AUTHORIZATION) String bearerToken) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
            return registrationService.updateRegistrationRating(personSession,registration_id, rating);}

        else {
            return false;}
    }

    /**
     * @Author Teddy El-Husseini
     * @param registration_id
     * @param bearerToken
     * @return ResponseEntity
     */
    @GetMapping(value = {"/registrations/{registration_id}/invoices", "/registrations/{registration_id}/invoices"})
    ResponseEntity<?>  getInvoicesForRegistration(@PathVariable("registration_id") String registration_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String bearerToken) {

        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            List<Invoice> registrations = registrationService.getInvoicesForRegistration(registration_id);
            if (registrations ==null ){return DefaultHTTPResponse.badRequest("Please check RegistrationID");}
            return new ResponseEntity<>(convertInvoiceListToDTO(registrations), HttpStatus.OK) ;}

        else if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
            List<Invoice> registrations = registrationService.getInvoicesForCustomerRegistration(personSession.getPersonId(), registration_id);
            if (registrations==null){return DefaultHTTPResponse.badRequest("Please check CustomerID or RegistrationID");}
            return new ResponseEntity<>(convertInvoiceListToDTO(registrations), HttpStatus.OK) ;}

        else {
            List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
            return new ResponseEntity<>(invoiceDTOS, HttpStatus.BAD_REQUEST) ;}

    }

    /**
     * Converts Registration to RegistrationDTO
     * @param registration
     * @return RegistrationDTO
     * @Author Teddy El-Husseini
     */
    private RegistrationDTO convertToDTO(Registration registration){
        return new RegistrationDTO(registration);
    }

    /**
     * Converts a List of Registrations to a List of RegistrationDTOs
     * @param registrations
     * @return List of registrations
     * @Author Teddy El-Husseini
     */
    private List<RegistrationDTO> convertRegistrationListToDTO(List<Registration> registrations){
        List<RegistrationDTO> registrationDTOS = new ArrayList<>();

        for (Registration r : registrations) {
            registrationDTOS.add(convertToDTO(r));
        }
        return registrationDTOS;
    }

    /**
     * Converts invoice to invoiceDTO
     * @param invoice
     * @return InvoiceDTO
     * @Author Teddy El-Husseini
     */
    private InvoiceDTO convertToDTO(Invoice invoice){

        InvoiceDTO invoiceDTO = new InvoiceDTO(invoice);
        invoiceDTO.setRegistrationDTO(convertToDTO(invoice.getRegistration()));
        return invoiceDTO;
    }

    /**
     * Converts a List of Invoices to a List of InvoicesDTOs
     * @param invoices
     * @return List of invoices
     * @Author Teddy El-Husseini
     */
    private List<InvoiceDTO> convertInvoiceListToDTO(List<Invoice> invoices){
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();

        for (Invoice i : invoices) {
            invoiceDTOS.add(convertToDTO(i));
        }
        return invoiceDTOS;
    }
}

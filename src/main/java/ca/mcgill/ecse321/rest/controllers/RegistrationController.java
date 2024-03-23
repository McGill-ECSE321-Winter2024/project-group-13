package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.dto.RegistrationDTO;
import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    @RequestMapping(value = {"/registrations/{registration_id}/cancel","/registrations/{registration_id}/cancel/"} , method = RequestMethod.POST)
    ResponseEntity<?>  cancelSpecificRegistration(@PathVariable("registration_id") String registration_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String bearerToken) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            Boolean registrations = registrationService.cancelRegistration(registration_id);
            if (registrations.equals(false)){return DefaultHTTPResponse.badRequest("Please check RegistrationID");}
            return new ResponseEntity<>(registrations, HttpStatus.OK) ;}

        else if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
            Boolean registrations = registrationService.cancelRegistrationByCustomer(personSession.getPersonId(), registration_id);
            if (registrations.equals(false)){return DefaultHTTPResponse.badRequest("Please check CustomerID or RegistrationID");}
            return new ResponseEntity<>(registrations, HttpStatus.OK) ;}

        else {
            return DefaultHTTPResponse.badRequest("Invalid Credentials!");}

    }

    @RequestMapping(value = {"/registrations/{registration_id}/{rating}","/registrations/{registration_id}/{rating}/" }, method = RequestMethod.POST)
    ResponseEntity<?> updateRegistrationRating(@PathVariable("registration_id") String registration_id ,@PathVariable("rating") Integer rating, @RequestHeader (HttpHeaders.AUTHORIZATION) String bearerToken) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)){
            Boolean registrations = registrationService.updateRegistrationRating(personSession,registration_id, rating);
            if (registrations.equals(false)){return DefaultHTTPResponse.badRequest("Please check CustomerID, RegistrationID or Rating");}
            return new ResponseEntity<>(registrations, HttpStatus.OK) ;}

        else {
            return DefaultHTTPResponse.badRequest("Invalid Credentials!");}

    }

    @GetMapping(value = {"/registrations/{registration_id}/invoices", "/registrations/{registration_id}/invoices/"})
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
            return DefaultHTTPResponse.badRequest("Invalid Credentials!");}

    }

    private RegistrationDTO convertToDTO(Registration registration){

        CustomerDTO customerDTO = new CustomerDTO(registration.getCustomer());
        CourseDTO courseDTO = new CourseDTO(registration.getCourse());

        return new RegistrationDTO(registration);
    }

    private List<RegistrationDTO> convertRegistrationListToDTO(List<Registration> registrations){
        List<RegistrationDTO> registrationDTOS = new ArrayList<>();

        for (Registration r : registrations) {
            registrationDTOS.add(convertToDTO(r));
        }
        return registrationDTOS;

    }


    private InvoiceDTO convertToDTO(Invoice invoice){

        InvoiceDTO invoiceDTO = new InvoiceDTO(invoice);
        invoiceDTO.setRegistrationDTO(convertToDTO(invoice.getRegistration()));
        return invoiceDTO;
    }

    private List<InvoiceDTO> convertInvoiceListToDTO(List<Invoice> invoices){
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();

        for (Invoice i : invoices) {
            invoiceDTOS.add(new InvoiceDTO(i));
        }
        return invoiceDTOS;

    }


}

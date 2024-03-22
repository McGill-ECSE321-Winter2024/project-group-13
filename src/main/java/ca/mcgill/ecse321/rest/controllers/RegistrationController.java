package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.dto.RegistrationDTO;
import ca.mcgill.ecse321.rest.models.*;
import ca.mcgill.ecse321.rest.services.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/registrations")
    List<RegistrationDTO> getRegistrations(@RequestHeader("Authorization") String bearerToken){
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        return convertRegistrationListToDTO(registrationService.getRegistrations(personSession));
    }

    @GetMapping("/registrations/{registration_id}")
    RegistrationDTO getSpecificRegistration(@RequestHeader("Authorization") String bearerToken, @PathVariable("registration_id") String registration_id) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        return convertToDTO(registrationService.getSpecificRegistration(personSession, registration_id));
    }

    @RequestMapping(value = "/registrations/{registration_id}/cancel", method = RequestMethod.POST)
    boolean cancelSpecificRegistration(@RequestHeader("Authorization") String bearerToken, @PathVariable("registration_id") String registration_id) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        return registrationService.cancelSpecificRegistration(personSession, registration_id);
    }

    @RequestMapping(value = "/registrations/{registration_id}/{rating}", method = RequestMethod.POST)
    boolean updateRegistrationRating(@RequestHeader("Authorization") String bearerToken, @PathVariable("registration_id") String registration_id ,@PathVariable("rating") Integer rating) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        return registrationService.updateRegistrationRating(personSession, registration_id, rating);

    }

    @GetMapping("/registrations/{registration_id}/invoices")
    List<InvoiceDTO> getInvoicesForRegistration(@RequestHeader("Authorization") String bearerToken, @PathVariable("registration_id") String registration_id) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

        return convertInvoiceListToDTO(registrationService.getInvoices(personSession, registration_id));
    }

    private RegistrationDTO convertToDTO(Registration registration){

        CustomerDTO customerDTO = new CustomerDTO(registration.getCustomer());
        CourseDTO courseDTO = new CourseDTO(registration.getCourse());

        return new RegistrationDTO(customerDTO, courseDTO, registration.getRating());
    }

    private List<RegistrationDTO> convertRegistrationListToDTO(List<Registration> registrations){
        List<RegistrationDTO> registrationDTOS = new ArrayList<>();

        for (Registration r : registrations) {
            registrationDTOS.add(convertToDTO(r));
        }
        return registrationDTOS;

    }
    private List<InvoiceDTO> convertInvoiceListToDTO(List<Invoice> invoices){
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();

        for (Invoice i : invoices) {
            invoiceDTOS.add(new InvoiceDTO(i));
        }
        return invoiceDTOS;

    }


}

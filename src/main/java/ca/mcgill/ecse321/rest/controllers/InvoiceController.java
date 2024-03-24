package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.CreateInvoiceDTO;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class InvoiceController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> verify(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(authorization);
        if(personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            return DefaultHTTPResponse.forbidden("Instructors do not have access to invoices");
        }
        return new ResponseEntity<>(invoiceService.getAllInvoices(personSession), HttpStatus.OK);
    }

    @RequestMapping(value = "/invoices/{invoice_id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getInvoice(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable("invoice_id") String invoiceId
    ) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(authorization);
        if(personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            return DefaultHTTPResponse.unauthorized("Instructors do not have access to invoices");
        }
        InvoiceDTO invoiceDTO = invoiceService.getInvoice(personSession, invoiceId);
        if(invoiceDTO == null) {
            return DefaultHTTPResponse.notFound("Invoice not found");
        }
        return new ResponseEntity<>(invoiceDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> generateCheckoutSession(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody CreateInvoiceDTO createInvoiceDTO
    ) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(authorization);
        if(personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            return DefaultHTTPResponse.unauthorized("Instructors do not have access to invoices");
        }
        try {
            InvoiceDTO invoiceDTO = invoiceService.createInvoice(
                    personSession,
                    createInvoiceDTO.getRegistrationId(),
                    createInvoiceDTO.getAmount()
            );
            if(invoiceDTO == null) {
                return DefaultHTTPResponse.badRequest("Invalid request");
            }
            return new ResponseEntity<>(invoiceDTO, HttpStatus.OK);
        } catch (Exception e) {
            return DefaultHTTPResponse.badRequest(e.getMessage());
        }
    }

    @RequestMapping(value = "/invoices/{invoice_id}/status", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateInvoiceStatus(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable("invoice_id") String invoiceId,
            @RequestParam("status") String status
    ) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(authorization);
        if(!personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return DefaultHTTPResponse.unauthorized("Only owners can update invoice status");
        }
        if(invoiceService.updateInvoiceStatus(personSession, invoiceId, status)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @RequestMapping(value = "/invoices/{invoice_id}/amount", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateInvoiceAmount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable("invoice_id") String invoiceId,
            @RequestParam("amount") double amount
    ) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(authorization);
        if(!personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return DefaultHTTPResponse.unauthorized("Only owners can update invoice amount");
        }
        if(invoiceService.updateInvoiceAmount(personSession, invoiceId, amount)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }





}

package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dto.LoginDTO;
import ca.mcgill.ecse321.rest.dto.auth.RegisterDTO;
import ca.mcgill.ecse321.rest.dto.auth.SessionDTO;
import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = {"/auth/login", "/auth/login/"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginDTO body) {

        String email = body.getEmail();
        String password = body.getPassword();
        if (!authenticationService.isValidCredentials(email, password))
            return DefaultHTTPResponse.unauthorized("Invalid credentials. Please change them");
        String session = authenticationService.issueTokenWithEmail(email);
        PersonSession person = authenticationService.verifyTokenAndGetUser("Bearer "+session);
        System.out.println(session);

        return new ResponseEntity<>(new SessionDTO(
                session,
                person.getPersonId(),
                person.getPersonType().toString(),
                person.getPersonName(),
                person.getPersonEmail(),
                person.getPersonPhoneNumber(),
                person.getSportCenterId()
        ), HttpStatus.OK);
    }

    @RequestMapping(value = {"/auth/verify", "/auth/verify/"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> verify(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.unauthorized();
        return new ResponseEntity<>(DefaultHTTPResponse.success("valid session"), HttpStatus.OK);
    }

    @RequestMapping(value = {"/auth/register/customer", "/auth/register/customer/"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> registerCustomer(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody RegisterDTO body) {
        try {
            authenticationService.registerCustomer(
                    body.getEmail(),
                    body.getPassword(),
                    body.getName(),
                    body.getPhoneNumber()
            );
            return new ResponseEntity<>(DefaultHTTPResponse.success("Customer registered"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return DefaultHTTPResponse.badRequest(e.getMessage());
        }

    }

    @RequestMapping(value = {"/auth/password", "/auth/password/"}, method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody LoginDTO body) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.badRequest();
        authenticationService.changePassword(person.getPersonId(), body.getPassword());
        return new ResponseEntity<>(DefaultHTTPResponse.success("Password changed"), HttpStatus.OK);
    }

    @RequestMapping(value = {"/auth/email", "/auth/email/"}, method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> changeEmail(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Map<String, String> body) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.badRequest();
        String error = authenticationService.changeEmail(person.getPersonId(), body.get("email"));
        if (error != null) return DefaultHTTPResponse.badRequest(error);
        return new ResponseEntity<>(DefaultHTTPResponse.success("Email changed"), HttpStatus.OK);
    }

    @RequestMapping(value = {"/auth/phoneNumber", "/auth/phoneNumber/"}, method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> changePhoneNumber(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Map<String, String> body) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.badRequest();
        String error = authenticationService.changePhoneNumber(person.getPersonId(), body.get("phoneNumber"));

        if (error != null) return DefaultHTTPResponse.badRequest(error);
        return new ResponseEntity<>(DefaultHTTPResponse.success("Phone number changed"), HttpStatus.OK);
    }



}


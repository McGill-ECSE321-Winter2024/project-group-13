package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.LoginDTO;
import ca.mcgill.ecse321.rest.dto.auth.RegisterDTO;
import ca.mcgill.ecse321.rest.dto.auth.SessionDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
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

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginDTO body) {

        String email = body.getEmail();
        String password = body.getPassword();
        if (!authenticationService.isValidCredentials(email, password))
            return DefaultHTTPResponse.unauthorized("Invalid credentials");
        String session = authenticationService.issueTokenWithEmail(email);
        System.out.println(session);

        return new ResponseEntity<>(new SessionDTO(session), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/verify", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> verify(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.unauthorized();
        return new ResponseEntity<>(new SessionDTO(person.getPersonId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/register/customer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> registerCustomer(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody RegisterDTO body) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if(person.getPersonType() != PersonSession.PersonType.Owner) return DefaultHTTPResponse.unauthorized();
        String session = authenticationService.registerCustomer(
            body.getEmail(),
            body.getPassword(),
            body.getName(),
            body.getPhoneNumber()
        );

        return new ResponseEntity<>(new SessionDTO(session), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/password", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody LoginDTO body) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.badRequest();
        authenticationService.changePassword(person.getPersonId(), body.getPassword());
        return new ResponseEntity<>(new SessionDTO("Password changed"), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/email", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> changeEmail(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Map<String, String> body) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.badRequest();
        String error = authenticationService.changeEmail(person.getPersonId(), body.get("email"));
        if (error != null) return new ResponseEntity<>(new HTTPDTO(error), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new SessionDTO("Email changed"), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/phoneNumber", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> changePhoneNumber(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Map<String, String> body) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonId() == null) return DefaultHTTPResponse.badRequest();
        String error = authenticationService.changePhoneNumber(person.getPersonId(), body.get("phoneNumber"));
        if (error != null) return new ResponseEntity<>(new HTTPDTO(error), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new SessionDTO("Phone number changed"), HttpStatus.OK);
    }



}


package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.dto.LoginDTO;
import ca.mcgill.ecse321.rest.dto.SessionDTO;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SessionDTO> login(@RequestBody LoginDTO body) {

        String email = body.email;
        String password = body.password;
        if (!authenticationService.isValidCredentials(email, password))
            throw new IllegalArgumentException("Invalid credentials");
        String session = authenticationService.issueToken(email);
        System.out.println(session);

        return new ResponseEntity<>(new SessionDTO(session), HttpStatus.OK);
    }




}


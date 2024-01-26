package ca.mcgill.ecse321.rest.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {

    @GetMapping("/api/hello")
    public String sayHello() {
        return "Hello, World!";
    }

}

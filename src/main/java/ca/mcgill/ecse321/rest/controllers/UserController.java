package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
import ca.mcgill.ecse321.rest.dto.InstructorDTO;
import ca.mcgill.ecse321.rest.dto.LoginDTO;
import ca.mcgill.ecse321.rest.dto.auth.SessionDTO;
import ca.mcgill.ecse321.rest.services.CustomerService;
import ca.mcgill.ecse321.rest.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController

public class UserController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private CustomerService customerService;

    // View all instructors
    @GetMapping(value = { "/instructors", "/instructors/" })
    @ResponseBody
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<InstructorDTO> instructors = instructorService.findAll();
        return ResponseEntity.ok(instructors);
    }

    // View all customers
    @GetMapping(value = { "/customers", "/customers/" })
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    // Create instructor
    @PostMapping(value = {"/instructors", "/instructors/"})
    public ResponseEntity<InstructorDTO> createInstructor(@RequestBody InstructorDTO instructorDTO) {
        InstructorDTO savedInstructor = instructorService.save(instructorDTO);
        return ResponseEntity.ok(savedInstructor);
    }
}

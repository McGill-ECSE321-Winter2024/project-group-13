package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
import ca.mcgill.ecse321.rest.dto.InstructorDTO;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CustomerService;
import ca.mcgill.ecse321.rest.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    private AuthenticationService authenticationService;

    // View all instructors
    @GetMapping(value = { "/instructors", "/instructors/" })
    @ResponseBody
    public ResponseEntity<List<InstructorDTO>> getAllInstructors(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        if (person!=null) {
            List<InstructorDTO> instructors = instructorService.findAll(person);
            return ResponseEntity.ok(instructors);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // View all customers
    @GetMapping(value = { "/customers", "/customers/" })
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonType().equals(PersonSession.PersonType.Owner) ||
                person.getPersonType().equals(PersonSession.PersonType.Instructor)) {
            List<CustomerDTO> customers = customerService.findAll(person);
            return ResponseEntity.ok(customers);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Create instructor
    @PostMapping(value = {"/instructors", "/instructors/"})
    public ResponseEntity<InstructorDTO> createInstructor(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization,@RequestBody InstructorDTO instructorDTO) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        if (person.getPersonType().equals(PersonSession.PersonType.Owner)) {
            InstructorDTO savedInstructor = instructorService.save(instructorDTO,person);
            return ResponseEntity.ok(savedInstructor);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

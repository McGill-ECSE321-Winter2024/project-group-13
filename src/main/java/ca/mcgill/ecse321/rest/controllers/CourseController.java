package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.auth.SessionDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping(value = { "/courses", "/courses/" })
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(@RequestBody CourseDTO courseDTO) {
        Course createdCourse = courseService.createCourse(courseDTO);
        return new CourseDTO(createdCourse);
    }
    @PostMapping(value = { "/courses/{course_id}/approve", "/courses/{course_id}/approve/" })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SessionDTO> approveCourse(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.approveCourse(course_id, person);
        if (errorMessage.isEmpty()){
            return new ResponseEntity<>(new SessionDTO("Course approved"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new SessionDTO(errorMessage), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(value = { "/courses/{course_id}/name", "/courses/{course_id}/name/" })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SessionDTO> updateCourseName(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
    , @RequestBody String name) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseName(person, course_id, name);
        if (errorMessage.isEmpty()){
            return new ResponseEntity<>(new SessionDTO("Course name changed"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new SessionDTO(errorMessage), HttpStatus.BAD_REQUEST);
        }
    }

}

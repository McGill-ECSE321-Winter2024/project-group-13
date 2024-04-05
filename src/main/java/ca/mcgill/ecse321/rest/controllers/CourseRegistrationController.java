package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseRegistrationController {
    @Autowired
    private CourseRegistrationService courseRegistrationService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/courses/{courseId}/register")
    public ResponseEntity<?> registerForCourse(@PathVariable String courseId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(authorization);
        String responseMessage = courseRegistrationService.registerForCourse(courseId, personSession);

        if ("Successfully registered for the course.".equals(responseMessage)) {
            return ResponseEntity.ok().body(responseMessage);
        } else {
            return ResponseEntity.badRequest().body(responseMessage);
        }
    }
}


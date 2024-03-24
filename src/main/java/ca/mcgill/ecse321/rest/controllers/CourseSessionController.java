package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseService;
import ca.mcgill.ecse321.rest.services.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

import static ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse.*;

@CrossOrigin(origins = "*")
@RestController
public class CourseSessionController {
    /*
    @Autowired
    private CourseSessionService courseSessionService;
    @Autowired
    private AuthenticationService authenticationService;
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<HTTPDTO> handleUnsupportedMediaType() {
        // Create an error response with appropriate message
        return badRequest("Invalid input type");
    }
    @PostMapping(value = { "/courses/{course_id}/sessions", "/courses/sessions/" })
    public ResponseEntity<HTTPDTO> createCourseSession(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody(required = false)  String courseID) {
        if (courseID==null || courseID.isEmpty()){
            return badRequest("Course not found");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage= courseSessionService.createCourseSession(courseID,person);
        return getResponse(errorMessage,"Course Created successfully");
    }
    @PutMapping(value = { "/courses/{course_id}/sessions/{id}", "/courses/{course_id}/name/" })
    public ResponseEntity<HTTPDTO> updateCourseSessionStart(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false)String time) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        Timestamp startTime;
        try{
            startTime=Timestamp.valueOf(time);
        }
        catch (Exception e){
            return badRequest("Invalid start time");
        }
        String errorMessage=courseSessionService.updateCourseSessionStart(course_id,startTime ,person);
        return getResponse(errorMessage,"Course session start time changed");
    }
    @PutMapping(value = { "/courses/{course_id}/name", "/courses/{course_id}/name/" })
    public ResponseEntity<HTTPDTO> updateCourseSessionEnd(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false)  String name) {
        if (name==null || name.isEmpty()){
            return badRequest("Requires valid name");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseSessionService.updateCourseSessionStart(person, course_id, name);
        return getResponse(errorMessage,"Course name changed");
    }
    @DeleteMapping(value = { "/courses/{course_id}", "/courses/{course_id}/" })
    public ResponseEntity<HTTPDTO> deleteCourse(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.deleteCourse(person, course_id);
        return getResponse(errorMessage,"Course deleted");
    }
    public ResponseEntity<HTTPDTO> getResponse(String errorMessage, String successMessage){
        if (errorMessage.isEmpty()){
            return success(successMessage);
        }
        else if(errorMessage.equals("Must be an owner")
                || errorMessage.equals("Session must belong to the same sports center")){
            return forbidden(errorMessage);
        }
        else {
            return badRequest(errorMessage);
        }
    }
     */
}

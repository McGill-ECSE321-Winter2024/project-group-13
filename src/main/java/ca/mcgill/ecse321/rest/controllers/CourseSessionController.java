package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CourseSessionDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseDetailService;
import ca.mcgill.ecse321.rest.services.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import static ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse.*;

@CrossOrigin(origins = "*")
@RestController
public class CourseSessionController {
    @Autowired
    private CourseSessionService courseSessionService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CourseDetailService courseDetailService;
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<HTTPDTO> handleUnsupportedMediaType() {
        // Create an error response with appropriate message
        return badRequest("Invalid input type");
    }
    @PostMapping(value = { "/sessions", "/sessions/" })
    public ResponseEntity<HTTPDTO> createCourseSession(@RequestBody(required = false)String course_id,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        CourseDTO courseDTO= new CourseDTO(courseDetailService.getSpecificCourse(course_id));
        String errorMessage = "";
        if(!person.getPersonType().equals(PersonSession.PersonType.Owner)){
            errorMessage="Must be an owner";
        }
        if (!person.getSportCenterId().equals(courseDTO.getSportCenter())){
            errorMessage="Session must belong to the same sports center errorMessage";
        }
        if (errorMessage.isEmpty()){
            errorMessage= courseSessionService.createCourseSession(course_id,person);
        }
        return getResponse(errorMessage,"Course session created successfully");
    }

    @GetMapping(value = { "/sessions/{session_id}", "/sessions/{session_id}/" })
    public ResponseEntity<?> getCourseSession(@PathVariable String session_id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            PersonSession personSession = authenticationService.verifyTokenAndGetUser(authorization);
            CourseSessionDTO courseSessionDTO=new CourseSessionDTO(courseSessionService.getCourseSession(session_id));
            CourseDTO courseDTO= new CourseDTO(courseDetailService.getSpecificCourse(courseSessionDTO.getCourse()));
            if (courseSessionDTO.getId() == null) {
                return notFound("Course session not found");
            }
            if (!personSession.getSportCenterId().equals(courseDTO.getSportCenter())){
                return forbidden("Session must belong to the same sports center errorMessage");
            }
            switch (personSession.getPersonType()) {
                case Owner:
                    // For owner, show any course
                    return ResponseEntity.ok(courseSessionDTO);
                case Instructor:
                    // For instructor, show all active courses + courses created by them

                    if (courseDTO.getCourseState().equals("Approved") || courseDTO.getInstructor().equals(personSession.getPersonId())) {
                        return ResponseEntity.ok(courseSessionDTO);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                case Customer:
                    // For customer, only show active courses
                    if (courseDTO.getCourseState().equals("Approved") && courseDTO.getSportCenter().equals(personSession.getSportCenterId())) {
                        return ResponseEntity.ok(courseDTO);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                default:
                    // If the user type is not recognized or authorized
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (IllegalArgumentException e) {
            // Handle the case where the token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (Exception error){
            return badRequest(error.getMessage());
        }
    }
    @PutMapping(value = { "/sessions/{session_id}/startTime", "/sessions/{session_id}/startTime/" })
    public ResponseEntity<HTTPDTO> updateCourseSessionStart(@PathVariable String session_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false)String time) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        Timestamp startTime;
        try{
            startTime=Timestamp.valueOf(time);
        }
        catch (Exception e){
            return badRequest("Invalid start time"+e);
        }
        String errorMessage=courseSessionService.updateCourseSessionStart(session_id,startTime ,person);
        return getResponse(errorMessage,"Course session start time changed");
    }
    @PutMapping(value = { "/sessions/{session_id}/endTime", "/sessions/{session_id}/endTime/" })
    public ResponseEntity<HTTPDTO> updateCourseSessionEnd(@PathVariable String session_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false)String time) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        Timestamp endTime;
        try{
            endTime=Timestamp.valueOf(time);
        }
        catch (Exception e){
            return badRequest("Invalid start time");
        }
        String errorMessage=courseSessionService.updateCourseSessionEnd(session_id,endTime,person);
        return getResponse(errorMessage,"Course session end time changed");
    }
    @DeleteMapping(value = { "/sessions/{session_id}", "/sessions/{session_id}/" })
    public ResponseEntity<HTTPDTO> deleteCourseSession(@PathVariable String session_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseSessionService.deleteCourseSession(session_id, person);
        return getResponse(errorMessage,"Course session deleted");
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
}

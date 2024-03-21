package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import static ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse.*;

@CrossOrigin(origins = "*")
@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping(value = { "/courses", "/courses/" })
    public ResponseEntity<HTTPDTO> createCourse(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody CourseDTO courseDTO) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage= courseService.createCourse(courseDTO,person);
        return getResponse(errorMessage,"Course Created successfully");
    }
    @PostMapping(value = { "/courses/{course_id}/approve", "/courses/{course_id}/approve/" })
    public ResponseEntity<HTTPDTO> approveCourse(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.approveCourse(course_id, person);
        return getResponse(errorMessage,"Course approved");
    }
    @PutMapping(value = { "/courses/{course_id}/name", "/courses/{course_id}/name/" })
    public ResponseEntity<HTTPDTO> updateCourseName(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
    , @RequestBody String name) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseName(person, course_id, name);
        return getResponse(errorMessage,"Course name changed");
    }

    @PutMapping(value = { "/courses/{course_id}/description", "/courses/{course_id}/description/" })
    public ResponseEntity<HTTPDTO> updateCourseDescription(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody String description) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseDescription(person, course_id, description);
        return getResponse(errorMessage,"Course description changed");
    }
    @PutMapping(value = { "/courses/{course_id}/level", "/courses/{course_id}/level/" })
    public ResponseEntity<HTTPDTO> updateCourseLevel(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody String level) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseLevel(person, course_id, level);
        return getResponse(errorMessage,"Course level changed");
    }
    @PutMapping(value = { "/courses/{course_id}/rate", "/courses/{course_id}/rate/" })
    public ResponseEntity<HTTPDTO> updateCourseRate(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody Double hourlyRateAmount) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseRate(person, course_id, hourlyRateAmount);
        return getResponse(errorMessage,"Course rate changed");
    }
    @PutMapping(value = { "/courses/{course_id}/startDate", "/courses/{course_id}/startDate/" })
    public ResponseEntity<HTTPDTO> updateCourseStartDate(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody Timestamp courseStartDate) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseStartDate(person, course_id, courseStartDate);
        return getResponse(errorMessage,"Course start date changed");
    }
    @PutMapping(value = { "/courses/{course_id}/endDate", "/courses/{course_id}/endDate/" })
    public ResponseEntity<HTTPDTO> updateCourseEndDate(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody Timestamp courseEndDate) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseEndDate(person, course_id, courseEndDate);
        return getResponse(errorMessage,"Course end date changed");
    }
    @PutMapping(value = { "/courses/{course_id}/room", "/courses/{course_id}/room/" })
    public ResponseEntity<HTTPDTO> updateCourseRoom(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody String roomID) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseRoom(person, course_id, roomID);
        return getResponse(errorMessage,"Course room changed");
    }
    @PutMapping(value = { "/courses/{course_id}/instructor", "/courses/{course_id}/instructor/" })
    public ResponseEntity<HTTPDTO> updateCourseInstructor(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody String instructorID) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseInstructor(person, course_id, instructorID);
        return getResponse(errorMessage,"Course instructor changed");
    }
    @PutMapping(value = { "/courses/{course_id}/schedule", "/courses/{course_id}/schedule/" })
    public ResponseEntity<HTTPDTO> updateCourseSchedule(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody String scheduleID) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseSchedule(person, course_id, scheduleID);
        return getResponse(errorMessage,"Course schedule changed");
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
        else if(errorMessage.equals("Must be an owner of the course's sports center")
        || errorMessage.equals("Must be an owner or instructor")){
            return forbidden(errorMessage);
        }
        else {
            return badRequest(errorMessage);
        }
    }


}

package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseDetailService;
import ca.mcgill.ecse321.rest.services.CourseService;
import ca.mcgill.ecse321.rest.services.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

import static ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse.*;

@CrossOrigin(origins = "*")
@RestController
public class CourseController {
    @Autowired private CourseService courseService;
    @Autowired private CourseSessionService courseSessionService;
    @Autowired private CourseDetailService courseDetailService;
    @Autowired private AuthenticationService authenticationService;

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<HTTPDTO> handleUnsupportedMediaType() {
        // Create an error response with appropriate message
        return badRequest("Invalid input type");
    }
    @PostMapping(value = { "/courses", "/courses/" })
    public ResponseEntity<HTTPDTO> createCourse(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody(required = false)  String name) {
        if (name==null || name.isEmpty()){
            return badRequest("Course requires name to be created");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage= courseService.createCourse(name,person);
        if(errorMessage.startsWith("--ID: ")){
            HTTPDTO httpDTO = new HTTPDTO();
            httpDTO.setMessage(errorMessage.replace("--ID: ",""));
            return new ResponseEntity<>(httpDTO, HttpStatus.OK);
        }
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
    , @RequestBody(required = false)  String name) {
        if (name==null || name.isEmpty()){
            return badRequest("Requires valid name");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        // remove all non alphanumeric characters
        String errorMessage=courseService.updateCourseName(person, course_id, name.replaceAll("[^a-zA-Z0-9\\-\\s']", ""));
        return getResponse(errorMessage,"Course name changed");
    }

    @PutMapping(value = { "/courses/{course_id}/description", "/courses/{course_id}/description/" })
    public ResponseEntity<HTTPDTO> updateCourseDescription(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false)  String description) {
        if (description==null || description.isEmpty()){
            return badRequest("Requires valid description");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseDescription(person, course_id, description.replaceAll("[^a-zA-Z0-9\\-\\s']", ""));
        return getResponse(errorMessage,"Course description changed");
    }
    @PutMapping(value = { "/courses/{course_id}/level", "/courses/{course_id}/level/" })
    public ResponseEntity<HTTPDTO> updateCourseLevel(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false) String level) {
        if (level==null || level.isEmpty()){
            return badRequest("Requires valid level");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseLevel(person, course_id, level.replaceAll("[^a-zA-Z0-9\\-\\s']", ""));
        return getResponse(errorMessage,"Course level changed");
    }
    @PutMapping(value = { "/courses/{course_id}/rate", "/courses/{course_id}/rate/" })
    public ResponseEntity<HTTPDTO> updateCourseRate(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false) Double hourlyRateAmount) {
        if (hourlyRateAmount==null ){
            return badRequest("Requires valid hourly rate amount");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseRate(person, course_id, hourlyRateAmount);
        return getResponse(errorMessage,"Course rate changed");
    }
    @PutMapping(value = { "/courses/{course_id}/startDate", "/courses/{course_id}/startDate/" })
    public ResponseEntity<HTTPDTO> updateCourseStartDate(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false) Timestamp courseStartDate) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseStartDate(person, course_id, courseStartDate);
        return getResponse(errorMessage,"Course start date changed");
    }
    @PutMapping(value = { "/courses/{course_id}/endDate", "/courses/{course_id}/endDate/" })
    public ResponseEntity<HTTPDTO> updateCourseEndDate(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false) Timestamp courseEndDate) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseEndDate(person, course_id, courseEndDate);
        return getResponse(errorMessage,"Course end date changed");
    }
    @PutMapping(value = { "/courses/{course_id}/room", "/courses/{course_id}/room/" })
    public ResponseEntity<HTTPDTO> updateCourseRoom(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false) String roomID) {
        if (roomID==null || roomID.isEmpty()){
            return badRequest("Requires valid room id");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage=courseService.updateCourseRoom(person, course_id, roomID.replaceAll("[^a-zA-Z0-9\\-]", ""));
        return getResponse(errorMessage,"Course room changed");
    }
    @PutMapping(value = { "/courses/{course_id}/instructor", "/courses/{course_id}/instructor/" })
    public ResponseEntity<HTTPDTO> updateCourseInstructor(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false) String instructorID) {
        if (instructorID==null || instructorID.isEmpty()){
            return badRequest("Requires valid instructor id");
        }
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        // remove all " " chars
        String errorMessage=courseService.updateCourseInstructor(person, course_id, instructorID.replaceAll("[^a-zA-Z0-9\\-]", ""));
        return getResponse(errorMessage,"Course instructor changed");
    }
    @PutMapping(value = { "/courses/{course_id}/schedule", "/courses/{course_id}/schedule/" })
    public ResponseEntity<HTTPDTO> updateCourseSchedule(@PathVariable String course_id, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization
            , @RequestBody(required = false) String scheduleID) {
        if (scheduleID==null || scheduleID.isEmpty()){
            return badRequest("Requires valid schedule id");
        }
        String errorMessage;
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        CourseDTO courseDTO= new CourseDTO(courseDetailService.getSpecificCourse(course_id));
        if(courseDTO.getId()==null){
            errorMessage = "Course does not exist";
        }
        else {
            errorMessage = courseService.updateCourseSchedule(person, course_id, scheduleID);
            if (courseDTO.getSchedule() == null && errorMessage.isEmpty()) {
                errorMessage = courseSessionService.createSessionsPerCourse(course_id);
            }
        }
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

    @PutMapping(value = "/courses/{course_id}/update-schedule-and-sessions")
    public ResponseEntity<HTTPDTO> addOrUpdateCourseScheduleAndSessions(@PathVariable String course_id, @RequestBody ScheduleDTO scheduleDTO,
                                                             @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person == null) {
            return forbidden("Authorization required");
        }
        String errorMessage = courseService.addOrUpdateCourseScheduleAndSessions(course_id, scheduleDTO, person);
        if (errorMessage.isEmpty()) {
            return success("Schedule successfully added/updated and linked to the course");
        } else {
            return badRequest(errorMessage);
        }
    }

    @PutMapping(value = "/courses/{course_id}/state")
    public ResponseEntity<HTTPDTO> updateCourseState(@PathVariable String course_id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody Map<String, String> stateMap) {
        String newState = stateMap.get("state");
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        if (person == null || !person.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return forbidden("Only owners can modify course state.");
        }
        String errorMessage = courseService.updateCourseState(course_id, newState, person);
        return getResponse(errorMessage, "Course state updated successfully");
    }




}

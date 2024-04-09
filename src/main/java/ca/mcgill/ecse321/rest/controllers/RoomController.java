package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CourseSessionDTO;
import ca.mcgill.ecse321.rest.dto.RoomDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse.*;

@CrossOrigin(origins = "*")
@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private AuthenticationService authenticationService;

    // get all rooms
    @GetMapping(value = { "/rooms", "/rooms/" })
    public ResponseEntity<?> getAllRooms(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        try {
            List<RoomDTO> rooms = roomService.getAllRooms(person);
            return ResponseEntity.ok(rooms);
        }
        catch (Exception e){
            return badRequest(e.getMessage());
        }
    }


    // Create Room (Owner Only)
    @PostMapping(value = { "/rooms", "/rooms/" })
    public ResponseEntity<HTTPDTO> createRoom(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody RoomDTO roomDTO) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage = roomService.createRoom(roomDTO, person);
        return getResponse(errorMessage,"Room Created successfully");
    }

    // Get Courses Per Room
    @GetMapping(value = {"/room/courses", "/room/courses/"})
    public ResponseEntity<?> getCoursesPerRoom(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody RoomDTO roomDTO){
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        List<CourseDTO> coursesPerRoom = roomService.getCoursesPerRoom(roomDTO);
        return ResponseEntity.ok(coursesPerRoom);
    }

    // Get Course Sessions Per Room (Owner Only)
    @GetMapping(value = {"/room/course-sessions", "/room/course-sessions/"})
    public ResponseEntity<?> getCourseSessionsPerRoom(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody RoomDTO roomDTO){
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        List<CourseSessionDTO> courseSessionsPerRoom = roomService.getCourseSessionsPerRoom(roomDTO);
        if (person.getPersonType() != PersonSession.PersonType.Owner ) return DefaultHTTPResponse.badRequest("Must be the Owner of the Sport Center");
        return ResponseEntity.ok(courseSessionsPerRoom);
    }

    // Delete Room (Owner Only)
    @DeleteMapping(value = { "/rooms/{roomID}", "/rooms/{roomID}/" })
    public ResponseEntity<HTTPDTO> deleteRoom(@PathVariable String roomID, @RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage = roomService.deleteRoom(roomID, person);
        return getResponse(errorMessage,"Room deleted");
    }

    public ResponseEntity<HTTPDTO> getResponse(String errorMessage, String successMessage){
        if (errorMessage.isEmpty()){
            return success(successMessage);
        }
        else if(errorMessage.equals("Must be the Owner of the Sport Center")){
            return forbidden(errorMessage);
        }
        else {
            return badRequest(errorMessage);
        }
    }


}

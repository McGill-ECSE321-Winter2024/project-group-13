package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.RoomDTO;
import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
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

    // Create Room (Owner Only)
    @PostMapping(value = { "/rooms", "/rooms/" })
    public ResponseEntity<HTTPDTO> createRoom(@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization, @RequestBody RoomDTO roomDTO) {
        PersonSession person = authenticationService.verifyTokenAndGetUser(authorization);
        String errorMessage = roomService.createRoom(roomDTO, person);
        return getResponse(errorMessage,"Room Created successfully");
    }

    // View Courses Per Room
    @GetMapping(value = {"/rooms/{roomID}/course", "/rooms/{roomID}/course/"})
    public List<CourseDTO> getCoursesPerRoom(@RequestHeader("Authorization") String bearerToken, @RequestBody RoomDTO roomDTO){
        PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);
        return roomService.getCoursesPerRoom(roomDTO, personSession);
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

package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.dto.SportCenterDTO;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.rest.services.SportCenterService;

@CrossOrigin(origins = "*")
@RestController
public class SportCenterController {

    @Autowired
    private SportCenterService sportCenterService;
    @Autowired
    private AuthenticationService authenticationService;
    // Get sport center DTO
    @GetMapping(value = {"/sportcenter", "/sportcenter/"})
    public ResponseEntity<SportCenterDTO> getSportCenter() {
        SportCenterDTO sportCenterDTO = sportCenterService.getSportCenterDTO();
        return ResponseEntity.ok(sportCenterDTO);
    }

    // Change name (Owner only)
    @PutMapping(value = {"/sportcenter/name", "/sportcenter/name/"})
    public ResponseEntity<SportCenterDTO> changeSportCenterName(@RequestParam String newName,@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        if(sportCenterService.updateName(newName,person)) return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Update Address (Owner only)
    @PutMapping(value = {"/sportcenter/address", "/sportcenter/address/"})
    public ResponseEntity<SportCenterDTO> updateSportCenterAddress(@RequestParam String newAddress,@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        if(sportCenterService.updateAddress(newAddress,person)) return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Change operating hours (Owner only)
    @PutMapping(value = {"/sportcenter/opening-hours", "/sportcenter/opening-hours/"})
    public ResponseEntity<SportCenterDTO> updateSchedule(@RequestBody ScheduleDTO newSchedule,@RequestHeader (HttpHeaders.AUTHORIZATION) String authorization) {
        PersonSession person= authenticationService.verifyTokenAndGetUser(authorization);
        if(sportCenterService.updateSchedule(newSchedule,person)) return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

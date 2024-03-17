package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.dto.SportCenterDTO;
import ca.mcgill.ecse321.rest.models.SportCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.rest.services.SportCenterService;

@CrossOrigin(origins = "*")
@RestController
public class SportCenterController {

    @Autowired
    private SportCenterService sportCenterService;

    // Get sport center DTO
    @GetMapping(value = {"/sportcenter", "/sportcenter/"})
    public ResponseEntity<SportCenterDTO> getSportCenter() {
        SportCenterDTO sportCenter = sportCenterService.getSportCenterDTO();
        return ResponseEntity.ok(sportCenter);
    }

    // Change name (Owner only)
    @PutMapping(value = {"/sportcenter/name", "/sportcenter/name/"})
    public ResponseEntity<SportCenterDTO> changeSportCenterName(@RequestParam String newName) {
        SportCenterDTO updatedSportCenter = sportCenterService.updateName(newName);
        return ResponseEntity.ok(updatedSportCenter);
    }

    // Update Address (Owner only)
    @PutMapping(value = {"/sportcenter/address", "/sportcenter/address/"})
    public ResponseEntity<SportCenterDTO> updateSportCenterAddress(@RequestParam String newAddress) {
        SportCenterDTO updatedSportCenter = sportCenterService.updateAddress(newAddress);
        return ResponseEntity.ok(updatedSportCenter);
    }

    // Change operating hours (Owner only)
    @PutMapping(value = {"/sportcenter/opening-hours", "/sportcenter/opening-hours/"})
    public ResponseEntity<SportCenterDTO> updateSchedule(@RequestBody ScheduleDTO newSchedule) {
        SportCenterDTO updatedSportCenter = sportCenterService.updateSchedule(newSchedule);
        return ResponseEntity.ok(updatedSportCenter);
    }
}

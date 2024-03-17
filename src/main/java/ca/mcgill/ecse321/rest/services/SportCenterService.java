package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.dto.SportCenterDTO;
import ca.mcgill.ecse321.rest.models.Schedule;
import ca.mcgill.ecse321.rest.models.SportCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;

@Service
public class SportCenterService {

    @Autowired
    private SportCenterRepository sportCenterRepository;

    public SportCenterDTO getSportCenterDTO() {
        SportCenter sportCenter = sportCenterRepository.findFirst();
        return convertToDto(sportCenter);
    }

    public SportCenterDTO updateName(String newName) {
        SportCenter sportCenter = sportCenterRepository.findFirst();
        sportCenter.setName(newName);
        sportCenter = sportCenterRepository.save(sportCenter);
        return convertToDto(sportCenter);
    }

    public SportCenterDTO updateAddress(String newAddress) {
        SportCenter sportCenter = sportCenterRepository.findFirst();
        sportCenter.setAddress(newAddress);
        sportCenter = sportCenterRepository.save(sportCenter);
        return convertToDto(sportCenter);
    }

    public SportCenterDTO updateSchedule(ScheduleDTO newScheduleDTO) {
        SportCenter sportCenter = sportCenterRepository.findFirst();
        Schedule newSchedule = convertToEntity(newScheduleDTO);
        sportCenter.setSchedule(newSchedule);
        sportCenter = sportCenterRepository.save(sportCenter);
        return convertToDto(sportCenter);
    }

    private Schedule convertToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setMondayStart(Time.valueOf(scheduleDTO.getMondayStart()));
        schedule.setMondayEnd(Time.valueOf(scheduleDTO.getMondayEnd()));
        schedule.setTuesdayStart(Time.valueOf(scheduleDTO.getTuesdayStart()));
        schedule.setTuesdayEnd(Time.valueOf(scheduleDTO.getTuesdayEnd()));
        schedule.setWednesdayStart(Time.valueOf(scheduleDTO.getWednesdayStart()));
        schedule.setWednesdayEnd(Time.valueOf(scheduleDTO.getWednesdayEnd()));
        schedule.setThursdayStart(Time.valueOf(scheduleDTO.getThursdayStart()));
        schedule.setThursdayEnd(Time.valueOf(scheduleDTO.getThursdayEnd()));
        schedule.setFridayStart(Time.valueOf(scheduleDTO.getFridayStart()));
        schedule.setFridayEnd(Time.valueOf(scheduleDTO.getFridayEnd()));
        schedule.setSaturdayStart(Time.valueOf(scheduleDTO.getSaturdayStart()));
        schedule.setSaturdayEnd(Time.valueOf(scheduleDTO.getSaturdayEnd()));
        schedule.setSundayStart(Time.valueOf(scheduleDTO.getSundayStart()));
        schedule.setSundayEnd(Time.valueOf(scheduleDTO.getSundayEnd()));
        return schedule;
    }

    private SportCenterDTO convertToDto(SportCenter sportCenter) {
        return new SportCenterDTO(sportCenter);
    }
}

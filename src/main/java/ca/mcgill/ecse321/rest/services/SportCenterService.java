package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.ScheduleRepository;
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

    @Autowired
    private ScheduleRepository scheduleRepository;

    public SportCenterDTO getSportCenterDTO(PersonSession personSession) {
        if (personSession!=null){
            SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
            return convertToDto(sportCenter);
        }else {
            return null;
        }
    }

    public boolean updateName(String newName, PersonSession personSession) {
        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)){
            SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
            sportCenter.setName(newName);
            sportCenterRepository.save(sportCenter);
            return true;
        }
        return false;
    }

    public boolean updateAddress(String newAddress, PersonSession personSession) {
        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
              SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
              sportCenter.setAddress(newAddress);
              sportCenterRepository.save(sportCenter);
              return true;
        }
        return false;
    }

    public boolean updateSchedule(ScheduleDTO newScheduleDTO, PersonSession personSession) {
        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
              SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
              Schedule newSchedule = convertToEntity(newScheduleDTO);
              scheduleRepository.save(newSchedule);
              sportCenter.setSchedule(newSchedule);
              sportCenterRepository.save(sportCenter);
              return true;
        }
        return false;
    }

    private Schedule convertToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(scheduleDTO.getId());
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

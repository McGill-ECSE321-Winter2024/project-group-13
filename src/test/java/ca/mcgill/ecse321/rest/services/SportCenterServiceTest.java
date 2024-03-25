package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.dto.SportCenterDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;

@Nested
@ExtendWith(MockitoExtension.class)
public class SportCenterServiceTest {
    @Mock
    private SportCenterRepository sportCenterRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private SportCenterService sportCenterService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getSportCenterTestDTO() {
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        sportCenter.setName("Sport Center");
        sportCenter.setAddress("1234 Street");
        Schedule schedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        sportCenter.setSchedule(schedule);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);
        PersonSession personSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        SportCenterDTO result = sportCenterService.getSportCenterDTO(personSession);

        assertNotNull(result);
        assertEquals(sportCenter.getId(), result.getId());
        assertEquals(sportCenter.getName(), result.getName());
        assertEquals(sportCenter.getAddress(), result.getAddress());
        assertEquals(sportCenter.getSchedule().getMondayStart(), Time.valueOf(result.getSchedule().getMondayStart()));
        assertEquals(sportCenter.getSchedule().getMondayEnd(), Time.valueOf(result.getSchedule().getMondayEnd()));
        assertEquals(sportCenter.getSchedule().getTuesdayStart(), Time.valueOf(result.getSchedule().getTuesdayStart()));
        assertEquals(sportCenter.getSchedule().getTuesdayEnd(), Time.valueOf(result.getSchedule().getTuesdayEnd()));
        assertEquals(sportCenter.getSchedule().getWednesdayStart(), Time.valueOf(result.getSchedule().getWednesdayStart()));
        assertEquals(sportCenter.getSchedule().getWednesdayEnd(), Time.valueOf(result.getSchedule().getWednesdayEnd()));
        assertEquals(sportCenter.getSchedule().getThursdayStart(), Time.valueOf(result.getSchedule().getThursdayStart()));
        assertEquals(sportCenter.getSchedule().getThursdayEnd(), Time.valueOf(result.getSchedule().getThursdayEnd()));
        assertEquals(sportCenter.getSchedule().getFridayStart(), Time.valueOf(result.getSchedule().getFridayStart()));
        assertEquals(sportCenter.getSchedule().getFridayEnd(), Time.valueOf(result.getSchedule().getFridayEnd()));
        assertEquals(sportCenter.getSchedule().getSaturdayStart(), Time.valueOf(result.getSchedule().getSaturdayStart()));
        assertEquals(sportCenter.getSchedule().getSaturdayEnd(), Time.valueOf(result.getSchedule().getSaturdayEnd()));
        assertEquals(sportCenter.getSchedule().getSundayStart(), Time.valueOf(result.getSchedule().getSundayStart()));
        assertEquals(sportCenter.getSchedule().getSundayEnd(), Time.valueOf(result.getSchedule().getSundayEnd()));

    }

    @Test
    void updateNameTestOwner() {
        String newName = "New Name";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession personSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);

        boolean result = sportCenterService.updateName(newName, personSession);

        assertTrue(result);
        assertEquals(newName, sportCenter.getName());
    }

    @Test
    void updateNameTestInstructor() {
        String oldName = "Old Name";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);

        boolean result = sportCenterService.updateName(oldName, ownerSession);

        assertTrue(result);
        assertEquals(oldName, sportCenter.getName());

        String newName = "New Name";
        PersonSession instructorSession = new PersonSession("1",PersonSession.PersonType.Instructor,sportsCenterID);

        boolean result2 = sportCenterService.updateName(newName, instructorSession);

        assertFalse(result2);
        assertNotEquals(newName, sportCenter.getName());
        assertEquals(oldName, sportCenter.getName());
    }

    @Test
    void updateNameTestCustomer() {
        String oldName = "Old Name";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);

        boolean result = sportCenterService.updateName(oldName, ownerSession);

        assertTrue(result);
        assertEquals(oldName, sportCenter.getName());

        String newName = "New Name";
        PersonSession customerSession = new PersonSession("1",PersonSession.PersonType.Customer,sportsCenterID);

        boolean result2 = sportCenterService.updateName(newName, customerSession);

        assertFalse(result2);
        assertNotEquals(newName, sportCenter.getName());
        assertEquals(oldName, sportCenter.getName());
    }


    @Test
    void updateAddressTestOwner() {
        String newAddress = "New Address";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession personSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);

        boolean result = sportCenterService.updateAddress(newAddress, personSession);

        assertTrue(result);
        assertEquals(newAddress, sportCenter.getAddress());
    }

    @Test
    void updateAddressTestInstructor() {
        String oldAddress = "Old Address";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);

        boolean result = sportCenterService.updateAddress(oldAddress, ownerSession);

        assertTrue(result);
        assertEquals(oldAddress, sportCenter.getAddress());

        String newAddress = "New Address";
        PersonSession instructorSession = new PersonSession("1",PersonSession.PersonType.Instructor,sportsCenterID);

        boolean result2 = sportCenterService.updateAddress(newAddress, instructorSession);

        assertFalse(result2);
        assertNotEquals(newAddress, sportCenter.getAddress());
        assertEquals(oldAddress, sportCenter.getAddress());
    }

    @Test
    void updateAddressTestCustomer() {
        String oldAddress = "Old Address";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);

        boolean result = sportCenterService.updateAddress(oldAddress, ownerSession);

        assertTrue(result);
        assertEquals(oldAddress, sportCenter.getAddress());

        String newAddress = "New Address";
        PersonSession customerSession = new PersonSession("1",PersonSession.PersonType.Customer,sportsCenterID);

        boolean result2 = sportCenterService.updateAddress(newAddress, customerSession);

        assertFalse(result2);
        assertNotEquals(newAddress, sportCenter.getAddress());
        assertEquals(oldAddress, sportCenter.getAddress());
    }

    @Test
    void updateScheduleTestOwner() {
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession personSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);
        Schedule schedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);

        boolean result = sportCenterService.updateSchedule(scheduleDTO, personSession);

        assertTrue(result);
        assertEquals(schedule.getMondayStart(), sportCenter.getSchedule().getMondayStart());
        assertEquals(schedule.getMondayEnd(), sportCenter.getSchedule().getMondayEnd());
        assertEquals(schedule.getTuesdayStart(), sportCenter.getSchedule().getTuesdayStart());
        assertEquals(schedule.getTuesdayEnd(), sportCenter.getSchedule().getTuesdayEnd());
        assertEquals(schedule.getWednesdayStart(), sportCenter.getSchedule().getWednesdayStart());
        assertEquals(schedule.getWednesdayEnd(), sportCenter.getSchedule().getWednesdayEnd());
        assertEquals(schedule.getThursdayStart(), sportCenter.getSchedule().getThursdayStart());
        assertEquals(schedule.getThursdayEnd(), sportCenter.getSchedule().getThursdayEnd());
        assertEquals(schedule.getFridayStart(), sportCenter.getSchedule().getFridayStart());
        assertEquals(schedule.getFridayEnd(), sportCenter.getSchedule().getFridayEnd());
        assertEquals(schedule.getSaturdayStart(), sportCenter.getSchedule().getSaturdayStart());
        assertEquals(schedule.getSaturdayEnd(), sportCenter.getSchedule().getSaturdayEnd());
        assertEquals(schedule.getSundayStart(), sportCenter.getSchedule().getSundayStart());
        assertEquals(schedule.getSundayEnd(), sportCenter.getSchedule().getSundayEnd());
    }

    @Test
    void updateScheduleTestInstructor() {
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);
        Schedule schedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);

        boolean result = sportCenterService.updateSchedule(scheduleDTO, ownerSession);

        assertTrue(result);
        assertEquals(schedule.getMondayStart(), sportCenter.getSchedule().getMondayStart());
        assertEquals(schedule.getMondayEnd(), sportCenter.getSchedule().getMondayEnd());
        assertEquals(schedule.getTuesdayStart(), sportCenter.getSchedule().getTuesdayStart());
        assertEquals(schedule.getTuesdayEnd(), sportCenter.getSchedule().getTuesdayEnd());
        assertEquals(schedule.getWednesdayStart(), sportCenter.getSchedule().getWednesdayStart());
        assertEquals(schedule.getWednesdayEnd(), sportCenter.getSchedule().getWednesdayEnd());
        assertEquals(schedule.getThursdayStart(), sportCenter.getSchedule().getThursdayStart());
        assertEquals(schedule.getThursdayEnd(), sportCenter.getSchedule().getThursdayEnd());
        assertEquals(schedule.getFridayStart(), sportCenter.getSchedule().getFridayStart());
        assertEquals(schedule.getFridayEnd(), sportCenter.getSchedule().getFridayEnd());
        assertEquals(schedule.getSaturdayStart(), sportCenter.getSchedule().getSaturdayStart());
        assertEquals(schedule.getSaturdayEnd(), sportCenter.getSchedule().getSaturdayEnd());
        assertEquals(schedule.getSundayStart(), sportCenter.getSchedule().getSundayStart());
        assertEquals(schedule.getSundayEnd(), sportCenter.getSchedule().getSundayEnd());

        Schedule newSchedule = new Schedule(new Time(9,0,0),new Time(21,0,0));
        ScheduleDTO newScheduleDTO = new ScheduleDTO(newSchedule);
        PersonSession instructorSession = new PersonSession("1",PersonSession.PersonType.Instructor,sportsCenterID);

        boolean result2 = sportCenterService.updateSchedule(newScheduleDTO, instructorSession);

        assertFalse(result2);
        assertNotEquals(newSchedule.getMondayStart(), sportCenter.getSchedule().getMondayStart());
        assertNotEquals(newSchedule.getMondayEnd(), sportCenter.getSchedule().getMondayEnd());
        assertNotEquals(newSchedule.getTuesdayStart(), sportCenter.getSchedule().getTuesdayStart());
        assertNotEquals(newSchedule.getTuesdayEnd(), sportCenter.getSchedule().getTuesdayEnd());
        assertNotEquals(newSchedule.getWednesdayStart(), sportCenter.getSchedule().getWednesdayStart());
        assertNotEquals(newSchedule.getWednesdayEnd(), sportCenter.getSchedule().getWednesdayEnd());
        assertNotEquals(newSchedule.getThursdayStart(), sportCenter.getSchedule().getThursdayStart());
        assertNotEquals(newSchedule.getThursdayEnd(), sportCenter.getSchedule().getThursdayEnd());
        assertNotEquals(newSchedule.getFridayStart(), sportCenter.getSchedule().getFridayStart());
        assertNotEquals(newSchedule.getFridayEnd(), sportCenter.getSchedule().getFridayEnd());
        assertNotEquals(newSchedule.getSaturdayStart(), sportCenter.getSchedule().getSaturdayStart());
        assertNotEquals(newSchedule.getSaturdayEnd(), sportCenter.getSchedule().getSaturdayEnd());
        assertNotEquals(newSchedule.getSundayStart(), sportCenter.getSchedule().getSundayStart());
        assertNotEquals(newSchedule.getSundayEnd(), sportCenter.getSchedule().getSundayEnd());
    }

    @Test
    void updateScheduleTestCustomer() {
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByIdNotNull()).thenReturn(sportCenter);
        Schedule schedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);

        boolean result = sportCenterService.updateSchedule(scheduleDTO, ownerSession);

        assertTrue(result);
        assertEquals(schedule.getMondayStart(), sportCenter.getSchedule().getMondayStart());
        assertEquals(schedule.getMondayEnd(), sportCenter.getSchedule().getMondayEnd());
        assertEquals(schedule.getTuesdayStart(), sportCenter.getSchedule().getTuesdayStart());
        assertEquals(schedule.getTuesdayEnd(), sportCenter.getSchedule().getTuesdayEnd());
        assertEquals(schedule.getWednesdayStart(), sportCenter.getSchedule().getWednesdayStart());
        assertEquals(schedule.getWednesdayEnd(), sportCenter.getSchedule().getWednesdayEnd());
        assertEquals(schedule.getThursdayStart(), sportCenter.getSchedule().getThursdayStart());
        assertEquals(schedule.getThursdayEnd(), sportCenter.getSchedule().getThursdayEnd());
        assertEquals(schedule.getFridayStart(), sportCenter.getSchedule().getFridayStart());
        assertEquals(schedule.getFridayEnd(), sportCenter.getSchedule().getFridayEnd());
        assertEquals(schedule.getSaturdayStart(), sportCenter.getSchedule().getSaturdayStart());
        assertEquals(schedule.getSaturdayEnd(), sportCenter.getSchedule().getSaturdayEnd());
        assertEquals(schedule.getSundayStart(), sportCenter.getSchedule().getSundayStart());
        assertEquals(schedule.getSundayEnd(), sportCenter.getSchedule().getSundayEnd());

        Schedule newSchedule = new Schedule(new Time(9,0,0),new Time(21,0,0));
        ScheduleDTO newScheduleDTO = new ScheduleDTO(newSchedule);
        PersonSession customerSession = new PersonSession("1",PersonSession.PersonType.Customer,sportsCenterID);

        boolean result2 = sportCenterService.updateSchedule(newScheduleDTO, customerSession);

        assertFalse(result2);
        assertNotEquals(newSchedule.getMondayStart(), sportCenter.getSchedule().getMondayStart());
        assertNotEquals(newSchedule.getMondayEnd(), sportCenter.getSchedule().getMondayEnd());
        assertNotEquals(newSchedule.getTuesdayStart(), sportCenter.getSchedule().getTuesdayStart());
        assertNotEquals(newSchedule.getTuesdayEnd(), sportCenter.getSchedule().getTuesdayEnd());
        assertNotEquals(newSchedule.getWednesdayStart(), sportCenter.getSchedule().getWednesdayStart());
        assertNotEquals(newSchedule.getWednesdayEnd(), sportCenter.getSchedule().getWednesdayEnd());
        assertNotEquals(newSchedule.getThursdayStart(), sportCenter.getSchedule().getThursdayStart());
        assertNotEquals(newSchedule.getThursdayEnd(), sportCenter.getSchedule().getThursdayEnd());
        assertNotEquals(newSchedule.getFridayStart(), sportCenter.getSchedule().getFridayStart());
        assertNotEquals(newSchedule.getFridayEnd(), sportCenter.getSchedule().getFridayEnd());
        assertNotEquals(newSchedule.getSaturdayStart(), sportCenter.getSchedule().getSaturdayStart());
        assertNotEquals(newSchedule.getSaturdayEnd(), sportCenter.getSchedule().getSaturdayEnd());
        assertNotEquals(newSchedule.getSundayStart(), sportCenter.getSchedule().getSundayStart());
        assertNotEquals(newSchedule.getSundayEnd(), sportCenter.getSchedule().getSundayEnd());
    }

}

package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
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
import java.sql.Timestamp;

@Nested
@ExtendWith(MockitoExtension.class)
public class SportCenterServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SportCenterRepository sportCenterRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private SportCenterService sportCenterService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//    @Test
//    void createSportCenter_validInput() {
//        // Arrange
//        String name = "Sport Center";
//        String address = "1234 Street";
//        SportCenter sportCenter = new SportCenter();
//        sportCenter.setName(name);
//        sportCenter.setAddress(address);
//        when(sportCenterRepository.save(any(SportCenter.class))).thenReturn(sportCenter);
//
//        // Act
//        SportCenter result = sportCenterService.createSportCenter(name, address);
//    }
    @Test
    void updateNameTestOwner() {
        // Arrange
        String newName = "New Name";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession personSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);

        // Act
        boolean result = sportCenterService.updateName(newName, personSession);

        // Assert
        assertTrue(result);
        assertEquals(newName, sportCenter.getName());
    }

    @Test
    void updateNameTestInstructor() {
        // Arrange
        String oldName = "Old Name";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);

        // Act
        boolean result = sportCenterService.updateName(oldName, ownerSession);

        // Assert
        assertTrue(result);
        assertEquals(oldName, sportCenter.getName());

        // Arrange
        String newName = "New Name";
        PersonSession instructorSession = new PersonSession("1",PersonSession.PersonType.Instructor,sportsCenterID);

        // Act
        boolean result2 = sportCenterService.updateName(newName, instructorSession);

        // Assert
        assertFalse(result2);
        assertNotEquals(newName, sportCenter.getName());
        assertEquals(oldName, sportCenter.getName());
    }

    @Test
    void updateNameTestCustomer() {
        // Arrange
        String oldName = "Old Name";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);

        // Act
        boolean result = sportCenterService.updateName(oldName, ownerSession);

        // Assert
        assertTrue(result);
        assertEquals(oldName, sportCenter.getName());

        // Arrange
        String newName = "New Name";
        PersonSession customerSession = new PersonSession("1",PersonSession.PersonType.Customer,sportsCenterID);

        // Act
        boolean result2 = sportCenterService.updateName(newName, customerSession);

        // Assert
        assertFalse(result2);
        assertNotEquals(newName, sportCenter.getName());
        assertEquals(oldName, sportCenter.getName());
    }


    @Test
    void updateAddressTestOwner() {
        // Arrange
        String newAddress = "New Address";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession personSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);

        // Act
        boolean result = sportCenterService.updateAddress(newAddress, personSession);

        // Assert
        assertTrue(result);
        assertEquals(newAddress, sportCenter.getAddress());
    }

    @Test
    void updateAddressTestInstructor() {
        // Arrange
        String oldAddress = "Old Address";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);

        // Act
        boolean result = sportCenterService.updateAddress(oldAddress, ownerSession);

        // Assert
        assertTrue(result);
        assertEquals(oldAddress, sportCenter.getAddress());

        // Arrange
        String newAddress = "New Address";
        PersonSession instructorSession = new PersonSession("1",PersonSession.PersonType.Instructor,sportsCenterID);

        // Act
        boolean result2 = sportCenterService.updateAddress(newAddress, instructorSession);

        // Assert
        assertFalse(result2);
        assertNotEquals(newAddress, sportCenter.getAddress());
        assertEquals(oldAddress, sportCenter.getAddress());
    }

    @Test
    void updateAddressTestCustomer() {
        // Arrange
        String oldAddress = "Old Address";
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);

        // Act
        boolean result = sportCenterService.updateAddress(oldAddress, ownerSession);

        // Assert
        assertTrue(result);
        assertEquals(oldAddress, sportCenter.getAddress());

        // Arrange
        String newAddress = "New Address";
        PersonSession customerSession = new PersonSession("1",PersonSession.PersonType.Customer,sportsCenterID);

        // Act
        boolean result2 = sportCenterService.updateAddress(newAddress, customerSession);

        // Assert
        assertFalse(result2);
        assertNotEquals(newAddress, sportCenter.getAddress());
        assertEquals(oldAddress, sportCenter.getAddress());
    }

    @Test
    void updateScheduleTestOwner() {
        // Arrange
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession personSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);
        Schedule schedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);

        // Act
        boolean result = sportCenterService.updateSchedule(scheduleDTO, personSession);

        // Assert
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
        // Arrange
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);
        Schedule schedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);

        // Act
        boolean result = sportCenterService.updateSchedule(scheduleDTO, ownerSession);

        // Assert
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

        // Arrange
        Schedule newSchedule = new Schedule(new Time(9,0,0),new Time(21,0,0));
        ScheduleDTO newScheduleDTO = new ScheduleDTO(newSchedule);
        PersonSession instructorSession = new PersonSession("1",PersonSession.PersonType.Instructor,sportsCenterID);

        // Act
        boolean result2 = sportCenterService.updateSchedule(newScheduleDTO, instructorSession);

        // Assert
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
        // Arrange
        String sportsCenterID = "1";
        SportCenter sportCenter = new SportCenter(sportsCenterID);
        PersonSession ownerSession= new PersonSession("1",PersonSession.PersonType.Owner,sportsCenterID);
        when(sportCenterRepository.findSportCenterByScheduleNotNull()).thenReturn(sportCenter);
        Schedule schedule = new Schedule(new Time(8,0,0),new Time(22,0,0));
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);

        // Act
        boolean result = sportCenterService.updateSchedule(scheduleDTO, ownerSession);

        // Assert
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

        // Arrange
        Schedule newSchedule = new Schedule(new Time(9,0,0),new Time(21,0,0));
        ScheduleDTO newScheduleDTO = new ScheduleDTO(newSchedule);
        PersonSession customerSession = new PersonSession("1",PersonSession.PersonType.Customer,sportsCenterID);

        // Act
        boolean result2 = sportCenterService.updateSchedule(newScheduleDTO, customerSession);

        // Assert
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

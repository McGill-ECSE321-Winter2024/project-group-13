package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.RegistrationRepository;
import ca.mcgill.ecse321.rest.dao.RoomRepository;
import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
import ca.mcgill.ecse321.rest.dto.RoomDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private SportCenterRepository sportCenterRepository;
    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRoom_ValidInput() {

        String roomName = "Yoga Room";
        String sportsCenterID = "ID1234";
        String personID = "ID56789";

        SportCenter sportCenter= new SportCenter(sportsCenterID);

        Room room = new Room();
        room.setRoomName(roomName);
        room.setSportCenter(sportCenter);

        RoomDTO roomDTO = new RoomDTO(room);

        PersonSession personSessionOwner= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        PersonSession personSessionInstructor= new PersonSession(personID,PersonSession.PersonType.Instructor,sportsCenterID);
        PersonSession personSessionCustomer= new PersonSession(personID,PersonSession.PersonType.Customer,sportsCenterID);

        when(sportCenterRepository.findSportCenterById(roomDTO.getSportCenter())).thenReturn(sportCenter);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        String errorMessageOwner= roomService.createRoom(roomDTO, personSessionOwner);
        String errorMessageInstructor= roomService.createRoom(roomDTO, personSessionInstructor);
        String errorMessageCustomer= roomService.createRoom(roomDTO, personSessionCustomer);

        assertEquals("", errorMessageOwner);
        assertEquals("Must be the Owner of the Sport Center", errorMessageInstructor);
        assertEquals("Must be the Owner of the Sport Center", errorMessageCustomer);

        verify(sportCenterRepository).findSportCenterById(roomDTO.getSportCenter());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void createRoom_InvalidInput() {
        String roomName = "Yoga Room";
        String sportsCenterID = "ID1234";
        String personID = "ID56789";

        SportCenter sportCenter = new SportCenter(sportsCenterID);

        Room room = new Room();
        room.setRoomName(roomName);
        room.setSportCenter(sportCenter);

        RoomDTO roomDTO = new RoomDTO("", sportsCenterID);
        RoomDTO roomDTO1=  new RoomDTO(roomName,"123");
        PersonSession personSession = new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);

        String errorMessageName = roomService.createRoom(roomDTO, personSession);
        String errorMessageID = roomService.createRoom(roomDTO1, personSession);

        assertEquals("Room requires name to be created", errorMessageName);
        assertEquals("Invalid Sport Center id", errorMessageID);
    }

    @Test
    void deleteRoom() {
        String roomName= "HealthPlus";
        String sportsCenterID="ID1234";
        String personID="ID56789";
        String roomID= "roomID";
        Room room = new Room();
        SportCenter sportCenter= new SportCenter(sportsCenterID);
        room.setRoomName(roomName);
        room.setSportCenter(sportCenter);
        PersonSession personSessionOwner= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        PersonSession personSessionInstructor= new PersonSession(personID,PersonSession.PersonType.Instructor,sportsCenterID);
        PersonSession personSessionCustomer= new PersonSession(personID,PersonSession.PersonType.Customer,sportsCenterID);
        when(roomRepository.findRoomById(roomID)).thenReturn(room);
        when(roomRepository.findRoomById("")).thenReturn(null);


        String errorMessageOwner= roomService.deleteRoom(roomID, personSessionOwner);
        String errorMessageInstructor= roomService.deleteRoom(roomID, personSessionInstructor);
        String errorMessageCustomer= roomService.deleteRoom(roomID, personSessionCustomer);
        String errorMessageInvalid= roomService.deleteRoom("", personSessionOwner);

        assertEquals("",errorMessageOwner);
        assertEquals("Must be the Owner of the Sport Center",errorMessageInstructor);
        assertEquals("Must be the Owner of the Sport Center",errorMessageCustomer);
        assertEquals("Room does not exist",errorMessageInvalid);

        verify(roomRepository,times(3)).findRoomById(roomID);
        verify(roomRepository).findRoomById("");
        verify(roomRepository).deleteRoomById(roomID);
    }

}
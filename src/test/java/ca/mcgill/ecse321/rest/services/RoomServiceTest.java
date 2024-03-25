package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CourseSessionDTO;
import ca.mcgill.ecse321.rest.dto.RoomDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CourseSessionRepository courseSessionRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private SportCenterRepository sportCenterRepository;
    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomRepository.deleteAll();
    }

    @Order(1)
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

    @Order(2)
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
    @Order(3)
    @Test
    void getRoomTest() {
        String roomName = "Yoga Room";
        String sportsCenterID = "ID1234";
        String personID = "ID56789";

        SportCenter sportCenter = new SportCenter(sportsCenterID);

        Room room = new Room();
        room.setRoomName(roomName);
        room.setSportCenter(sportCenter);
        room.setId("roomID");
        roomRepository.save(room);
        when(roomRepository.findRoomById(room.getId())).thenReturn(room);

        PersonSession personSessionOwner= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        Room room1 = roomService.getRoom(room.getId(), personSessionOwner).getRoom();
        assertNotNull(room1);
        assertEquals(room, room1);
    }

    @Order(4)
    @Test
    void getCoursesPerRoomTest() {
        String roomName = "Yoga Room";
        String sportsCenterID = "ID1234";
        String personID = "ID56789";

        SportCenter sportCenter = new SportCenter(sportsCenterID);

        Room room = new Room();
        room.setRoomName(roomName);
        room.setSportCenter(sportCenter);
        room.setId("roomID");
        roomRepository.save(room);

        Course course = new Course();
        course.setName("Yoga");
        course.setRoom(room);
        course.setId("courseID");
        course.setSportCenter(sportCenter);

        Course course1 = new Course();
        course1.setName("Pilates");
        course1.setRoom(room);
        course1.setId("courseID1");
        course1.setSportCenter(sportCenter);

        Course course2 = new Course();
        course2.setName("Zumba");
        course2.setRoom(room);
        course2.setId("courseID2");
        course2.setSportCenter(sportCenter);

        List<Course> courses = List.of(course,course1,course2);
        when(courseRepository.findCoursesByRoomId(room.getId())).thenReturn(courses);
        PersonSession personSessionOwner= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        List<CourseDTO> courses1 = roomService.getCoursesPerRoom(new RoomDTO(room));
        assertNotNull(courses1);
        assertEquals(3,courses1.size());
        assertEquals(new CourseDTO(course),courses1.get(0));
        assertEquals(new CourseDTO(course1),courses1.get(1));
        assertEquals(new CourseDTO(course2),courses1.get(2));


    }

    @Order(5)
    @Test
    void getCourseSessionsPerRoom() {
        String roomName = "Yoga Room";
        String sportsCenterID = "ID1234";
        String personID = "ID56789";

        SportCenter sportCenter = new SportCenter(sportsCenterID);

        Room room = new Room();
        room.setRoomName(roomName);
        room.setSportCenter(sportCenter);
        room.setId("roomID");
        roomRepository.save(room);

        Course course = new Course();
        course.setName("Yoga");
        course.setRoom(room);
        course.setId("courseID");
        course.setSportCenter(sportCenter);

        CourseSession courseSession = new CourseSession();
        courseSession.setCourse(course);
        courseSession.setId("courseSessionID");

        Course course1 = new Course();
        course1.setName("Pilates");
        course1.setRoom(room);
        course1.setId("courseID1");
        course1.setSportCenter(sportCenter);

        CourseSession courseSession1 = new CourseSession();
        courseSession1.setCourse(course1);
        courseSession1.setId("courseSessionID1");

        Course course2 = new Course();
        course2.setName("Zumba");
        course2.setRoom(room);
        course2.setId("courseID2");
        course2.setSportCenter(sportCenter);

        CourseSession courseSession2 = new CourseSession();
        courseSession2.setCourse(course2);
        courseSession2.setId("courseSessionID2");

        List<CourseSession> courseSessions = List.of(courseSession,courseSession1,courseSession2);
        when(courseSessionRepository.findCourseSessionByCourseRoomId(room.getId())).thenReturn(courseSessions);
        PersonSession personSessionOwner= new PersonSession(personID,PersonSession.PersonType.Owner,sportsCenterID);
        List<CourseSessionDTO> courseSessionsDTOs = roomService.getCourseSessionsPerRoom(new RoomDTO(room));
        assertNotNull(courseSessionsDTOs);
        assertEquals(3,courseSessionsDTOs.size());
        assertEquals(new CourseSessionDTO(courseSession),courseSessionsDTOs.get(0));
        assertEquals(new CourseSessionDTO(courseSession1),courseSessionsDTOs.get(1));
        assertEquals(new CourseSessionDTO(courseSession2),courseSessionsDTOs.get(2));
    }


    @Order(6)
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
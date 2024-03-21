package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.InvoiceDTO;
import ca.mcgill.ecse321.rest.dto.RegistrationDTO;
import ca.mcgill.ecse321.rest.dto.RoomDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Invoice;
import ca.mcgill.ecse321.rest.models.Registration;
import ca.mcgill.ecse321.rest.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private CourseRepository courseRepository;

    public static class RoomMessagePair{
        private final Room room;
        private final String message;
        public RoomMessagePair(Room room, String message) {
            this.room = room;
            this.message = message;
        }
        public Room getRoom() {
            return room;
        }
        public String getMessage() {
            return message;
        }

    }
    public RoomService.RoomMessagePair getRoom(String roomID, PersonSession personSession){
        String message = "";
        Room room = roomRepository.findRoomById(roomID);
        if (personSession.getPersonType() != PersonSession.PersonType.Owner ){
            if (personSession.getSportCenterId().equals(room.getSportCenter().getId())){
                message = "Must be the Owner of the Sport Center";
            }
        }
        else if (room == null){
            message = "Room does not exist";
        }
        return new RoomService.RoomMessagePair(room, message);
    }
    @Transactional
    public String createRoom(RoomDTO roomDTO, PersonSession personSession){
        if (personSession.getPersonType() != PersonSession.PersonType.Owner ){
            return "Must be the Owner of the Sport Center";
        }
        if (!roomDTO.getSportCenter().equals(personSession.getSportCenterId())){
            return "Invalid Sport Center id";
        }
        if (roomDTO.getName().isEmpty()){
            return "Room requires name to be created";
        }
        Room room = new Room();
        room.setRoomName(roomDTO.getName());
        room.setSportCenter(sportCenterRepository.findSportCenterById(roomDTO.getSportCenter()));
        roomRepository.save(room);
        return "";
    }

    @Transactional
    public List<CourseDTO> getCoursesPerRoom(RoomDTO roomDTO, PersonSession personSession){
        List<Course> courses = courseRepository.findCoursesByRoomName(roomDTO.getName());
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course c : courses) {
            courseDTOS.add(new CourseDTO(c));
        }
        return courseDTOS;
    }

    @Transactional
    public String deleteRoom(String roomID, PersonSession personSession) {
        RoomService.RoomMessagePair roomMessagePair = getRoom(roomID,personSession);
        if (roomMessagePair.getMessage().isEmpty()){
            roomRepository.deleteRoomById(roomID);
        }
        return roomMessagePair.getMessage();
    }

    private <T> List<T> toList(Iterable<T> iterable){
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}

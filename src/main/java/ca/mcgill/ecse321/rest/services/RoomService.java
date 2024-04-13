package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.*;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseSessionRepository courseSessionRepository;

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

    /**
     * Get a room - message pair
     * @param roomID the room id
     * @param personSession  for authentication
     * @return a RoomMessagePair object
     */
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

    /**
     * Create a room
     * @param roomDTO the room DTO
     * @param personSession for authentication
     * @return An error message if there was an error
     */
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

    /**
     * For a specific room, get all courses associated to it
     * @param roomDTO the room we seek courses from
     * @return a list of course DTOs associated to this room
     */
    @Transactional
    public List<CourseDTO> getCoursesPerRoom(RoomDTO roomDTO){
        List<Course> courses = courseRepository.findCoursesByRoomId(roomDTO.getId());
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course c : courses) {
            courseDTOS.add(new CourseDTO(c));
        }
        return courseDTOS;
    }

    /**
     * For a specific room, get all course sessions associated to it
     * @param roomDTO the room we seek course sessions from
     * @return a list of course sessions DTOs associated to it
     */
    @Transactional
    public List<CourseSessionDTO> getCourseSessionsPerRoom(RoomDTO roomDTO){
        List<CourseSession> courseSessions = courseSessionRepository.findCourseSessionByCourseRoomId(roomDTO.getId());
        List<CourseSessionDTO> courseSessionDTOS = new ArrayList<>();
        for (CourseSession c : courseSessions) {
            courseSessionDTOS.add(new CourseSessionDTO(c));
        }
        return courseSessionDTOS;
    }

    /**
     * AS the owner, delete a room
     * @param roomDTO the room to delete
     * @param personSession for authentication
     * @return an error message
     */
    @Transactional
    public List<CourseSessionDTO> getCourseSessionsPerRoom(RoomDTO roomDTO, PersonSession personSession){
        List<CourseSession> courseSessions = courseSessionRepository.findCourseSessionByCourseRoomId(roomDTO.getId());
        List<CourseSessionDTO> courseSessionDTOS = new ArrayList<>();
        for (CourseSession c : courseSessions) {
            courseSessionDTOS.add(new CourseSessionDTO());
        }
        return courseSessionDTOS;
    }

    @Transactional
    public String deleteRoom(String roomID, PersonSession personSession) {
        RoomService.RoomMessagePair roomMessagePair = getRoom(roomID,personSession);
        if (roomMessagePair.getMessage().isEmpty()){
            roomRepository.deleteRoomById(roomID);
        }
        return roomMessagePair.getMessage();
    }

    @Transactional
    public List<RoomDTO> getAllRooms(PersonSession personSession){
        if (personSession.getPersonType() == PersonSession.PersonType.Customer ){
            throw new IllegalArgumentException("Must be the Owner of the Sport Center");
        }
        List<Room> rooms = toList(roomRepository.findAll());
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (Room r : rooms) {
            roomDTOS.add(new RoomDTO(r));
        }
        return roomDTOS;
    }


    private <T> List<T> toList(Iterable<T> iterable){
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}

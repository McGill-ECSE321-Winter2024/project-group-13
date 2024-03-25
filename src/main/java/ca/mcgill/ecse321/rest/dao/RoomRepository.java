package ca.mcgill.ecse321.rest.dao;

import ca.mcgill.ecse321.rest.models.Room;
import org.springframework.data.repository.CrudRepository;

/** This is the Room CRUD Repository @Author Mohamed Abdelrahman */
public interface RoomRepository extends CrudRepository<Room, String> {

  Room findRoomById(String id);
  Room findRoomByRoomName(String name);
  void deleteRoomById(String id);
}

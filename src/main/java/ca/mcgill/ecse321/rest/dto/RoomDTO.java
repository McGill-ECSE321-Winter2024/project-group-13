package ca.mcgill.ecse321.rest.dto;
import ca.mcgill.ecse321.rest.models.Room;

public class RoomDTO {
    private String id;
    private String name;
    private String sportCenter;
    public RoomDTO(Room room){
        if (room != null){
            if (room.getId() != null){
                this.id=room.getId();
            }
            if (room.getRoomName() != null){
                this.name=room.getRoomName();
            }
            if (room.getSportCenter() != null){
                this.sportCenter= room.getSportCenter().getId();
            }
        }
    }
    public RoomDTO(String name, String sportCenterID){
        this.name = name;
        this.sportCenter = sportCenterID;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    // getters
    public String getId() {
        return id;
    }
    public String getName() {return name;}

    public String getSportCenter() {
        return sportCenter;
    }

}

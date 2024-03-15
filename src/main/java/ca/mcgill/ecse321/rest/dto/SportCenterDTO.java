package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.*;
import jakarta.persistence.OneToOne;

public class SportCenterDTO {
    private String id;

    private String address;
    private String name;
    private Schedule schedule;

    public SportCenterDTO(SportCenter sportCenter){
        this.id=sportCenter.getId();
        this.address=sportCenter.getAddress();
        this.name=sportCenter.getName();
        this.schedule=sportCenter.getSchedule();
    }

    public String getId() {
        return id;
    }
    public String getAddress(){return address;}
    public String getName() {return name;}
    public Schedule getSchedule(){return schedule;}
}

package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.*;


public class SportCenterDTO {
    private String id;

    private String address;
    private String name;
    private ScheduleDTO schedule;

    public SportCenterDTO(SportCenter sportCenter){
        this.id=sportCenter.getId();
        this.address=sportCenter.getAddress();
        this.name=sportCenter.getName();
        this.schedule= new ScheduleDTO(sportCenter.getSchedule());
    }
    public SportCenterDTO(){
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAddress(){return address;}

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public ScheduleDTO getSchedule(){return schedule;}
    public void setSchedule(ScheduleDTO schedule){this.schedule=schedule;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SportCenterDTO sportCenterDTO = (SportCenterDTO) obj;
        return (id==sportCenterDTO.getId()||id.equals(sportCenterDTO.getId())) &&
                (address==sportCenterDTO.getAddress()||address.equals(sportCenterDTO.getAddress())) &&
                (name==sportCenterDTO.getName()||name.equals(sportCenterDTO.getName())) &&
                (schedule==sportCenterDTO.getSchedule()||schedule.equals(sportCenterDTO.getSchedule()));
    }
}

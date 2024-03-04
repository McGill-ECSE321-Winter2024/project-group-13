package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.*;
import jakarta.persistence.*;

import java.sql.Timestamp;

public class CourseDTO {
    private String id;
    private String name;

    private String description;
    private String level;

    private Timestamp courseStartDate;

    private Timestamp courseEndDate;

    private String roomName;
    private SportCenterDTO sportCenterDTO;
    private InstructorDTO instructorDTO;
    private ScheduleDTO scheduleDTO;
    private Double hourlyRateAmount;
    private String courseState;

    public CourseDTO(String id,String name,String description,Timestamp courseStartDate,Timestamp courseEndDate, String roomName,SportCenterDTO sportCenterDTO,InstructorDTO instructorDTO, ScheduleDTO scheduleDTO, Double hourlyRateAmount, String courseState){
        this.id=id;
        this.name=name;
        this.description=description;
        this.courseStartDate=courseStartDate;
        this.courseEndDate=courseEndDate;
        this.roomName=roomName;
        this.sportCenterDTO=sportCenterDTO;
        this.instructorDTO= instructorDTO;
        this.scheduleDTO=scheduleDTO;
        this.hourlyRateAmount=hourlyRateAmount;
        this.courseState=courseState;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLevel() {
        return level;
    }

    public Timestamp getCourseStartDate() {
        return courseStartDate;
    }

    public Timestamp getCourseEndDate() {
        return courseEndDate;
    }

    public String getRoomName() {
        return roomName;
    }

    public SportCenterDTO getSportCenterDTO() {
        return sportCenterDTO;
    }

    public InstructorDTO getInstructorDTO() {
        return instructorDTO;
    }

    public ScheduleDTO getScheduleDTO() {
        return scheduleDTO;
    }

    public Double getHourlyRateAmount() {
        return hourlyRateAmount;
    }
    public String getCourseState() {
        return courseState;
    }
}

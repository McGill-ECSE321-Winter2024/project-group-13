package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
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

    private String room;
    private String sportCenter;
    private String instructor;
    private String schedule;
    private Double hourlyRateAmount;
    private String courseState;

    public CourseDTO(Course course){
        this.id=course.getId();
        this.name=course.getName();
        this.description=course.getDescription();
        this.courseStartDate=course.getCourseStartDate();
        this.courseEndDate=course.getCourseEndDate();
        this.room=course.getRoom().getId();
        this.sportCenter= course.getSportCenter().getId();
        this.instructor=course.getInstructor().getId();
        this.schedule= course.getSchedule().getId();
        this.hourlyRateAmount=course.getHourlyRateAmount();
        this.courseState=course.getCourseState().toString();
        this.level=course.getLevel().toString();

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
    public String getRoom() {
        return room;
    }
    public String getSportCenter() {
        return sportCenter;
    }
    public String getInstructor() {
        return instructor;
    }
    public String getSchedule() {
        return schedule;
    }
    public Double getHourlyRateAmount() {
        return hourlyRateAmount;
    }
    public String getCourseState() {
        return courseState;
    }


}

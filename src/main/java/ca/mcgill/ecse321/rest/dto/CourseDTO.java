package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.*;

import java.sql.Timestamp;

public class CourseDTO {
    private String id;
    private String name;

    private String description;
    private String level;

    private Timestamp courseStartDate;

    private Timestamp courseEndDate;

    private String room;
    private RoomDTO roomDTO;
    private String sportCenter;
    private String instructor;
    private String schedule;
    private Double hourlyRateAmount;
    private String courseState;

    public CourseDTO(){
    }
    public CourseDTO(Course course){
        if (course != null){
            if (course.getId() != null){
                this.id=course.getId();
            }
            if (course.getName() != null){
                this.name=course.getName();
            }
            if (course.getSportCenter() != null){
                this.sportCenter= course.getSportCenter().getId();
            }
            if (course.getDescription() != null){
                this.description=course.getDescription();
            }
            if (course.getCourseStartDate() != null){
                this.courseStartDate=course.getCourseStartDate();
            }
            if (course.getCourseEndDate() != null){
                this.courseEndDate=course.getCourseEndDate();
            }
            if (course.getInstructor() != null){
                this.instructor=course.getInstructor().getId();
            }
            if (course.getRoom() != null){
                this.room=course.getRoom().getId();
                this.roomDTO= new RoomDTO(course.getRoom());
            }
            if (course.getSchedule() != null){
                this.schedule= course.getSchedule().getId();
            }
            if (course.getHourlyRateAmount() != null){
                this.hourlyRateAmount=course.getHourlyRateAmount();
            }
            if (course.getCourseState() != null){
                this.courseState=course.getCourseState().toString();
            }
            if (course.getLevel() != null){
                this.level=course.getLevel().toString();
            }
        }
    }
    public CourseDTO(String name, String sportCenterID){
        this.name=name;
        this.sportCenter= sportCenterID;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CourseDTO)) {
            return false;
        }
        CourseDTO courseDTO = (CourseDTO) obj;
        return (id.equals(courseDTO.getId())||id==courseDTO.getId()) &&
                (name.equals(courseDTO.getName())||name==courseDTO.getName()) &&
                (sportCenter.equals(courseDTO.getSportCenter())||sportCenter==courseDTO.getSportCenter());
    }

    public RoomDTO getRoomDTO() {
        return roomDTO;
    }
}

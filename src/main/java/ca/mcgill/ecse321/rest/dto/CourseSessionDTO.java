package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.CourseSession;


import java.sql.Timestamp;

public class CourseSessionDTO {
    private String id;
    private Timestamp startTime;
    private Timestamp endTime;
    private String course;

    public CourseSessionDTO(){
    }
    public CourseSessionDTO(CourseSession courseSession){
        if (courseSession != null) {
          this.id = courseSession.getId();
          this.startTime = courseSession.getStartTime();
          this.endTime = courseSession.getEndTime();
          this.course = courseSession.getCourse().getId();
        }
    }
    public String getId() {
        return id;
    }
    public Timestamp getStartTime() {
        return startTime;
    }
    public Timestamp getEndTime() {
        return endTime;
    }

    public String getCourse() {
        return course;
    }

    public void setId(String sessionID) {
        this.id = sessionID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CourseSessionDTO)) {
            return false;
        }
        CourseSessionDTO courseSessionDTO = (CourseSessionDTO) obj;
        return (id==courseSessionDTO.getId()||id.equals(courseSessionDTO.getId())) &&
                (startTime==courseSessionDTO.getStartTime()||startTime.equals(courseSessionDTO.getStartTime())) &&
                (endTime==courseSessionDTO.getEndTime()||endTime.equals(courseSessionDTO.getEndTime())) &&
                (course==courseSessionDTO.getCourse()||course.equals(courseSessionDTO.getCourse()));
    }
}

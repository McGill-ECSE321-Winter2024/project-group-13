package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.CourseSession;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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

}

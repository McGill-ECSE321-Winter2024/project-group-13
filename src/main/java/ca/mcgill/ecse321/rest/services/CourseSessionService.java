package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.CourseSessionRepository;
import ca.mcgill.ecse321.rest.dao.ScheduleRepository;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CourseSessionDTO;
import ca.mcgill.ecse321.rest.models.CourseSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class CourseSessionService {
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private CourseSessionRepository courseSessionRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Transactional
  public String createSessionsPerCourse(String courseID, PersonSession personSession) {
      return "";
  }
  @Transactional
  public String createCourseSession(String courseID, PersonSession personSession) {
      CourseSession courseSession=new CourseSession();
      courseSession.setCourse(courseRepository.findCourseById(courseID));
      if (personSession.getPersonType()!= PersonSession.PersonType.Owner ){
          return "Must be an owner";
      }
      if (courseSession.getCourse()==null){
          return "Course not found";
      }
      if (!personSession.getSportCenterId().equals(courseSession.getCourse().getSportCenter().getId())){
          return "Session must belong to the same sports center";
      }
      courseSessionRepository.save(courseSession);
      return "";
  }
    @Transactional
    public String updateCourseSessionStart(String courseSessionID, Timestamp startTime, PersonSession personSession) {
        CourseSession courseSession=courseSessionRepository.findCourseSessionById(courseSessionID);
        if (courseSession==null){
            return "Course session not found";
        }
        CourseDTO courseDTO =new CourseDTO(courseRepository.findCourseById(courseSession.getCourse().getId()));
        if (personSession.getPersonType()!= PersonSession.PersonType.Owner ){
            return "Must be an owner";
        }

        if (!personSession.getSportCenterId().equals(courseDTO.getSportCenter())){
            return "Session must belong to the same sports center";
        }
        if (courseSession.getEndTime()!= null && startTime!= null && startTime.before(courseSession.getEndTime()) && courseSession.setStartTime(startTime)){
            courseSessionRepository.save(courseSession);
            return "";
        }
        return "Invalid Start time";
    }
    @Transactional
    public String updateCourseSessionEnd(String courseSessionID,Timestamp endTime, PersonSession personSession) {
        CourseSession courseSession=courseSessionRepository.findCourseSessionById(courseSessionID);
        if (courseSession==null){
            return "Course session not found";
        }
        CourseDTO courseDTO =new CourseDTO(courseRepository.findCourseById(courseSession.getCourse().getId()));
        if (personSession.getPersonType()!= PersonSession.PersonType.Owner ){
            return "Must be an owner";
        }

        if (!personSession.getSportCenterId().equals(courseDTO.getSportCenter())){
            return "Session must belong to the same sports center";
        }
        if (courseSession.getStartTime()!= null && endTime!= null && endTime.after(courseSession.getEndTime()) && courseSession.setStartTime(startTime)){
            courseSessionRepository.save(courseSession);
            return "";
        }
        return "Invalid Start time";
    }
  @Transactional
  public String deleteCourseSession(String courseSessionID, PersonSession personSession) {
      CourseSessionDTO courseSessionDTO=new CourseSessionDTO(courseSessionRepository.findCourseSessionById(courseSessionID));
      CourseDTO courseDTO =new CourseDTO(courseRepository.findCourseById(courseSessionDTO.getCourse()));
      if (personSession.getPersonType()!= PersonSession.PersonType.Owner ){
          return "Must be an owner";
      }
      if (courseSessionDTO.getId()==null){
          return "Course session not found";
      }
      if (!personSession.getSportCenterId().equals(courseDTO.getSportCenter())){
          return "Session must belong to the same sports center";
      }
      courseSessionRepository.deleteById(courseSessionID);
      return "";
  }

}
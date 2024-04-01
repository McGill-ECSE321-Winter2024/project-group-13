package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.CourseRepository;
import ca.mcgill.ecse321.rest.dao.CourseSessionRepository;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.CourseSession;
import ca.mcgill.ecse321.rest.models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.InvalidKeyException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class CourseSessionService {
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private CourseSessionRepository courseSessionRepository;

  private Timestamp combineDateWithTime(Timestamp currentDate, Time time) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(currentDate.getTime());
      calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
      calendar.set(Calendar.MINUTE, time.getMinutes());
      calendar.set(Calendar.SECOND, time.getSeconds());
      calendar.set(Calendar.MILLISECOND, 0); // Not included in Time

      return new Timestamp(calendar.getTimeInMillis());
  }


  public String createSessionsPerCourse(String courseID) {
    Course course = courseRepository.findCourseById(courseID);
    if (course == null) {
      return "Invalid course id";
    }

    Schedule schedule = course.getSchedule();
    if (course.getSchedule() == null){
        return "Schedule not found";
    }
    Timestamp startDate = course.getCourseStartDate();
    Timestamp endDate = course.getCourseEndDate();

    // Retrieve start and end times from the schedule for the current day
    Time startTime = null; // Assign start time based on the current day of the week
    Time endTime = null; // Assign end time based on the current day of the week

    // Determine the current day of the week
    Calendar currentDay = Calendar.getInstance();
    currentDay.setTimeInMillis(startDate.getTime());

    while (currentDay.getTime().before(endDate) || currentDay.getTime().equals(endDate)) {
      // Retrieve start and end times for the current day of the week
        endTime = switch (currentDay.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> {
                startTime = schedule.getMondayStart();
                yield schedule.getMondayEnd();
            }
            case Calendar.TUESDAY -> {
                startTime = schedule.getTuesdayStart();
                yield schedule.getTuesdayEnd();
            }
            case Calendar.WEDNESDAY -> {
                startTime = schedule.getWednesdayStart();
                yield schedule.getWednesdayEnd();
            }
            case Calendar.THURSDAY -> {
                startTime = schedule.getThursdayStart();
                yield schedule.getThursdayEnd();
            }
            case Calendar.FRIDAY -> {
                startTime = schedule.getFridayStart();
                yield schedule.getFridayEnd();
            }
            case Calendar.SATURDAY -> {
                startTime = schedule.getSaturdayStart();
                yield schedule.getSaturdayEnd();
            }
            case Calendar.SUNDAY -> {
                startTime = schedule.getSundayStart();
                yield schedule.getSundayEnd();
            }
            default -> endTime;
        };

      // Combine date with start and end times to create session start and end times
      Timestamp currentDate = new Timestamp(currentDay.getTimeInMillis());
      if (startTime!=null && endTime!=null){
          Timestamp sessionStartTime = combineDateWithTime(currentDate, startTime);
          Timestamp sessionEndTime = combineDateWithTime(currentDate, endTime);

          // Create and save the course session
          CourseSession session = new CourseSession();
          session.setStartTime(sessionStartTime);
          session.setEndTime(sessionEndTime);
          courseSessionRepository.save(session);
      }
      // Move to the next day
      currentDay.add(Calendar.DAY_OF_MONTH, 1);
    }

    return "";
    }

  public CourseSession getCourseSession(String courseSessionID) {
      CourseSession courseSession= courseSessionRepository.findCourseSessionById(courseSessionID);
      if (courseSession==null){
          throw new InvalidKeyException("Course session not found");
      }
      return courseSession;
  }

  public String deleteSessionsPerCourse(String courseID) {
      Course course= courseRepository.findCourseById(courseID);
      if (course==null){
          return "Invalid course id";
      }
      courseSessionRepository.deleteAllByCourseId(courseID);
      return "";
  }

  public List<CourseSession> getSessionsPerCourse(String courseID, PersonSession personSession) {
      Course course= courseRepository.findCourseById(courseID);
      if (course==null){
          throw new IllegalArgumentException("Invalid course id");
      }
      return courseSessionRepository.findCourseSessionsByCourse(course);
    }

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

  public String updateCourseSessionStart(String courseSessionID, Timestamp startTime, PersonSession personSession) {
      CourseSession courseSession=courseSessionRepository.findCourseSessionById(courseSessionID);
      if (courseSession==null){
          return "Course session not found";
      }
      Course course = courseRepository.findCourseById(courseSession.getCourse().getId());
      CourseDTO courseDTO =new CourseDTO(courseRepository.findCourseById(courseSession.getCourse().getId()));
      if (personSession.getPersonType()!= PersonSession.PersonType.Owner ){
          return "Must be an owner";
      }

      if (!personSession.getSportCenterId().equals(course.getSportCenter().getId())){
          return "Session must belong to the same sports center";
      }
      if (startTime!= null){
          if (courseSession.getEndTime()!= null && startTime.after(courseSession.getEndTime())){
              return "Start time must be before end time";
          }
          courseSessionRepository.save(courseSession);
          return "";
      }
      return "Invalid Start time";
  }

  public String updateCourseSessionEnd(String courseSessionID,Timestamp endTime, PersonSession personSession) {
      CourseSession courseSession=courseSessionRepository.findCourseSessionById(courseSessionID);
      if (courseSession==null){
          return "Course session not found";
      }
      Course course = courseRepository.findCourseById(courseSession.getCourse().getId());
      if (personSession.getPersonType()!= PersonSession.PersonType.Owner){
          return "Must be an owner";
      }
      if (!personSession.getSportCenterId().equals(course.getSportCenter().getId())){
          return "Session must belong to the same sports center";
      }
      if (endTime!= null){
          if (courseSession.getStartTime()!= null && endTime.before(courseSession.getStartTime())){
              return "End time must be after start time";
          }
          courseSessionRepository.save(courseSession);
          return "";
      }
      return "Invalid end time";
  }

  public String deleteCourseSession(String courseSessionID, PersonSession personSession) {
      CourseSession courseSession = courseSessionRepository.findCourseSessionById(courseSessionID);
      Course course = courseRepository.findCourseById(courseSession.getCourse().getId());
      if (personSession.getPersonType()!= PersonSession.PersonType.Owner){
          return "Must be an owner";
      }
      if (courseSession.getId()==null){
          return "Course session not found";
      }
      if (!personSession.getSportCenterId().equals(course.getSportCenter().getId())){
          return "Session must belong to the same sports center";
      }
      courseSessionRepository.deleteById(courseSessionID);
      return "";
  }

}
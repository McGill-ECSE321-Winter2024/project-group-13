package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;

    public static class CourseMessagePair{
        private final Course course;
        private final String message;
        public CourseMessagePair(Course course, String message) {
            this.course = course;
            this.message = message;
        }
        public Course getCourse() {
            return course;
        }

        public String getMessage() {
            return message;
        }

    }

    public CourseMessagePair getCourse(String course_id, PersonSession personSession){
        String message="";
        Course course = courseRepository.findCourseById(course_id);
        if (personSession.getPersonType()!= PersonSession.PersonType.Owner ){
            if (personSession.getSportCenterId().equals(course.getSportCenter().getId())){
                message= "Must be an owner of the course's sports center";
            }
        }
        else if (course== null){
            message= "Course does not exist";
        }
        return new CourseMessagePair(course,message);
    }

    @Transactional
    public String createCourse(CourseDTO courseDTO, PersonSession personSession){
        if (personSession.getPersonType()== PersonSession.PersonType.Customer ){
            return "Must be an owner or instructor";
        }
        if (!courseDTO.getSportCenter().equals(personSession.getSportCenterId())){
            return "Invalid sport's center id";
        }
        if (courseDTO.getName().isEmpty()){
            return "Course requires name to be created";
        }
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setSportCenter(sportCenterRepository.findSportCenterById(courseDTO.getSportCenter()));
        course.setCourseState(Course.CourseState.AwaitingApproval);
        courseRepository.save(course);
        return "";
    }
    @Transactional
    public String approveCourse(String course_id, PersonSession personSession) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        if (courseMessagePair.getMessage().isEmpty()) {
          Course course = courseMessagePair.getCourse();
          course.setCourseState(Course.CourseState.Approved);
          courseRepository.save(course);
        }
        return courseMessagePair.getMessage() ;
    }
    @Transactional
    public String updateCourseName(PersonSession personSession,String course_id,String name) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        if (courseMessagePair.getMessage().isEmpty() && !name.isBlank()){
            course.setName(name);
            courseRepository.save(course);
        }
        else return "Name must not be null";
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseDescription(PersonSession personSession, String course_id,String description) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        if (courseMessagePair.getMessage().isEmpty() && !description.isBlank()){
            course.setDescription(description);
            courseRepository.save(course);
        }
        else return "Description must not be null";
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseLevel(PersonSession personSession, String course_id, String level) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        if (courseMessagePair.getMessage().isEmpty()){
            if(level.equals("Beginner") || level.equals("Intermediate") || level.equals("Advanced")){
                course.setLevel(level);
                courseRepository.save(course);
            }
            else {
                return "Invalid level";
            }
        }
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseRate(PersonSession personSession,String course_id, Double hourlyRateAmount) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        if (courseMessagePair.getMessage().isEmpty()){
          if (hourlyRateAmount >= 0) {
            course.setHourlyRateAmount(hourlyRateAmount);
            courseRepository.save(course);
          } else return "Course rate must be a positive number";
        }
        return courseMessagePair.getMessage();

    }
    @Transactional
    public String updateCourseStartDate(PersonSession personSession,String course_id, Timestamp courseStartDate) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        if (courseMessagePair.getMessage().isEmpty()){
          if (course.getCourseEndDate().after(courseStartDate)) {
            course.setCourseStartDate(courseStartDate);
            courseRepository.save(course);
          }
          else return "Course start date must be before course end date";
        }
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseEndDate(PersonSession personSession, String course_id, Timestamp courseEndDate) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        if (courseMessagePair.getMessage().isEmpty()){
          if (course.getCourseStartDate().before(courseEndDate)) {
            course.setCourseEndDate(courseEndDate);
            courseRepository.save(course);
          } else return "Course end date must be after course start date";
        }
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseRoom(PersonSession personSession,String course_id, String roomID) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        Room room = roomRepository.findRoomById(roomID);
        if (courseMessagePair.getMessage().isEmpty()){
            if (room==null)
                return "Room not found";
            course.setRoom(room);
            courseRepository.save(course);
        }
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseInstructor(PersonSession personSession,String course_id, String instructorID) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        Instructor instructor= instructorRepository.findInstructorById(instructorID);
        if (courseMessagePair.getMessage().isEmpty()){
            if (instructor==null ){
                return "Instructor not found";
            }
            course.setInstructor(instructor);
            courseRepository.save(course);
        }
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseSchedule(PersonSession personSession,String course_id, String scheduleID) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        Schedule schedule= scheduleRepository.findScheduleById(scheduleID);
        if (courseMessagePair.getMessage().isEmpty()){
            if (schedule==null)
                return "Schedule not found";
            course.setSchedule(schedule);
            courseRepository.save(course);
        }
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String deleteCourse(PersonSession personSession,String course_id) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        if (courseMessagePair.getMessage().isEmpty()){
            courseRepository.deleteCourseById(course_id);
        }
        return courseMessagePair.getMessage();
    }
}

package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;


@Service
public class CourseService {
    @Autowired private CourseRepository courseRepository;
    @Autowired private InstructorRepository instructorRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SportCenterRepository sportCenterRepository;
    @Autowired private CourseSessionRepository courseSessionRepository;

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
        if (personSession.getPersonType()== PersonSession.PersonType.Customer ){
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
    public String createCourse(String name, PersonSession personSession){
        if (personSession.getPersonType()== PersonSession.PersonType.Customer ){
            return "Must be an owner or instructor";
        }
        if (name== null || name.isEmpty()){
            return "Course requires name to be created";
        }
        Course course = new Course();
        course.setName(name);
        course.setSportCenter(sportCenterRepository.findSportCenterById(personSession.getSportCenterId()));
        if (course.getSportCenter()==null){
            return "Invalid sport's center id";
        }
        course.setCourseState(Course.CourseState.AwaitingApproval);
        courseRepository.save(course);
        return "--ID: "+course.getId();
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
        if (courseMessagePair.getMessage().isEmpty() && !name.isEmpty()){
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
                return "Requires valid level";
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
          if (course.getCourseEndDate()== null || course.getCourseEndDate().after(courseStartDate)) {
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
          if (course.getCourseStartDate()== null || course.getCourseStartDate().before(courseEndDate)) {
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
            if (room==null) return "Room not found";
            course.setRoom(room);
            courseRepository.save(course);
        }
        return courseMessagePair.getMessage();
    }
    @Transactional
    public String updateCourseInstructor(PersonSession personSession,String course_id, String instructorID) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        System.out.println(instructorID);
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

    public String updateCourseState(String courseId, String newState, PersonSession person) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return "Course not found";
        }
        if (!person.getPersonType().equals(PersonSession.PersonType.Owner)) {
            return "Must be an owner of the course's sports center to change state";
        }
        try {
            course.setCourseState(Course.CourseState.valueOf(newState));
            courseRepository.save(course);
            return ""; // Empty string signifies success
        } catch (IllegalArgumentException e) {
            return "Invalid state provided";
        }
    }



    @Transactional
    public String addOrUpdateCourseScheduleAndSessions(String course_id, ScheduleDTO scheduleDTO, PersonSession personSession) {
        // First, verify access to the course
        CourseMessagePair courseMessagePair = getCourse(course_id, personSession);
        if (!courseMessagePair.getMessage().isEmpty()) {
            return courseMessagePair.getMessage();
        }
        Course course = courseMessagePair.getCourse();

        // Check if the schedule ID is provided and valid
        Schedule schedule = null;
        if (scheduleDTO.getId() != null && !scheduleDTO.getId().trim().isEmpty()) {
            schedule = scheduleRepository.findScheduleById(scheduleDTO.getId());
        }

        // If schedule is not found, create a new one
        if (schedule == null) {
            schedule = new Schedule();
        }

        // Set or update schedule fields from DTO
        try {
            updateScheduleFromDTO(schedule, scheduleDTO);
        } catch (IllegalArgumentException e) {
            return "Invalid time format provided for one or more schedule fields";
        }

        // Save the updated or new schedule
        scheduleRepository.save(schedule);

        // Link the schedule to the course
        course.setSchedule(schedule);
        courseRepository.save(course);



        // Generate course sessions based on the schedule
        if (!generateCourseSessions(course, schedule)) {
            return "Failed to generate course sessions";
        }

        return ""; // Empty string indicates success
    }

    private boolean generateCourseSessions(Course course, Schedule schedule) {
        if (course.getCourseStartDate() == null || course.getCourseEndDate() == null) {
            return false; // Cannot generate sessions without start and end dates
        }
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(course.getCourseStartDate().getTime());

        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(course.getCourseEndDate().getTime());

        while (!startCal.after(endCal)) {
            int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
            // Assuming the Schedule class has methods like getDayStartTime() which returns a Time object for the start time
            Time startTime = getStartTimeForDay(schedule, dayOfWeek);
            if (startTime != null) {
                Time endTime = getEndTimeForDay(schedule, dayOfWeek);
                Timestamp sessionStart = new Timestamp(startCal.getTimeInMillis() + startTime.getTime());
                Timestamp sessionEnd = new Timestamp(startCal.getTimeInMillis() + endTime.getTime());

                CourseSession session = new CourseSession();
                session.setStartTime(sessionStart);
                session.setEndTime(sessionEnd);
                session.setCourse(course);
                courseSessionRepository.save(session);
            }
            startCal.add(Calendar.DATE, 1); // Move to the next day
        }
        return true;
    }


    private void updateScheduleFromDTO(Schedule schedule, ScheduleDTO scheduleDTO) {
        // Check and set the start and end times for each day from DTO to Schedule
        // Assumes that scheduleDTO getters return time as String in "HH:mm:ss" format
        if (scheduleDTO.getMondayStart() != null) schedule.setMondayStart(Time.valueOf(scheduleDTO.getMondayStart()));
        if (scheduleDTO.getMondayEnd() != null) schedule.setMondayEnd(Time.valueOf(scheduleDTO.getMondayEnd()));

        if (scheduleDTO.getTuesdayStart() != null) schedule.setTuesdayStart(Time.valueOf(scheduleDTO.getTuesdayStart()));
        if (scheduleDTO.getTuesdayEnd() != null) schedule.setTuesdayEnd(Time.valueOf(scheduleDTO.getTuesdayEnd()));

        if (scheduleDTO.getWednesdayStart() != null) schedule.setWednesdayStart(Time.valueOf(scheduleDTO.getWednesdayStart()));
        if (scheduleDTO.getWednesdayEnd() != null) schedule.setWednesdayEnd(Time.valueOf(scheduleDTO.getWednesdayEnd()));

        if (scheduleDTO.getThursdayStart() != null) schedule.setThursdayStart(Time.valueOf(scheduleDTO.getThursdayStart()));
        if (scheduleDTO.getThursdayEnd() != null) schedule.setThursdayEnd(Time.valueOf(scheduleDTO.getThursdayEnd()));

        if (scheduleDTO.getFridayStart() != null) schedule.setFridayStart(Time.valueOf(scheduleDTO.getFridayStart()));
        if (scheduleDTO.getFridayEnd() != null) schedule.setFridayEnd(Time.valueOf(scheduleDTO.getFridayEnd()));

        if (scheduleDTO.getSaturdayStart() != null) schedule.setSaturdayStart(Time.valueOf(scheduleDTO.getSaturdayStart()));
        if (scheduleDTO.getSaturdayEnd() != null) schedule.setSaturdayEnd(Time.valueOf(scheduleDTO.getSaturdayEnd()));

        if (scheduleDTO.getSundayStart() != null) schedule.setSundayStart(Time.valueOf(scheduleDTO.getSundayStart()));
        if (scheduleDTO.getSundayEnd() != null) schedule.setSundayEnd(Time.valueOf(scheduleDTO.getSundayEnd()));
    }

    private Time getStartTimeForDay(Schedule schedule, int dayOfWeek) {
        switch(dayOfWeek) {
            case Calendar.MONDAY:
                return schedule.getMondayStart();
            case Calendar.TUESDAY:
                return schedule.getTuesdayStart();
            case Calendar.WEDNESDAY:
                return schedule.getWednesdayStart();
            case Calendar.THURSDAY:
                return schedule.getThursdayStart();
            case Calendar.FRIDAY:
                return schedule.getFridayStart();
            case Calendar.SATURDAY:
                return schedule.getSaturdayStart();
            case Calendar.SUNDAY:
                return schedule.getSundayStart();
            default:
                return null; // If dayOfWeek is not valid
        }
    }

    private Time getEndTimeForDay(Schedule schedule, int dayOfWeek) {
        switch(dayOfWeek) {
            case Calendar.MONDAY:
                return schedule.getMondayEnd();
            case Calendar.TUESDAY:
                return schedule.getTuesdayEnd();
            case Calendar.WEDNESDAY:
                return schedule.getWednesdayEnd();
            case Calendar.THURSDAY:
                return schedule.getThursdayEnd();
            case Calendar.FRIDAY:
                return schedule.getFridayEnd();
            case Calendar.SATURDAY:
                return schedule.getSaturdayEnd();
            case Calendar.SUNDAY:
                return schedule.getSundayEnd();
            default:
                return null; // If dayOfWeek is not valid
        }
    }



}

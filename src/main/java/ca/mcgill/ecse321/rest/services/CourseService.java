package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseSessionRepository courseSessionRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    public static class CourseMessagePair{
        private Course course;
        private String message;
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
        if (personSession.getPersonType()!= PersonSession.PersonType.Owner){
            Owner owner=ownerRepository.findOwnerById(personSession.getPersonId());
            if (owner ==null || !owner.getSportCenter().getId().equals(course.getSportCenter().getId())){
                message= "Must be an owner of the course's sports center";
            }
        }
        else if (course== null){
            message= "Invalid Course";
        };
        return new CourseMessagePair(course,message);
    }

    @Transactional
    public Course createCourse(CourseDTO courseDTO){
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setLevel(courseDTO.getLevel());
        course.setCourseStartDate(courseDTO.getCourseStartDate());
        course.setCourseEndDate(courseDTO.getCourseEndDate());
        course.setHourlyRateAmount(courseDTO.getHourlyRateAmount());
        course.setInstructor(instructorRepository.findInstructorById(courseDTO.getInstructor()));
        course.setRoom(roomRepository.findRoomById(courseDTO.getRoom()));
        course.setSchedule(scheduleRepository.findScheduleById(courseDTO.getSchedule()));
        course.setSportCenter(sportCenterRepository.findSportCenterById(courseDTO.getId()));
        course.setCourseState(Course.CourseState.AwaitingApproval);
        courseRepository.save(course);
        return course;
    }
    @Transactional
    public String approveCourse(String course_id, PersonSession personSession) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        course.setCourseState(Course.CourseState.Approved);
        courseRepository.save(course);
        return courseMessagePair.getMessage() ;
    }
    @Transactional
    public String updateCourseName(PersonSession personSession,String course_id,String name) {
        CourseMessagePair courseMessagePair=getCourse(course_id,personSession);
        Course course=courseMessagePair.getCourse();
        course.setName(name);
        courseRepository.save(course);
        return courseMessagePair.getMessage();
    }
    @Transactional
    public boolean updateCourseDescription(String id,String description) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setDescription(description);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseLevel(String id, Course.Level level) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setLevel(level);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseRate(String id, Double hourlyRateAmount) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setHourlyRateAmount(hourlyRateAmount);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseStartDate(String id, Timestamp courseStartDate) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setCourseStartDate(courseStartDate);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseEndDate(String id, Timestamp courseEndDate) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setCourseEndDate(courseEndDate);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseRoom(String id, Room room) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setRoom(room);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseInstructor(String id, Instructor instructor) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setInstructor(instructor);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseSchedule(String id, Schedule schedule) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setSchedule(schedule);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean deleteCourse(String id, Owner owner) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null && course.getSportCenter().equals(owner.getSportCenter())){
            courseRepository.deleteCourseById(id);
            return true;
        }
        return false;
    }
}

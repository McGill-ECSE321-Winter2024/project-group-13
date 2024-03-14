package ca.mcgill.ecse321.rest.services;

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
    public boolean approveCourse(String id,Owner owner) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null && course.getSportCenter().equals(owner.getSportCenter())){
            course.setCourseState(Course.CourseState.Approved);
            courseRepository.save(course);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateCourseName(String id,String name) {
        Course course = courseRepository.findCourseById(id);
        if (course!= null){
            course.setName(name);
            courseRepository.save(course);
            return true;
        }
        return false;
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

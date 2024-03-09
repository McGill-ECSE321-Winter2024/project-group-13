package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Schedule;
import ca.mcgill.ecse321.rest.services.CourseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseDetailRestController {
    @Autowired
    private CourseDetailService courseDetailService;

    @GetMapping("/courses/{course_id}/schedule")
    Schedule getSchedule(@PathVariable("course_id") String courseId){
        return courseDetailService.getSchedule(courseId);
    }

    @GetMapping("/courses")
    List<Course> getAllCourses(){
        return courseDetailService.getAllCourses();
    }

    @GetMapping("/courses/{course_id}")
    Course getCourse(@PathVariable("course_id") String courseId){
        return courseDetailService.getSpecificCourse(courseId);
    }

    @GetMapping("/courses/{course_id}/customers")
    List<Customer> getCustomers(@PathVariable("course_id") String courseId){
        return courseDetailService.getCustomers(courseId);
    }


}


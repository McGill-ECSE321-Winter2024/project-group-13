package ca.mcgill.ecse321.rest.controllers;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
import ca.mcgill.ecse321.rest.dto.ScheduleDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Schedule;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class CourseDetailController {
    @Autowired
    private CourseDetailService courseDetailService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/courses/{course_id}/schedule")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable("course_id") String courseId,
                                                   @RequestHeader("Authorization") String bearerToken) {
        try {
            // Verify the token and get user details
            PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

            Course course = courseDetailService.getSpecificCourse(courseId);
            // Check for null if the course is not found
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Schedule schedule;

            // Determining access rights based on user type
            switch (personSession.getPersonType()) {
                case Owner:
                    // Owner has access to any schedule
                    schedule = courseDetailService.getSchedule(courseId);
                    break;
                case Instructor:
                    // Instructor has access if they are the instructor of the course or if the course is approved
                    if(courseDetailService.isInstructorOfCourse(courseId, personSession.getPersonId()) || course.getCourseState().equals(Course.CourseState.Approved)){
                        schedule = courseDetailService.getSchedule(courseId);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Instructor does not have access
                    }
                    break;
                case Customer:
                    // Customer has access only if the course is approved
                    if(course.getCourseState().equals(Course.CourseState.Approved)){
                        schedule = courseDetailService.getSchedule(courseId);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Customer does not have access
                    }
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Any other user type is considered unauthorized
            }

            // Check if a schedule was successfully retrieved and return it
            if (schedule != null) {
                ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);
                return ResponseEntity.ok(scheduleDTO);
            } else {
                // If schedule is null, it means the user role did not match any of the cases with access
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (IllegalArgumentException e) {
            // If token verification failed or an illegal argument was provided
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses(@RequestHeader("Authorization") String bearerToken,
                                                         @RequestParam(required = false) String state,
                                                         @RequestParam(required = false) String instructorName,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate) {
        try {
            // Verify the token and get user details
            PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

            Course.CourseState courseState = null;
            if (state != null) {
                courseState = Course.CourseState.valueOf(state);
            }
            Timestamp timestamp = null;
            if(startDate != null){
                timestamp = Timestamp.valueOf(startDate + " 00:00:00");
            }

            // Initially apply filters based on query params, which is most useful for owners
            List<Course> courses = courseDetailService.getCoursesWithFilters(courseState, instructorName, timestamp);

            // For instructors, further filter the initially fetched courses
            if (personSession.getPersonType().equals(PersonSession.PersonType.Instructor)) {
                courses = courses.stream()
                        .filter(course -> course.getInstructor() != null && course.getInstructor().getId().equals(personSession.getPersonId()) ||
                                course.getCourseState() == Course.CourseState.Approved)
                        .collect(Collectors.toList());
            } else if (personSession.getPersonType().equals(PersonSession.PersonType.Customer)) {
                // For customers, filter out only approved courses
                courses = courses.stream()
                        .filter(course -> course.getCourseState() == Course.CourseState.Approved)
                        .collect(Collectors.toList());
            } else if (!personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
                // If the person is neither an owner, instructor, nor customer, forbid access
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Convert to DTOs
            List<CourseDTO> courseDTOS = courses.stream().map(CourseDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(courseDTOS);

        } catch (IllegalArgumentException e) {
            // If token verification failed or an illegal argument was provided
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/courses/{course_id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable("course_id") String courseId,
                                            @RequestHeader("Authorization") String bearerToken) {
        try {
            // Verify the token to ensure it is valid.
            PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);

            Course course = courseDetailService.getSpecificCourse(courseId);
            // Check for null if the course is not found
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            CourseDTO courseDTO = new CourseDTO(course);
            switch (personSession.getPersonType()) {
                case Owner:
                    // For owner, show any course
                    return ResponseEntity.ok(courseDTO);

                case Instructor:
                    // For instructor, show all active courses + courses created by them
                    boolean isCreator = courseDetailService.isInstructorOfCourse(courseId, personSession.getPersonId());
                    if (course.getCourseState().equals(Course.CourseState.Approved) || isCreator) {
                        return ResponseEntity.ok(courseDTO);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }

                case Customer:
                    // For customer, only show active courses
                    if (course.getCourseState().equals(Course.CourseState.Approved)) {
                        return ResponseEntity.ok(courseDTO);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }

                default:
                    // If the user type is not recognized or authorized
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (IllegalArgumentException e) {
            // Handle the case where the token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @GetMapping("/courses/{course_id}/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers(@PathVariable("course_id") String courseId,
                                                          @RequestHeader("Authorization") String bearerToken,
                                                          @RequestParam(required = false) String email,
                                                          @RequestParam(required = false) String name) {
        try {
            PersonSession personSession = authenticationService.verifyTokenAndGetUser(bearerToken);
            Course course = courseDetailService.getSpecificCourse(courseId);
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            boolean isOwner = personSession.getPersonType().equals(PersonSession.PersonType.Owner);
            boolean isInstructorOfCourse = courseDetailService.isInstructorOfCourse(courseId, personSession.getPersonId());

            if(isOwner || isInstructorOfCourse) {
                List<Customer> customers = courseDetailService.getCustomers(courseId, email, name); // Updated to pass email and name
                List<CustomerDTO> customerDTOS = customers.stream().map(CustomerDTO::new).collect(Collectors.toList());
                return ResponseEntity.ok(customerDTOS);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}

package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.dto.SessionDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.models.Owner;
import ca.mcgill.ecse321.rest.services.AuthenticationService;
import ca.mcgill.ecse321.rest.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping(value = { "/courses", "/courses/" })
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(@RequestBody CourseDTO courseDTO) {
        Course createdCourse = courseService.createCourse(courseDTO);
        return new CourseDTO(createdCourse);
    }
    @PostMapping(value = { "/courses/{course_id}/approve", "/courses/{course_id}/approve/" })
    @ResponseStatus(HttpStatus.OK)
    public CourseDTO approveCourse(@PathVariable String course_id, @RequestBody SessionDTO session) {
//        PersonSession session= courseService.verifyTokenAndGetUser();
//        Course approvedCourse = courseService.approveCourse(course_id, owner);
//        return new CourseDTO(approvedCourse);
        return null;
    }

}

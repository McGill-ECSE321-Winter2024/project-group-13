package ca.mcgill.ecse321.rest.controllers;
import ca.mcgill.ecse321.rest.dto.CourseDTO;
import ca.mcgill.ecse321.rest.models.Course;
import ca.mcgill.ecse321.rest.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*")
@RestController
public class CourseController {
    @PostMapping(value = { "/courses", "/courses/" })
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(@RequestBody CourseDTO courseDTO) {
        Course createdCourse = CourseService.createCourse(courseDTO);
        return new CourseDTO(createdCourse);
    }

}

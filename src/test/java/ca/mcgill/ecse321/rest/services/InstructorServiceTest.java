package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.InstructorDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@Nested
@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {
    @Mock
    private SportCenterRepository sportCenterRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructorRepository.deleteAll();
    }



    @Test
    void findAllTest_WithSession() {
        SportCenter sportCenter = new SportCenter("1");
        sportCenter.setAddress("1234 Rue Sainte-Catherine");
        sportCenter.setName("McGill Sports Center");

        Instructor instructor1 = new Instructor();
        instructor1.setId("1");
        instructor1.setName("John Doe");
        instructor1.setPhoneNumber("1234567890");
        instructor1.setEmail("johndoe@gmail.com");
        instructor1.setSportCenter(sportCenter);
        instructor1.setPassword("password1");
        InstructorDTO instructorDTO1 = new InstructorDTO(instructor1);

        Instructor instructor2 = new Instructor();
        instructor2.setId("2");
        instructor2.setName("Jane Doe");
        instructor2.setPhoneNumber("0987654321");
        instructor2.setEmail("janedoe@gmail.com");
        instructor2.setSportCenter(sportCenter);
        instructor2.setPassword("password2");
        InstructorDTO instructorDTO2 = new InstructorDTO(instructor2);

        Instructor instructor3 = new Instructor();
        instructor3.setId("3");
        instructor3.setName("Jack Doe");
        instructor3.setPhoneNumber("2345678901");
        instructor3.setEmail("jackdoe@gmail.com");
        instructor3.setSportCenter(sportCenter);
        instructor3.setPassword("password3");
        InstructorDTO instructorDTO3 = new InstructorDTO(instructor3);

        List<Instructor> instructors = List.of(instructor1, instructor2, instructor3);

        when(instructorRepository.findAll()).thenReturn(instructors);
        PersonSession instructor1Session = new PersonSession(instructor1.getId(), PersonSession.PersonType.Instructor, "1");
        List<InstructorDTO> instructorDTOS = instructorService.findAll(instructor1Session) ;

        assertEquals(3, instructorDTOS.size());
        assertEquals(instructorDTO1, instructorDTOS.get(0));
        assertEquals(instructorDTO2, instructorDTOS.get(1));
        assertEquals(instructorDTO3, instructorDTOS.get(2));
        verify(instructorRepository,times(1)).findAll();
    }

    @Test
    void findAllTest_SessionNull() {
        List<InstructorDTO> instructorDTOS = instructorService.findAll(null) ;
        assertNull(instructorDTOS);
        verify(instructorRepository,times(0)).findAll();
    }

    @Test
    void saveInstructorAsOwnerTest() {
        SportCenter sportCenter = new SportCenter();
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Owner,
                sportCenter.getId()
        );

        Instructor instructor = new Instructor();
        instructor.setName("philippe");
        instructor.setId("instructor_id");
        instructor.setPhoneNumber("1234567890");
        instructor.setEmail("philippe@gmail.com");
        instructor.setSportCenter(sportCenter);
        instructor.setPassword("password");
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        InstructorDTO instructorDTO = instructorService.save(new InstructorDTO(instructor), personSession);

        assertNotNull(instructorDTO);
        assertEquals("instructor_id", instructorDTO.getId());
        assertEquals("philippe", instructorDTO.getName());
        assertEquals("1234567890", instructorDTO.getPhoneNumber());
        assertEquals("philippe@gmail.com", instructorDTO.getEmail());
        assertEquals("password", instructorDTO.getPassword());
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void saveInstructorAsNonOwnerTest() {
        SportCenter sportCenter = new SportCenter();
        PersonSession personSession = new PersonSession(
                "owner_id",
                PersonSession.PersonType.Instructor,
                sportCenter.getId()
        );

        Instructor instructor = new Instructor();
        instructor.setName("John Doe");
        instructor.setId("instructor_id");
        instructor.setPhoneNumber("1234567890");
        instructor.setEmail("johndoe@gmail.com");
        instructor.setSportCenter(sportCenter);
        instructor.setPassword("password");
        InstructorDTO instructorDTO = instructorService.save(new InstructorDTO(instructor), personSession);
        assertNull(instructorDTO);
        verify(instructorRepository, times(0)).save(any(Instructor.class));
    }


}

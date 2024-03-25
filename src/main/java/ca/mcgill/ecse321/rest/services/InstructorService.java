package ca.mcgill.ecse321.rest.services;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.InstructorDTO;
import ca.mcgill.ecse321.rest.models.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;


    public List<InstructorDTO> findAll(PersonSession personSession) {
        if (personSession!=null) {
            List<InstructorDTO> instructorDTOS = new ArrayList<>() ;
            for (Instructor instructor : instructorRepository.findAll()) {
                instructorDTOS.add(new InstructorDTO(instructor));
            }
            return instructorDTOS;
        } else {
            return null;
        }

    }

    public InstructorDTO save(InstructorDTO instructorDTO, PersonSession personSession) {
        if (personSession.getPersonType().equals(PersonSession.PersonType.Owner)) {
            Instructor instructor = convertToEntity(instructorDTO);
            Instructor savedInstructor = instructorRepository.save(instructor);
            return convertToDto(savedInstructor);
        }else {
            return null;
        }
    }

    private InstructorDTO convertToDto(Instructor instructor) {
        return new InstructorDTO(instructor);
    }

    private Instructor convertToEntity(InstructorDTO instructorDTO) {
        Instructor instructor = new Instructor();
        instructor.setName(instructorDTO.getName());
        instructor.setPhoneNumber(instructorDTO.getPhoneNumber());
        instructor.setEmail(instructorDTO.getEmail());
        instructor.setPassword(instructorDTO.getPassword());
        instructor.setSportCenter(sportCenterRepository.findSportCenterById(instructorDTO.getSportCenterId()));
        return instructor;
    }
}

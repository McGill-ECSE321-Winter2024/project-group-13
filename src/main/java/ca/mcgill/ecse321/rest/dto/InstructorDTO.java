package ca.mcgill.ecse321.rest.dto;

import ca.mcgill.ecse321.rest.models.Instructor;

public class InstructorDTO extends PersonDTO{
    private String sportCenterId;

    public InstructorDTO(Instructor instructor) {
        super(instructor);
        this.sportCenterId = instructor.getSportCenter().getId();
    }

    public String getSportCenterId() {
        return sportCenterId;
    }

    public void setSportCenterId(String sportCenterId) {
        this.sportCenterId = sportCenterId;
    }


}


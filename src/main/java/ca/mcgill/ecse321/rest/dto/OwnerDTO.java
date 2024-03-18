package ca.mcgill.ecse321.rest.dto;


import ca.mcgill.ecse321.rest.models.Owner;

public class OwnerDTO extends PersonDTO {
    private String sportCenterId;

    public OwnerDTO(Owner owner) {
        super(owner);
        this.sportCenterId = owner.getSportCenter().getId();
    }

    public String getSportCenterId() {
        return sportCenterId;
    }

    public void setSportCenterId(String sportCenterId) {
        this.sportCenterId = sportCenterId;
    }


}


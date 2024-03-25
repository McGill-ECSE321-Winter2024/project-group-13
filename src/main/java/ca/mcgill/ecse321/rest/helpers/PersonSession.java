package ca.mcgill.ecse321.rest.helpers;

public class PersonSession {


    String personId;

    String sportCenterId;

    PersonType personType;

    public enum PersonType {
        Customer,
        Instructor,
        Owner
    }

    public PersonSession(String personId, PersonType personType, String sportCenterId){
        this.personId = personId;
        this.personType = personType;
        this.sportCenterId = sportCenterId;
    }

    public String getPersonId(){
        return personId;
    }

    public PersonType getPersonType(){
        return personType;
    }

    public String getSportCenterId(){
        return sportCenterId;
    }


}

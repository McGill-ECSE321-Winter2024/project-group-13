package ca.mcgill.ecse321.rest;

public class PersonSession {


    String personId;

    PersonType personType;

    public enum PersonType {
        Customer,
        Instructor,
        Owner
    }

    public PersonSession(String personId, PersonType personType){
        this.personId = personId;
        this.personType = personType;
    }

    public String getPersonId(){
        return personId;
    }

    public PersonType getPersonType(){
        return personType;
    }


}

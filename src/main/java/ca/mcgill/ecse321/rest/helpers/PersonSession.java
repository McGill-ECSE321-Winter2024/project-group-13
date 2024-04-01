package ca.mcgill.ecse321.rest.helpers;

public class PersonSession {


    String personId;

    String personName;
    String personEmail;
    String personPhoneNumber;


    String sportCenterId;

    PersonType personType;

    public enum PersonType {
        Customer,
        Instructor,
        Owner
    }
    public PersonSession(
            String personId,
            PersonType personType,
            String sportCenterId){
        this.personId = personId;
        this.personType = personType;
        this.sportCenterId = sportCenterId;
    }
    public PersonSession(
            String personId,
            String personName,
            String personEmail,
            String personPhoneNumber,
            PersonType personType,
            String sportCenterId){
        this.personId = personId;
        this.personName = personName;
        this.personEmail = personEmail;
        this.personPhoneNumber = personPhoneNumber;
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

    public String getPersonName() {
        return personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public String getPersonPhoneNumber() {
        return personPhoneNumber;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPersonName(String personFirstName) {
        this.personName = personFirstName;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public void setPersonPhoneNumber(String personPhoneNumber) {
        this.personPhoneNumber = personPhoneNumber;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public void setSportCenterId(String sportCenterId) {
        this.sportCenterId = sportCenterId;
    }




}

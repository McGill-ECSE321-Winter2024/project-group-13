package ca.mcgill.ecse321.rest.dto.auth;

import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;

public class SessionDTO extends DefaultHTTPResponse {

    private String session;

    private String personId;
    private String personType;
    private String personName;
    private String personEmail;
    private String personPhoneNumber;

    private String personSportCenterId;

    public SessionDTO(String session, String personId, String personType, String personName, String personEmail, String personPhoneNumber, String sportCenterId) {
        this.session = session;
        this.personId = personId;
        this.personType = personType;
        this.personName = personName;
        this.personEmail = personEmail;
        this.personPhoneNumber = personPhoneNumber;
        this.personSportCenterId = sportCenterId;
    }

    public SessionDTO() {
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonSportCenterId() {
        return personSportCenterId;
    }

    public void setPersonSportCenterId(String sportCenterId) {
        this.personSportCenterId = sportCenterId;
    }


    public void setPersonName(String personName) {
        this.personName = personName;
    }


    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonPhoneNumber() {
        return personPhoneNumber;
    }

    public void setPersonPhoneNumber(String personPhoneNumber) {
        this.personPhoneNumber = personPhoneNumber;
    }




}

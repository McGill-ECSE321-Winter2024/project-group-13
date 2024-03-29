package ca.mcgill.ecse321.rest.dto.auth;

import ca.mcgill.ecse321.rest.helpers.DefaultHTTPResponse;

public class SessionDTO extends DefaultHTTPResponse {

    private String session;

    public SessionDTO(String session) {
        this.session = session;
    }

    public SessionDTO() {
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }



}

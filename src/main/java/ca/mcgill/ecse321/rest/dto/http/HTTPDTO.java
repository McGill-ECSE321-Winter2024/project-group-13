package ca.mcgill.ecse321.rest.dto.http;

public class HTTPDTO {
    private String message;

    public HTTPDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

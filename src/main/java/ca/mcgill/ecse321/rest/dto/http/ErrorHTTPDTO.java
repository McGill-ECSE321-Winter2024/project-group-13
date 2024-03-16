package ca.mcgill.ecse321.rest.dto.http;

public class ErrorHTTPDTO {
    private String message;

    public ErrorHTTPDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

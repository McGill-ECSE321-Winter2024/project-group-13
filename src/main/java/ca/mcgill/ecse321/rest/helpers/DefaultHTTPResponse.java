package ca.mcgill.ecse321.rest.helpers;

import ca.mcgill.ecse321.rest.dto.auth.SessionDTO;
import ca.mcgill.ecse321.rest.dto.http.ErrorHTTPDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DefaultHTTPResponse {
    public static ResponseEntity<ErrorHTTPDTO> unauthorized(String message) {
        return new ResponseEntity<>(new ErrorHTTPDTO(message), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ErrorHTTPDTO> unauthorized() {
        return new ResponseEntity<>(new ErrorHTTPDTO("Unauthorized"), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ErrorHTTPDTO> forbidden(String message) {
        return new ResponseEntity<>(new ErrorHTTPDTO(message), HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<ErrorHTTPDTO> forbidden() {
        return new ResponseEntity<>(new ErrorHTTPDTO("Forbidden"), HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<ErrorHTTPDTO> notFound(String message) {
        return new ResponseEntity<>(new ErrorHTTPDTO(message), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ErrorHTTPDTO> notFound() {
        return new ResponseEntity<>(new ErrorHTTPDTO("Not Found"), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ErrorHTTPDTO> badRequest(String message) {
        return new ResponseEntity<>(new ErrorHTTPDTO(message), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ErrorHTTPDTO> badRequest() {
        return new ResponseEntity<>(new ErrorHTTPDTO("Bad Request"), HttpStatus.BAD_REQUEST);
    }
}

package ca.mcgill.ecse321.rest.helpers;

import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DefaultHTTPResponse {
    public static ResponseEntity<HTTPDTO> unauthorized(String message) {
        return new ResponseEntity<>(new HTTPDTO(message), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<HTTPDTO> unauthorized() {
        return new ResponseEntity<>(new HTTPDTO("Unauthorized"), HttpStatus.UNAUTHORIZED);
    }
    
    public static ResponseEntity<HTTPDTO> unauthorized(String message) {
        return new ResponseEntity<>(new HTTPDTO(message), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<HTTPDTO> unauthorized() {
        return new ResponseEntity<>(new HTTPDTO("Unauthorized"), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<HTTPDTO> forbidden(String message) {
        return new ResponseEntity<>(new HTTPDTO(message), HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<HTTPDTO> forbidden() {
        return new ResponseEntity<>(new HTTPDTO("Forbidden"), HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<HTTPDTO> notFound(String message) {
        return new ResponseEntity<>(new HTTPDTO(message), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<HTTPDTO> notFound() {
        return new ResponseEntity<>(new HTTPDTO("Not Found"), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<HTTPDTO> badRequest(String message) {
        return new ResponseEntity<>(new HTTPDTO(message), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<HTTPDTO> badRequest() {
        return new ResponseEntity<>(new HTTPDTO("Bad Request"), HttpStatus.BAD_REQUEST);
    }
}

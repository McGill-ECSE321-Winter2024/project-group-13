package ca.mcgill.ecse321.rest.helpers;

import ca.mcgill.ecse321.rest.dto.http.HTTPDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DefaultHTTPResponse {
    public static ResponseEntity<HTTPDTO> success(String message) {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage(message);
        return new ResponseEntity<>(httpdto, HttpStatus.OK);
    }

    public static ResponseEntity<HTTPDTO> success() {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage("Success");
        return new ResponseEntity<>(httpdto, HttpStatus.OK);
    }
    
    public static ResponseEntity<HTTPDTO> unauthorized(String message) {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage(message);
        return new ResponseEntity<>(httpdto, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<HTTPDTO> unauthorized() {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage("Unauthorized");
        return new ResponseEntity<>(httpdto, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<HTTPDTO> forbidden(String message) {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage(message);
        return new ResponseEntity<>(httpdto, HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<HTTPDTO> forbidden() {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage("Forbidden");
        return new ResponseEntity<>(httpdto,HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<HTTPDTO> notFound(String message) {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage(message);
        return new ResponseEntity<>(httpdto, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<HTTPDTO> notFound() {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage("Not Found");
        return new ResponseEntity<>(httpdto, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<HTTPDTO> badRequest(String message) {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage(message);
        return new ResponseEntity<>(httpdto, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<HTTPDTO> badRequest() {
        HTTPDTO httpdto= new HTTPDTO();
        httpdto.setMessage("Bad Request");
        return new ResponseEntity<>(httpdto,HttpStatus.BAD_REQUEST);
    }
}

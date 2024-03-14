package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Instructor;
import ca.mcgill.ecse321.rest.models.Owner;
import ca.mcgill.ecse321.rest.models.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationService {

    private long accessTokenValidity = 60*60*1000;
    private final String secret_key = "mysecretkey";


    @Autowired
    private PersonRepository personRepository;

    public boolean isValidCredentials(String email, String password){
        return personRepository.findPersonByEmailAndPassword(email, password) != null;
    }

    public String issueToken(String email){
        Person person = personRepository.findPersonByEmail(email);
        return person.getId();
//        Claims claims = Jwts.claims().setSubject(person.getEmail());
//        claims.put("name",person.getName());
//        Date tokenCreateTime = new Date();
//        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
//        return Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(tokenValidity)
//                .signWith(SignatureAlgorithm.HS256, secret_key)
//                .compact();
    }

    private String verifyToken(String token){
        String session = token.substring(7);
        Person person = personRepository.findPersonById(session);
        if(person == null) throw new IllegalArgumentException("Invalid token");
        return person.getId();
    }

    public PersonSession verifyTokenAndGetUser(String token){
        String id = verifyToken(token);
        Person person = personRepository.findPersonById(id);
        boolean isCustomer = person instanceof Customer;
        boolean isOwner = person instanceof Owner;
        boolean isInstructor = person instanceof Instructor;
        PersonSession.PersonType personType = isCustomer ? PersonSession.PersonType.Customer : isOwner ? PersonSession.PersonType.Owner : PersonSession.PersonType.Instructor;
        return new PersonSession(person.getId(), personType);
    }

    public Customer registerCustomer(String email, String password, String name, String phoneNumber){
        if (personRepository.findPersonByEmail(email) != null){
            return null;
        }
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setName(name);
        customer.setPhoneNumber(phoneNumber);
        return personRepository.save(customer);
    }

}

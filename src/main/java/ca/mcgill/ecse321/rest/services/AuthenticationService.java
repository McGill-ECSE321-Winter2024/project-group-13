package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.PersonSession;
import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Instructor;
import ca.mcgill.ecse321.rest.models.Owner;
import ca.mcgill.ecse321.rest.models.Person;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
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
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret_key);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("id", person.getId())
                    .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidity))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private String verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret_key);
            String id = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token)
                    .getClaim("id")
                    .asString();
            Person person = personRepository.findPersonById(id);
            return person.getId();
        } catch (Exception exception){
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private void validatePhoneNumber(String phoneNumber){
        if (phoneNumber.length() != 10) throw new IllegalArgumentException("Invalid phone number");
        if (!phoneNumber.matches("[0-9]+")) throw new IllegalArgumentException("Invalid phone number");
    }

    public PersonSession verifyTokenAndGetUser(String rawToken){
        String token = rawToken.split(" ")[1];
        String id = verifyToken(token);
        Person person = personRepository.findPersonById(id);
        boolean isCustomer = person instanceof Customer;
        boolean isOwner = person instanceof Owner;
        boolean isInstructor = person instanceof Instructor;
        PersonSession.PersonType personType =
                isCustomer ? PersonSession.PersonType.Customer :
                        isOwner ? PersonSession.PersonType.Owner :
                                isInstructor ? PersonSession.PersonType.Instructor :
                                        null;
        if (personType == null) throw new IllegalArgumentException("Invalid person role");
        return new PersonSession(person.getId(), personType);
    }

    public String registerCustomer(String email, String password, String name, String phoneNumber){
        if (personRepository.findPersonByEmail(email) != null){
            throw new IllegalArgumentException("Email already exists");
        }
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setName(name);
        customer.setPhoneNumber(phoneNumber);
        personRepository.save(customer);
        return this.issueToken(email);

    }

    public void changePassword(String personId, String password){
        Person person = personRepository.findPersonById(personId);
        person.setPassword(password);
        personRepository.save(person);
    }

    public void changeEmail(String personId, String email){
        if(personRepository.findPersonByEmail(email) != null){
            throw new IllegalArgumentException("Email already exists");
        }
        Person person = personRepository.findPersonById(personId);
        person.setEmail(email);
        personRepository.save(person);
    }

    public void changePhoneNumber(String personId, String phoneNumber){
        validatePhoneNumber(phoneNumber);
        if (personRepository.findPersonByPhoneNumber(phoneNumber) != null) throw new IllegalArgumentException("Phone number already exists");
        Person person = personRepository.findPersonById(personId);
        person.setPhoneNumber(phoneNumber);
        personRepository.save(person);
    }

}

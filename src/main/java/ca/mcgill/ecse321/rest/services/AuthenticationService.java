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

    private final String secret_key = "mysecretkey";

    @Autowired
    private PersonRepository personRepository;

    public boolean isValidCredentials(String email, String password){
        return personRepository.findPersonByEmailAndPassword(email, password) != null;
    }

    public String issueTokenWithEmail(String email){
        Person person = personRepository.findPersonByEmail(email);
        return issueToken(person.getId());
    }

    public String issueToken(String personId){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret_key);
            long accessTokenValidity = 60 * 60 * 1000;
            return JWT.create()
                    .withIssuer("ecse321")
                    .withClaim("id", personId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidity))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private String verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret_key);
            String id = JWT.require(algorithm)
                    .withIssuer("ecse321")
                    .build()
                    .verify(token)
                    .getClaim("id")
                    .asString();
            Person person = personRepository.findPersonById(id);
            if (person == null) throw new IllegalArgumentException("Invalid token");
            System.out.println(person);
            return person.getId();
        } catch (Exception exception){
            throw new IllegalArgumentException("Invalid token");
        }
    }


    public PersonSession verifyTokenAndGetUser(String rawToken){
        String token = rawToken.split(" ")[1];
        String id = verifyToken(token);
        Person person = personRepository.findPersonById(id);
        System.out.println(person);
        boolean isCustomer = person instanceof Customer;
        boolean isOwner = person instanceof Owner;
        boolean isInstructor = person instanceof Instructor;
        PersonSession.PersonType personType =
                isCustomer ? PersonSession.PersonType.Customer :
                        isOwner ? PersonSession.PersonType.Owner :
                                isInstructor ? PersonSession.PersonType.Instructor :
                                        null;
        String sportCenterId = null;
        sportCenterId = isOwner
                ? ((Owner) person).getSportCenter().getId() :
                isInstructor
                ? ((Instructor) person).getSportCenter().getId() :
                        isCustomer ? ((Customer) person).getSportCenter().getId() : null;
        if (personType == null) throw new IllegalArgumentException("Invalid person role");
        return new PersonSession(person.getId(), personType, sportCenterId);
    }

    public String registerCustomer(String email, String password, String name, String phoneNumber){
        if (personRepository.findPersonByEmail(email) != null){
            throw new IllegalArgumentException("Email already exists");
        }
        try {
            Customer customer = new Customer();
            customer.setEmail(email);
            customer.setPassword(password);
            customer.setName(name);
            customer.setPhoneNumber(phoneNumber);
            personRepository.save(customer);
            return this.issueToken(email);
        } catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    public String changePassword(String personId, String password){
        Person person = personRepository.findPersonById(personId);
        if (person == null) return "Person not found";
        person.setPassword(password);
        personRepository.save(person);
        return null;
    }

    public String changeEmail(String personId, String email){
        // validate email with regex
        if (!email.matches("^(.+)@(.+)$")) return "Invalid email";
        if(personRepository.findPersonByEmail(email) != null) return "Email already in use";
        Person person = personRepository.findPersonById(personId);
        person.setEmail(email);
        personRepository.save(person);
        return null;
    }

    public String changePhoneNumber(String personId, String phoneNumber){
        if (personRepository.findPersonByPhoneNumber(phoneNumber) != null) return "Phone number already in use";
        Person person = personRepository.findPersonById(personId);
        person.setPhoneNumber(phoneNumber);
        personRepository.save(person);
        return null;
    }

}

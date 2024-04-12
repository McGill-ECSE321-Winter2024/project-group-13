package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.models.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.mcgill.ecse321.rest.services.TwilioService;

import java.util.Date;

@Service
public class AuthenticationService {

    private final String secret_key = "mysecretkey";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    public boolean isValidCredentials(String email, String password){
        Owner admin = (Owner) personRepository.findPersonByEmail("admin@admin.com");
        if (admin == null) {
            System.out.println("Admin not found, creating admin");
            SportCenter sportCenter = sportCenterRepository.findSportCenterByIdNotNull();
            if (sportCenter == null) {
                System.out.println("SportCenter not found, creating sport center");
                sportCenter = new SportCenter();
                sportCenter.setName("SportCenter");
                sportCenterRepository.save(sportCenter);
            }
            admin = new Owner();
            admin.setEmail("admin@admin.com");
            admin.setPassword("admin");
            admin.setName("admin");
            admin.setPhoneNumber("1234567890");
            admin.setSportCenter(sportCenter);
            personRepository.save(admin);
        }
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
        return new PersonSession(
                person.getId(),
                person.getName(),
                person.getEmail(),
                person.getPhoneNumber(),
                personType,
                sportCenterId
        );
    }

    public String registerCustomer(String email, String password, String name, String phoneNumber){
        if (personRepository.findPersonByEmail(email) != null){
            throw new IllegalArgumentException("Email already exists");
        }
        if(personRepository.findPersonByPhoneNumber(phoneNumber) != null){
            throw new IllegalArgumentException("Phone number already in use");
        }
        try {
            Customer customer = new Customer();
            customer.setEmail(email);
            customer.setPassword(password);
            customer.setName(name);
            customer.setPhoneNumber(phoneNumber);
            customer.setSportCenter(sportCenterRepository.findSportCenterByIdNotNull());
            personRepository.save(customer);
            TwilioService.sendSms(phoneNumber, "Hello " + name + ", you have been registered successfully");
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
        Person user = personRepository.findPersonByEmail(email);
        if(user != null && !personId.equals(user.getId())) return "Email already in use";
        Person person = personRepository.findPersonById(personId);
        person.setEmail(email);
        personRepository.save(person);
        return null;
    }

    public String changePhoneNumber(String personId, String phoneNumber){
        Person user = personRepository.findPersonByPhoneNumber(phoneNumber);
        if (user != null && !personId.equals(user.getId())) return "Phone number already in use";
        Person person = personRepository.findPersonById(personId);
        person.setPhoneNumber(phoneNumber);
        personRepository.save(person);
        return null;
    }

}

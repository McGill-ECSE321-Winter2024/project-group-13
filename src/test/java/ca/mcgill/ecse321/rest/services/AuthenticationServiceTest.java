package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.dao.PersonRepository;
import ca.mcgill.ecse321.rest.dao.SportCenterRepository;
import ca.mcgill.ecse321.rest.models.Customer;
import ca.mcgill.ecse321.rest.models.Person;
import ca.mcgill.ecse321.rest.models.SportCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private PersonRepository personRepository;
    @Mock private SportCenterRepository sportCenterRepository;

    @InjectMocks
    private AuthenticationService authenticationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValidCredentials_ValidCredentials_ReturnsTrue() {
        // Arrange
        String email = "test@example.com";
        String password = "passsword";
        Person person = new Customer();
        when(personRepository.findPersonByEmailAndPassword(email, password)).thenReturn(person);

        boolean result = authenticationService.isValidCredentials(email, password);

        assertTrue(result);
        verify(personRepository).findPersonByEmailAndPassword(email, password);
    }

    @Test
    void isValidCredentials_InvalidCredentials_ReturnsFalse() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        when(personRepository.findPersonByEmailAndPassword(email, password)).thenReturn(null);

        // Act
        boolean result = authenticationService.isValidCredentials(email, password);

        // Assert
        assertFalse(result);
        verify(personRepository).findPersonByEmailAndPassword(email, password);
    }



    @Test
    void changePassword_ValidInput_ReturnsToken() {
        // Arrange
        String personId = "123";
        String password = "password";
        Person person = new Customer();
        person.setId(personId);
        person.setPassword(password);
        when(personRepository.findPersonById(personId)).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        authenticationService.changePassword(personId, password);
        assertEquals(password, person.getPassword());

        verify(personRepository).findPersonById(personId);
        verify(personRepository).save(any(Person.class));

    }

    @Test
    void changePassword_InvalidInput_ThrowsException() {
        // Arrange
        String personId = "123";
        String password = "password";
        when(personRepository.findPersonById(personId)).
                thenReturn(null);

        // Act
        String error = authenticationService.changePassword(personId, password);
        assertEquals("Person not found", error);
        // Assert
        verify(personRepository).findPersonById(personId);
    }


    @Test
    void changeEmail_ValidInput_ReturnsToken() {
        // Arrange
        String personId = "123";
        String oldEmail = "test1@example.com";
        String newEmail = "text2@example.com";

        Person person = new Customer();
        person.setId(personId);
        person.setEmail(oldEmail);


        when(personRepository.findPersonByEmail(newEmail)).thenReturn(null);
        when(personRepository.findPersonById(personId)).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        assertDoesNotThrow(() -> {
            authenticationService.changeEmail(personId, newEmail);
        });

        assertEquals(newEmail, person.getEmail());

        verify(personRepository).findPersonById(personId);
        verify(personRepository).findPersonByEmail(newEmail);
    }

    @Test
    void changePhoneNumber_ValidInput_ReturnsToken() {
        // Arrange
        String personId = "123";
        String phoneNumber = "1234567890";
        Person person = new Customer();
        person.setId(personId);
        person.setPhoneNumber(phoneNumber);
        when(personRepository.findPersonById(personId)).thenReturn(person);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        authenticationService.changePhoneNumber(personId, phoneNumber);
        assertEquals(phoneNumber, person.getPhoneNumber());

        verify(personRepository).findPersonById(personId);
        verify(personRepository).save(any(Person.class));
    }


    @Test
    void verifyTokenAndGetUser_ValidToken_ReturnsPersonSession() {
        // Arrange
        String personId = "123";
        Customer customer = new Customer();
        customer.setId(personId);
        SportCenter sportCenter = new SportCenter();
        sportCenter.setId("123");
        customer.setSportCenter(sportCenter);
        String jwt = authenticationService.issueToken(personId);
        when(personRepository.findPersonById(personId)).thenReturn(customer);

        String authorization = "Bearer " + jwt;
        authenticationService.verifyTokenAndGetUser(authorization);

        verify(personRepository, times(2)).findPersonById(personId);

    }

    @Test
    void verifyTokenAndGetUser_NonExistantUserId_ThrowsException() {
        // Arrange
        String token = authenticationService.issueToken("invalidtoken");
        String authorization = "Bearer " + token;
        when(personRepository.findPersonById("invalidtoken")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.verifyTokenAndGetUser(authorization);
        });

        verify(personRepository).findPersonById("invalidtoken");
    }

    @Test
    void verifyTokenAndGetUser_InvalidToken_ThrowsException() {
        // Arrange
        String token = "Bearer invalidtoken";
        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.verifyTokenAndGetUser(token);
        });
    }
}

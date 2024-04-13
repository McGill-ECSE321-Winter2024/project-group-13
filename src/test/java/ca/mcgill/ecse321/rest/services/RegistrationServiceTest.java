package ca.mcgill.ecse321.rest.services;
import ca.mcgill.ecse321.rest.helpers.PersonSession;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.models.*;


@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    //Using @Mock on all needed repositories
    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private InvoiceRepository invoiceRepository;
    @InjectMocks
    private RegistrationService registrationService;

    //Before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Template for all the following tests:
     *
     * First create a setup containing most or all of the following: courses, persons, registration, rating and invoices
     * Then, since we are using mock repositories, use "when()" to return values for methods called in "RegistrationService".
     * After that methods from "RegistrationService" are called to be tested.
     * The returned object should pass all written "assert" and "verify"
     */


    /**
     * This test follows the template described before the first test
     * This tests tries to get all registration for an owner and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    public void getRegistrationsForOwnerTest_Valid() {

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration = new Registration("id1",rating1,  customer1, course1);
        Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration); registrationList.add(registration2);

        when(registrationRepository.findAll()).thenReturn(registrationList);
        PersonSession personSession = new PersonSession("PersonID", PersonSession.PersonType.Owner, "SportCenter123");

        List<Registration> registrationsGOT = registrationService.getRegistrations(personSession);

        assertNotNull(registrationsGOT);
        assertEquals(registrationsGOT, registrationList);
        verify(registrationRepository,times(1)).findAll();
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all registration for an instructor and succeeds.
     * @Author Teddy El-Husseini
     */
    @Test
    public void getRegistrationsForInstructorTest_Valid() {

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        Instructor instructor1 = new Instructor(); Instructor instructor2 = new Instructor(); instructor1.setId("Instructor1");
        course1.setInstructor(instructor1); course2.setInstructor(instructor2);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);

        PersonSession personSession = new PersonSession(instructor1.getId(), PersonSession.PersonType.Instructor, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(instructorRepository.findInstructorById(instructor1.getId())).thenReturn(instructor1);

        List<Registration> registrationsGOT = registrationService.getRegistrations(personSession);

        assertNotNull(registrationsGOT);
        assertEquals(1,registrationsGOT.size());

        List<Registration> expected = new ArrayList<>(); expected.add(registration1);

        assertEquals(expected,registrationsGOT);
        verify(registrationRepository,times(1)).findAll();
        verify(instructorRepository,times(1)).findInstructorById(anyString());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all registration an instructor,
     * but bad parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    public void getRegistrationsForInstructorTest_Invalid() {

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        Instructor instructor1 = new Instructor(); Instructor instructor2 = new Instructor(); instructor1.setId("Instructor1"); instructor2.setId("Instructor2");
        course1.setInstructor(instructor1); course2.setInstructor(instructor1);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);

        PersonSession personSession = new PersonSession(instructor2.getId(), PersonSession.PersonType.Instructor, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(instructorRepository.findInstructorById(instructor2.getId())).thenReturn(instructor2);

        List<Registration> registrationsGOT = registrationService.getRegistrations(personSession);

        assertNotNull(registrationsGOT);
        assertEquals(0,registrationsGOT.size());

        List<Registration> expected = new ArrayList<>();
        assertEquals(expected, registrationsGOT);
        verify(registrationRepository,times(1)).findAll();
        verify(instructorRepository,times(1)).findInstructorById(anyString());
    }

    /**
     * This test follows the template described before test1
     * This tests tries to get all registration for a customer and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void getRegistrationsForCustomerTest_Valid() {
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);

        PersonSession personSession = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(customerRepository.findCustomerById(customer1.getId())).thenReturn(customer1);

        List<Registration> registrationsGOT = registrationService.getRegistrations(personSession);

        assertNotNull(registrationsGOT);
        assertEquals(1,registrationsGOT.size());

        List<Registration> expected = new ArrayList<>(); expected.add(registration1);

        assertEquals(expected,registrationsGOT);
        verify(registrationRepository,times(1)).findAll();
        verify(customerRepository,times(1)).findCustomerById(anyString());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all registration for a customer,
     * but a bad parameter is passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void getRegistrationsForCustomerTest_Invalid() {
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer1);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);

        PersonSession personSession = new PersonSession(customer2.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(customerRepository.findCustomerById(customer2.getId())).thenReturn(customer2);

        List<Registration> registrationsGOT = registrationService.getRegistrations(personSession);

        assertNotNull(registrationsGOT);
        assertEquals(0,registrationsGOT.size());

        List<Registration> expected = new ArrayList<>();

        assertEquals(expected,registrationsGOT);
        verify(registrationRepository,times(1)).findAll();
        verify(customerRepository,times(1)).findCustomerById(anyString());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an owner given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void getSpecificRegistrationsForOwnerTest_Valid() {

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);

        List<Registration> registrationList = new ArrayList<>();

        registrationList.add(registration1); registrationList.add(registration2);

        when(registrationRepository.findAll()).thenReturn(registrationList);
        PersonSession personSession = new PersonSession("PersonID", PersonSession.PersonType.Owner, "SportCenter123");

        Registration registrationGOT = registrationService.getSpecificRegistration(personSession, registration1.getId());

        assertNotNull(registrationGOT);
        assertEquals(registration1, registrationGOT);
        verify(registrationRepository,times(1)).findAll();
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an owner given the registrationID.
     * but some invalid parameters are passed
     * @Author Teddy El-Husseini
     */
    @Test
    void getSpecificRegistrationsForOwnerTest_Invalid() {

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        List<Registration> registrationList = new ArrayList<>();

        registrationList.add(registration1); registrationList.add(registration2);

        when(registrationRepository.findAll()).thenReturn(registrationList);
        PersonSession personSession = new PersonSession("PersonID", PersonSession.PersonType.Owner, "SportCenter123");

        Registration registrationGOT = registrationService.getSpecificRegistration(personSession, (registration1.getId()+"1"));

        assertNull(registrationGOT);
        assertNull( registrationService.getSpecificRegistration(personSession, null ));
        assertNull( registrationService.getSpecificRegistration(personSession, " " ));
        assertThrows(IllegalArgumentException.class, () -> {registrationService.getSpecificRegistration(null, "TEST" );});

        verify(registrationRepository,times(1)).findAll();
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an instructor given the registrationID,
     * @Author Teddy El-Husseini
     */
    @Test
    void getSpecificRegistrationsForInstructorTest_Valid() {

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        Instructor instructor1 = new Instructor(); Instructor instructor2 = new Instructor(); instructor1.setId("Instructor1");
        course1.setInstructor(instructor1); course2.setInstructor(instructor2);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);
        PersonSession personSession = new PersonSession(instructor1.getId(), PersonSession.PersonType.Instructor, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(instructorRepository.findInstructorById(instructor1.getId())).thenReturn(instructor1);
        Registration registrationsGOT = registrationService.getSpecificRegistration(personSession, registration1.getId());

        assertNotNull(registrationsGOT);
        assertEquals(registration1,registrationsGOT);
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for an instructor given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void getSpecificRegistrationsForInstructorTest_Invalid() {

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        Instructor instructor1 = new Instructor(); Instructor instructor2 = new Instructor(); instructor1.setId("Instructor1"); instructor2.setId("Instructor2");
        course1.setInstructor(instructor1); course2.setInstructor(instructor1);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);
        PersonSession personSession = new PersonSession(instructor2.getId(), PersonSession.PersonType.Instructor, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(instructorRepository.findInstructorById(instructor2.getId())).thenReturn(instructor2);
        Registration registrationsGOT = registrationService.getSpecificRegistration(personSession, registration1.getId());

        assertNull(registrationsGOT);

        assertNull(registrationService.getSpecificRegistration(personSession, null ));
        assertNull( registrationService.getSpecificRegistration(personSession, " " ));
        assertThrows(IllegalArgumentException.class, () -> {registrationService.getSpecificRegistration(null, "TEST" );});

        verify(registrationRepository,times(1)).findAll();
        verify(instructorRepository,times(1)).findInstructorById(anyString());}

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for a Customer given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void getSpecificRegistrationsForCustomerTest_Valid() {
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);

        PersonSession personSession = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(customerRepository.findCustomerById(customer1.getId())).thenReturn(customer1);

        Registration registrationsGOT = registrationService.getSpecificRegistration(personSession, registration1.getId());

        assertNotNull(registrationsGOT);
        assertEquals(registration1,registrationsGOT);
        verify(registrationRepository,times(1)).findAll();
        verify(customerRepository,times(1)).findCustomerById(anyString());}

    /**
     * This test follows the template described before the first test
     * This tests tries to get a specific registration for a Customer given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void getSpecificRegistrationsForCustomerTest_Invalid() {
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer1);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);
        PersonSession personSession = new PersonSession(customer2.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(customerRepository.findCustomerById(customer2.getId())).thenReturn(customer2);

        Registration registrationsGOT = registrationService.getSpecificRegistration(personSession, registration1.getId());

        assertNull(registrationsGOT);
        assertNull( registrationService.getSpecificRegistration(personSession, null ));
        assertNull( registrationService.getSpecificRegistration(personSession, " "));
        assertThrows(IllegalArgumentException.class, () -> {registrationService.getSpecificRegistration(null, "TEST" );});
        verify(registrationRepository,times(1)).findAll();
        verify(customerRepository,times(1)).findCustomerById(anyString());}

    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for an owner given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void cancelRegistrationForOwnerTest_Valid(){
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        List<Registration> registrationList = new ArrayList<>();

        registrationList.add(registration1); registrationList.add(registration2);

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(registrationRepository.findRegistrationById(registration1.getId())).thenReturn(registration1);
        PersonSession personSession = new PersonSession("PersonID", PersonSession.PersonType.Owner, "SportCenter123");

        assertTrue(registrationService.cancelSpecificRegistration(personSession, registration1.getId()));

        verify(registrationRepository,times(1)).findAll();
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for an owner given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void cancelRegistrationForOwnerTest_Invalid(){
        Course course1 = new Course();
        Customer customer1 = new Customer();
        int rating1 = 3;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1);

        PersonSession personSession = new PersonSession("PersonID", PersonSession.PersonType.Owner, "SportCenter123");

        assertFalse(registrationService.cancelSpecificRegistration(personSession, registration1.getId()+"1"));
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for an instructor given the registrationID,
     * but instructor cannot cancel a registration.
     * @Author Teddy El-Husseini
     */
    @Test
    void cancelRegistrationForInstructorTest_Invalid(){
        Course course1 = new Course();
        Customer customer1 = new Customer();
        int rating1 = 3;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1);

        PersonSession personSession = new PersonSession("PersonID", PersonSession.PersonType.Instructor, "SportCenter123");
        assertThrows(IllegalArgumentException.class, () -> {registrationService.cancelSpecificRegistration(personSession, registration1.getId());});
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for a customer given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void cancelRegistrationForCustomerTest_Valid(){
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);

        PersonSession personSession = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(customerRepository.findCustomerById(customer1.getId())).thenReturn(customer1);

        assertTrue(registrationService.cancelSpecificRegistration(personSession, registration1.getId()));

        verify(registrationRepository,times(1)).findAll();
        verify(customerRepository,times(1)).findCustomerById(anyString());}

    /**
     * This test follows the template described before the first test
     * This tests tries to cancel a specific registration for a customer given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void cancelRegistrationForCustomerTest_Invalid(){
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);
        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);

        PersonSession personSession = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        when(registrationRepository.findAll()).thenReturn(registrationList);
        when(customerRepository.findCustomerById(customer1.getId())).thenReturn(customer1);

        assertFalse(registrationService.cancelSpecificRegistration(personSession, registration1.getId()+"1"));

        verify(registrationRepository,times(1)).findAll();
        verify(customerRepository,times(1)).findCustomerById(anyString());
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to update a specific registration for an owner and an intructors given the registrationID and rating,
     * but owners and instructors cannot update the rating of a registration.
     * @Author Teddy El-Husseini
     */
    @Test
    void updateRegistrationRatingForOwnerAndInstructorTest_Invalid(){
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);

        PersonSession personSession1 = new PersonSession("customer1.getId()", PersonSession.PersonType.Owner, "SportCenter123");
        PersonSession personSession2 = new PersonSession("customer1.getId()", PersonSession.PersonType.Instructor, "SportCenter123");

        assertFalse(registrationService.updateRegistrationRating(personSession1, registration1.getId(), 3));
        assertFalse(registrationService.updateRegistrationRating(personSession2, registration1.getId(), 4));

        assertFalse(registrationService.updateRegistrationRating(personSession1, null, 3));
        assertFalse(registrationService.updateRegistrationRating(personSession1, " ", 3));
        assertFalse( registrationService.updateRegistrationRating(personSession1, registration1.getId(), -10));
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to update a specific registration for a customer given the registrationID and rating and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void updateRegistrationRatingForCustomerTest_Valid(){
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);

        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);
        when(registrationRepository.findAll()).thenReturn(registrationList);


        PersonSession personSession1 = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        assertTrue(registrationService.updateRegistrationRating(personSession1, registration1.getId(), 3));
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to update a specific registration for a customer given the registrationID and rating,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void updateRegistrationRatingForCustomerTest_Invalid(){

        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);

        List<Registration> registrationList = new ArrayList<>();
        registrationList.add(registration1); registrationList.add(registration2);
        when(registrationRepository.findAll()).thenReturn(registrationList);

        PersonSession personSession1 = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        assertFalse(registrationService.updateRegistrationRating(personSession1, registration2.getId(), 3));

        assertFalse(registrationService.updateRegistrationRating(personSession1, null, 3));
        assertFalse(registrationService.updateRegistrationRating(personSession1, " ", 3));
        assertFalse(registrationService.updateRegistrationRating(personSession1, registration1.getId(), -10));
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for an owner given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void getInvoicesForOwner_Valid() {
        Registration registration = new Registration("IDREG",4 ,new Customer(), new Course());
        Invoice invoice1 = new Invoice(); invoice1.setAmount(10); invoice1.setRegistration(registration);
        Invoice invoice2 = new Invoice(); invoice2.setAmount(30); invoice2.setRegistration(registration);
        Invoice invoice3 = new Invoice(); invoice3.setAmount(20); invoice3.setRegistration(registration);

        List<Invoice> invoiceList = new ArrayList<>(); invoiceList.add(invoice1); invoiceList.add(invoice2); invoiceList.add(invoice3);

        PersonSession personSession = new PersonSession("personID", PersonSession.PersonType.Owner, "SportCenter123");

        when(registrationRepository.findRegistrationById(registration.getId())).thenReturn(registration);
        when(invoiceRepository.findAll()).thenReturn(invoiceList);

        List<Invoice> invoicesGOT = registrationService.getInvoicess(personSession, registration.getId());

        assertEquals(3, invoicesGOT.size());
        assertEquals(invoiceList, invoicesGOT);
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for an owner given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void getInvoicesForOwner_Invalid() {
        Registration registration = new Registration("IDREG",4 ,new Customer(), new Course());
        Invoice invoice1 = new Invoice(); invoice1.setAmount(10); invoice1.setRegistration(registration);
        Invoice invoice2 = new Invoice(); invoice2.setAmount(30); invoice2.setRegistration(registration);
        Invoice invoice3 = new Invoice(); invoice3.setAmount(20); invoice3.setRegistration(registration);

        List<Invoice> invoiceList = new ArrayList<>(); invoiceList.add(invoice1); invoiceList.add(invoice2); invoiceList.add(invoice3);

        PersonSession personSession = new PersonSession("personID", PersonSession.PersonType.Owner, "SportCenter123");

        when(registrationRepository.findRegistrationById(registration.getId()+"1")).thenReturn(null);
        when(invoiceRepository.findAll()).thenReturn(invoiceList);

        List<Invoice> invoicesGOT = registrationService.getInvoicess(personSession, registration.getId()+"1");

        assertEquals(0, invoicesGOT.size());
        List<Invoice> expected = new ArrayList<>();

        assertNull( registrationService.getInvoicess(personSession, null));
        assertNull( registrationService.getInvoicess(personSession, " "));

        assertEquals(expected, invoicesGOT);
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for an instructor given the registrationID,
     * but instructors can not get invoices.
     * @Author Teddy El-Husseini
     */
    @Test
    void getInvoicesForInstructor_Invalid() {
        Registration registration = new Registration("IDREG",4 ,new Customer(), new Course());
        Invoice invoice1 = new Invoice(); invoice1.setAmount(10); invoice1.setRegistration(registration);
        Invoice invoice2 = new Invoice(); invoice2.setAmount(30); invoice2.setRegistration(registration);
        Invoice invoice3 = new Invoice(); invoice3.setAmount(20); invoice3.setRegistration(registration);

        PersonSession personSession = new PersonSession("personID", PersonSession.PersonType.Instructor, "SportCenter123");

        assertThrows(IllegalArgumentException.class, () -> { registrationService.getInvoicess(personSession, registration.getId());});
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for a customer given the registrationID and succeeds
     * @Author Teddy El-Husseini
     */
    @Test
    void getInvoicesForCustomer_Valid() {
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);
        Invoice invoice1 = new Invoice(); invoice1.setAmount(10); invoice1.setRegistration(registration1);
        Invoice invoice2 = new Invoice(); invoice2.setAmount(30); invoice2.setRegistration(registration1);
        Invoice invoice3 = new Invoice(); invoice3.setAmount(20); invoice3.setRegistration(registration2);

        when(registrationRepository.findRegistrationById(registration1.getId())).thenReturn(registration1);

        List<Invoice> invoiceList = new ArrayList<>(); invoiceList.add(invoice1); invoiceList.add(invoice2); invoiceList.add(invoice3);
        when(invoiceRepository.findAll()).thenReturn(invoiceList);
        when(customerRepository.findCustomerById(customer1.getId())).thenReturn(customer1);
        PersonSession personSession = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        List<Invoice> invoicesGOT = registrationService.getInvoicess(personSession, registration1.getId());

        assertEquals(2, invoicesGOT.size());

        List<Invoice> expected = new ArrayList<>();
        expected.add(invoice1); expected.add(invoice2);
        assertEquals(expected, invoicesGOT);
    }

    /**
     * This test follows the template described before the first test
     * This tests tries to get all invoices linked to a specific registration for a customer given the registrationID,
     * but some invalid parameters are passed.
     * @Author Teddy El-Husseini
     */
    @Test
    void getInvoicesForCustomer_Invalid() {
        Course course1 = new Course(); Course course2 = new Course();
        Customer customer1 = new Customer(); Customer customer2 = new Customer();
        customer1.setId("cus1"); customer2.setId("cus2");
        int rating1 = 3; int rating2 = 4;
        Registration registration1 = new Registration("id1",rating1,  customer1, course1); Registration registration2 = new Registration("id2",rating2,  customer2, course2);
        registration1.setCustomer(customer1); registration2.setCustomer(customer2);
        Invoice invoice1 = new Invoice(); invoice1.setAmount(10); invoice1.setRegistration(registration1);
        Invoice invoice2 = new Invoice(); invoice2.setAmount(30); invoice2.setRegistration(registration1);
        Invoice invoice3 = new Invoice(); invoice3.setAmount(20); invoice3.setRegistration(registration2);
        when(registrationRepository.findRegistrationById(registration1.getId()+"1")).thenReturn(null);
        List<Invoice> invoiceList = new ArrayList<>(); invoiceList.add(invoice1); invoiceList.add(invoice2); invoiceList.add(invoice3);
        when(invoiceRepository.findAll()).thenReturn(invoiceList);
        when(customerRepository.findCustomerById(customer1.getId())).thenReturn(customer1);

        PersonSession personSession = new PersonSession(customer1.getId(), PersonSession.PersonType.Customer, "SportCenter123");

        List<Invoice> invoicesGOT = registrationService.getInvoicess(personSession, registration1.getId()+"1");

        assertEquals(0, invoicesGOT.size());

        assertNull(registrationService.getInvoicess(personSession, null));
        assertNull( registrationService.getInvoicess(personSession, " "));
    }}


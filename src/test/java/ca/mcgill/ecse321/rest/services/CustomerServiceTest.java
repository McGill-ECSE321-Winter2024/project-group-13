package ca.mcgill.ecse321.rest.services;

import ca.mcgill.ecse321.rest.helpers.PersonSession;
import ca.mcgill.ecse321.rest.dao.*;
import ca.mcgill.ecse321.rest.dto.CustomerDTO;
import ca.mcgill.ecse321.rest.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@Nested
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllTestAsOwner() {
        SportCenter sportCenter = new SportCenter("1");
        sportCenter.setAddress("1234 Rue Sainte-Catherine");
        sportCenter.setName("McGill Sports Center");


        Customer customer1 = new Customer();
        customer1.setId("1");
        customer1.setName("John Doe");
        customer1.setPhoneNumber("1234567890");
        customer1.setEmail("johndoe@gmail.com");
        customer1.setPassword("password1");
        customer1.setSportCenter(sportCenter);
        CustomerDTO customerDTO1 = new CustomerDTO(customer1);

        Customer customer2 = new Customer();
        customer2.setId("2");
        customer2.setName("Jane Doe");
        customer2.setPhoneNumber("0987654321");
        customer2.setEmail("janedoe@gmail.com");
        customer2.setPassword("password2");
        customer2.setSportCenter(sportCenter);
        CustomerDTO customerDTO2 = new CustomerDTO(customer2);


        List<Customer> customers = List.of(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customers);

        PersonSession ownerSession = new PersonSession("1", PersonSession.PersonType.Owner, "1");
        List<CustomerDTO> customerDTOS = customerService.findAll(ownerSession);

        assertEquals(2, customerDTOS.size());
        assertEquals(customerDTO1, customerDTOS.get(0));
        assertEquals(customerDTO2, customerDTOS.get(1));

    }

    @Test
    void findAllTestAsInstructor() {
        SportCenter sportCenter = new SportCenter("1");
        sportCenter.setAddress("1234 Rue Sainte-Catherine");
        sportCenter.setName("McGill Sports Center");


        Customer customer1 = new Customer();
        customer1.setId("1");
        customer1.setName("John Doe");
        customer1.setPhoneNumber("1234567890");
        customer1.setEmail("johndoe@gmail.com");
        customer1.setPassword("password1");
        customer1.setSportCenter(sportCenter);
        CustomerDTO customerDTO1 = new CustomerDTO(customer1);

        Customer customer2 = new Customer();
        customer2.setId("2");
        customer2.setName("Jane Doe");
        customer2.setPhoneNumber("0987654321");
        customer2.setEmail("janedoe@gmail.com");
        customer2.setPassword("password2");
        customer2.setSportCenter(sportCenter);
        CustomerDTO customerDTO2 = new CustomerDTO(customer2);


        List<Customer> customers = List.of(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customers);

        PersonSession instructorSession = new PersonSession("1", PersonSession.PersonType.Instructor, "1");
        List<CustomerDTO> customerDTOS = customerService.findAll(instructorSession);

        assertEquals(2, customerDTOS.size());
        assertEquals(customerDTO1, customerDTOS.get(0));
        assertEquals(customerDTO2, customerDTOS.get(1));

    }

    @Test
    void findAllTestAsCustomer() {
        PersonSession customerSession = new PersonSession("1", PersonSession.PersonType.Customer, "1");
        List<CustomerDTO> customerDTOS = customerService.findAll(customerSession);
        assertNull(customerDTOS);
    }

}

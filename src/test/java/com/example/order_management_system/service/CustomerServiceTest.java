package com.example.order_management_system.service;

import com.example.order_management_system.model.Customer;
import com.example.order_management_system.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void testSaveCustomer() {

        Customer customerToSave = Customer.builder()
                .registrationCode("RegCode1")
                .name("Dmitrii")
                .email("dmitrii@gmail.com")
                .telephone("123-456-7890")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .registrationCode("RegCode1")
                .name("Dmitrii")
                .email("dmitrii@gmail.com")
                .telephone("123-456-7890")
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer returnedCustomer = customerService.saveCustomer(customerToSave);

        verify(customerRepository, times(1)).save(customerToSave);

        verifyNoMoreInteractions(customerRepository);
        assertNotNull(returnedCustomer);
        assertEquals(1L, returnedCustomer.getId());
        assertEquals("RegCode1", returnedCustomer.getRegistrationCode());
        assertEquals("Dmitrii", returnedCustomer.getName());
        assertEquals("dmitrii@gmail.com", returnedCustomer.getEmail());
        assertEquals("123-456-7890", returnedCustomer.getTelephone());
    }
}







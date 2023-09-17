package com.example.order_management_system.controller;

import com.example.order_management_system.model.Customer;
import com.example.order_management_system.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCustomer() throws Exception {

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .registrationCode("RegCode1")
                .name("Dmitrii")
                .email("dmitrii@gmail.com")
                .telephone("123-456-7890")
                .build();

        when(customerService.saveCustomer(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"registrationCode\":\"RegCode1\",\"name\":\"Dmitrii\",\"email\":\"dmitrii@gmail.com\",\"telephone\":\"123-456-7890\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationCode").value("RegCode1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dmitrii"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("dmitrii@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telephone").value("123-456-7890"));
    }
}
package com.example.order_management_system.controller;

import com.example.order_management_system.model.*;
import com.example.order_management_system.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveOrder() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        OrderRequest orderRequest = OrderRequest.builder()
                .orderLines(List.of())
                .customerId(1L)
                .date(LocalDate.now())
                .build();

        Order savedOrder = Order.builder().id(1L).build();

        when(orderService.saveOrder(orderRequest)).thenReturn(savedOrder);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetOrdersByDate() throws Exception {

        LocalDate date = LocalDate.of(2023, 9, 16);

        List<Order> ordersWithDate = new ArrayList<>();
        for (long i = 1L; i <= 4L; i++) {
            Order order = Order.builder()
                    .id(i)
                    .date(date)
                    .build();
            ordersWithDate.add(order);
        }

        when(orderService.findOrderByDate(date)).thenReturn(ordersWithDate);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/get/by/date")
                        .param("date", String.valueOf(date)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testSearchOrdersByProduct() throws Exception {

        Product product = Product.builder().id(1L).build();
        Order order1 = Order.builder().id(1L).build();
        Order order2 = Order.builder().id(2L).build();

        List<Order> ordersWithProduct = List.of(order1, order2);

        when(orderService.searchOrdersByProduct(product)).thenReturn(ordersWithProduct);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/get/by/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSearchOrdersByCustomer() throws Exception {

        Customer customer = Customer.builder().id(1L).build();
        Order order1 = Order.builder().id(1L).customer(customer).build();
        Order order2 = Order.builder().id(2L).customer(customer).build();

        List<Order> ordersWithCustomer = List.of(order1, order2);

        when(orderService.searchOrdersByCustomer(customer)).thenReturn(ordersWithCustomer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/get/by/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}






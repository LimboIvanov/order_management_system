package com.example.order_management_system.service;

import com.example.order_management_system.exception.CustomerNoSuchElementException;
import com.example.order_management_system.model.*;
import com.example.order_management_system.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderLineRepository orderLineRepository;

    @Mock
    private ProductRepository productRepository;

    private OrderService orderService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, orderLineRepository, customerRepository, productRepository);
    }

    @Test
    public void testSaveOrder() {

        OrderLineRequest orderLineRequest = OrderLineRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();
        OrderRequest orderRequest = OrderRequest.builder()
                .orderLines(List.of(orderLineRequest))
                .customerId(1L)
                .date(LocalDate.now())
                .build();

        Customer mockCustomer = new Customer();
        Product mockProduct = new Product();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Order mockOrder = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order savedOrder = orderService.saveOrder(orderRequest);

        verify(orderRepository).save(argThat(order -> order.getCustomer() == mockCustomer));

        assertEquals(mockOrder, savedOrder);
    }


    @Test
    public void testFindOrderByDate() {

        LocalDate date = LocalDate.of(2023, 9, 16);

        List<Order> ordersWithDate = new ArrayList<>();
        for (long i = 1L; i <= 4L; i++) {
            Order order = Order.builder()
                    .id(i)
                    .date(date)
                    .build();
            ordersWithDate.add(order);
        }

        when(orderRepository.findByDate(date)).thenReturn(ordersWithDate);

        List<Order> foundOrders = orderService.findOrderByDate(date);

        assertEquals(4, foundOrders.size());
    }


    @Test
    public void testSearchOrdersByProduct() {

        Product product = Product.builder().id(1L).build();
        Order order1 = Order.builder().id(1L).build();
        Order order2 = Order.builder().id(2L).build();

        List<Order> ordersWithProduct = List.of(order1, order2);

        when(orderRepository.findByProduct(product)).thenReturn(ordersWithProduct);

        List<Order> foundOrders = orderService.searchOrdersByProduct(product);

        assertEquals(2, foundOrders.size());
        assertEquals(1L, foundOrders.get(0).getId());
        assertEquals(2L, foundOrders.get(1).getId());
    }


    @Test
    public void testSearchOrdersByCustomer() {

        Customer customer = Customer.builder().id(1L).build();
        Order order1 = Order.builder().id(1L).customer(customer).build();
        Order order2 = Order.builder().id(2L).customer(customer).build();

        List<Order> ordersWithCustomer = List.of(order1, order2);

        when(orderRepository.findByCustomer(customer)).thenReturn(ordersWithCustomer);

        List<Order> foundOrders = orderService.searchOrdersByCustomer(customer);

        assertEquals(2, foundOrders.size());
        assertEquals(1L, foundOrders.get(0).getId());
        assertEquals(2L, foundOrders.get(1).getId());
    }


    @Test
    public void testSaveOrder_CustomerNoSuchElementException() {

        OrderLineRequest orderLineRequest = OrderLineRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();
        OrderRequest orderRequest = OrderRequest.builder()
                .orderLines(List.of(orderLineRequest))
                .customerId(1L)
                .date(LocalDate.now())
                .build();

        Product mockProduct = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNoSuchElementException.class, () -> orderService.saveOrder(orderRequest));
    }

}
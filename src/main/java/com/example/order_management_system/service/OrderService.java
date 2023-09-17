package com.example.order_management_system.service;

import com.example.order_management_system.exception.CustomerNoSuchElementException;
import com.example.order_management_system.exception.ProductNoSuchElementException;
import com.example.order_management_system.model.*;
import com.example.order_management_system.repository.CustomerRepository;
import com.example.order_management_system.repository.OrderLineRepository;
import com.example.order_management_system.repository.OrderRepository;
import com.example.order_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderLineRepository orderLineRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        super();
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }


    @Transactional
    public Order saveOrder(OrderRequest orderRequest){
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new CustomerNoSuchElementException("Customer not found with ID: " + orderRequest.getCustomerId()));

        Order order = Order.builder().date(orderRequest.getDate()).customer(customer).build();
        List<OrderLine> orderLines = new ArrayList<>(List.of());

        for (OrderLineRequest orderLineRequest : orderRequest.getOrderLines()) {
            Product product = productRepository.findById(orderLineRequest.getProductId())
                    .orElseThrow(() -> new ProductNoSuchElementException("Product not found with ID: " + orderLineRequest.getProductId()));
            OrderLine orderLine = OrderLine.builder().product(product).quantity(orderLineRequest.getQuantity()).order(order).build();
            orderLines.add(orderLine);
        }
        order.setOrderLines(orderLines);
        return orderRepository.save(order);
    }

    public List<Order> findOrderByDate(LocalDate date) {
        return orderRepository.findByDate(date);
    }

    public List<Order> searchOrdersByProduct(Product product) {
        return orderRepository.findByProduct(product);
    }

    public List<Order> searchOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    @Transactional
    public OrderLine updateOrderLineQuantity(Long orderLineId, int newQuantity) {
        Optional<OrderLine> optionalOrderLine = orderLineRepository.findById(orderLineId);

        if (optionalOrderLine.isPresent()) {
            OrderLine orderLine = optionalOrderLine.get();
            orderLine.setQuantity(newQuantity);
            return orderLineRepository.save(orderLine);
        }
        return null;
    }

}

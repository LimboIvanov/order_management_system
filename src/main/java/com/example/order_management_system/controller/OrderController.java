package com.example.order_management_system.controller;

import com.example.order_management_system.model.*;
import com.example.order_management_system.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        super();
        this.orderService = orderService;
    }


    @PostMapping()
    public ResponseEntity<Order> saveOrder(@RequestBody OrderRequest order){
        return new ResponseEntity<Order>(orderService.saveOrder(order), HttpStatus.CREATED);
    }


    @GetMapping("/get/by/date")
    public ResponseEntity<List<Order>> getOrdersByDate(@RequestParam LocalDate date) {
        return new ResponseEntity<List<Order>>(orderService.findOrderByDate(date), HttpStatus.OK);
    }


    @GetMapping("/get/by/product")
    public ResponseEntity<List<Order>> searchOrdersByProduct(@RequestBody Product product) {
        return new ResponseEntity<>(orderService.searchOrdersByProduct(product), HttpStatus.OK);
    }

    @GetMapping("/get/by/customer")
    public ResponseEntity<List<Order>> searchOrdersByCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(orderService.searchOrdersByCustomer(customer), HttpStatus.OK);
    }


    @PutMapping("/update/orderline/quantity")
    public ResponseEntity<OrderLine> updateOrderLineQuantity(
            @RequestParam Long orderLineId,
            @RequestParam int newQuantity
    ) {
        OrderLine updatedOrderLine = orderService.updateOrderLineQuantity(orderLineId, newQuantity);

        if (updatedOrderLine != null) {
            return new ResponseEntity<>(updatedOrderLine, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

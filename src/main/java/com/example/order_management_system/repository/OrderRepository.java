package com.example.order_management_system.repository;

import com.example.order_management_system.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByDate(LocalDate date);

    @Query("SELECT o FROM Order o JOIN o.orderLines ol WHERE ol.product = :product")
    List<Order> findByProduct(Product product);


    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    List<Order> findByCustomer(Customer customer);



}

package com.example.ECommerce.Repository;

import com.example.ECommerce.entityes.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}

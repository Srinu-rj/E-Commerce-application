package com.example.ECommerce.Repository;

import java.util.List;

import com.example.ECommerce.entityes.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
	
	@Query("SELECT o FROM Order o WHERE o.email = ?1 AND o.id = ?2")
	Order findOrderByEmailAndOrderId(String email, Long cartId);

	List<Order> findAllByEmail(String emailId);
	
}

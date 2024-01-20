package com.example.ECommerce.Repository;

import com.example.ECommerce.entityes.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long>{

}

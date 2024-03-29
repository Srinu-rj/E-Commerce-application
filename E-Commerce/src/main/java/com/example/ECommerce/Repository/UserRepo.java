package com.example.ECommerce.Repository;

import java.util.List;
import java.util.Optional;

import com.example.ECommerce.entityes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
	@Query("SELECT u FROM User u JOIN FETCH u.addresses a WHERE a.addressId = ?1")
	List<User> findByAddress(Long addressId);
	
	Optional<User> findByEmail(String email);
}

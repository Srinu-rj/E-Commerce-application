package com.example.ECommerce.Repository;

import com.example.ECommerce.entityes.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

}

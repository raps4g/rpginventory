package com.raps4g.rpginventory.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raps4g.rpginventory.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    
}

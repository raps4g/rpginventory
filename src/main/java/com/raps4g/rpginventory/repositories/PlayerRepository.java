package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.Player;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    Optional<Player> findByName(String name);

    boolean existsByName(String name);
}

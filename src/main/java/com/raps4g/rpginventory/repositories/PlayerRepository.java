package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.domain.entities.Player;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long>, 
                    PagingAndSortingRepository<Player, Long> {
    
    Optional<Player> findByName(String name);

    boolean existsByName(String name);
}

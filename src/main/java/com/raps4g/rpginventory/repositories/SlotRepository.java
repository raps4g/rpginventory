package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.Slot;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>{
    
    Optional<Slot> findByName(String name);

    boolean existsByName(String name);

}

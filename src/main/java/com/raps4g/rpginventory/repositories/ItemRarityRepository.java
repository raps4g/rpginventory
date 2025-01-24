package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.ItemRarity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRarityRepository extends JpaRepository<ItemRarity, Long>{

    Optional<ItemRarity> findByName(String name);

    boolean existsByName(String name);

}

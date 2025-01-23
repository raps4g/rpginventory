package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.ItemCategory;
import com.raps4g.rpginventory.model.ItemRarity;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRarityRepository extends CrudRepository<ItemRarity, Long>{

    Optional<ItemRarity> findByName(String name);

    boolean existsByName(String name);

}

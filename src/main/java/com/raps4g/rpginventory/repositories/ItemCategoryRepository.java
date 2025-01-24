package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.ItemCategory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long>{

    Optional<ItemCategory> findByName(String name);

    boolean existsByName(String name);

}

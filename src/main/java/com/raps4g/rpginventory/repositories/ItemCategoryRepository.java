package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.domain.entities.ItemCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository extends CrudRepository<ItemCategory, Long>{
}

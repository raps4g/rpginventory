package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.domain.entities.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long>{
}

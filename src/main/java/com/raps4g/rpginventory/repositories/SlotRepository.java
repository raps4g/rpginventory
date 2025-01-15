package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.domain.entities.Slot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends CrudRepository<Slot, Long>{
}

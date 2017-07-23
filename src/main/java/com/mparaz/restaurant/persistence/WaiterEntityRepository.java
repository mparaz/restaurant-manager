package com.mparaz.restaurant.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * WaiterEntity repository using Spring Data.
 */
public interface WaiterEntityRepository extends CrudRepository<WaiterEntity, Long> {
    Optional<WaiterEntity> findByName(String name);
}

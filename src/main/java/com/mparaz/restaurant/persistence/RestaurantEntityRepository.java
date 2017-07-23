package com.mparaz.restaurant.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * RestaurantEntity repository using Spring Data.
 */
public interface RestaurantEntityRepository extends CrudRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByName(String name);
}

package com.mparaz.restaurant.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * TableEntity repository using Spring Data.
 */
public interface TableEntityRepository extends CrudRepository<TableEntity, Long> {
    Optional<TableEntity> findByNumber(String number);
}

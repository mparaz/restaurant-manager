package com.mparaz.restaurant.service.impl;

import com.mparaz.restaurant.Restaurant;
import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;
import com.mparaz.restaurant.persistence.RestaurantEntity;
import com.mparaz.restaurant.persistence.RestaurantEntityRepository;
import com.mparaz.restaurant.persistence.TableEntity;
import com.mparaz.restaurant.persistence.TableEntityRepository;
import com.mparaz.restaurant.persistence.WaiterEntity;
import com.mparaz.restaurant.persistence.WaiterEntityRepository;
import com.mparaz.restaurant.service.PersistenceService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Persistence service.
 * <p>
 * Works with persistence layer and translates model objects to persistence entities.
 */
@Service
public class PersistenceServiceImpl implements PersistenceService {

    private final RestaurantEntityRepository restaurantEntityRepository;

    private final TableEntityRepository tableEntityRepository;

    private final WaiterEntityRepository waiterEntityRepository;

    public PersistenceServiceImpl(RestaurantEntityRepository restaurantEntityRepository,
                                  TableEntityRepository tableEntityRepository,
                                  WaiterEntityRepository waiterEntityRepository) {
        this.restaurantEntityRepository = restaurantEntityRepository;
        this.tableEntityRepository = tableEntityRepository;
        this.waiterEntityRepository = waiterEntityRepository;
    }

    @Override
    @Transactional
    public void assign(Table table, Waiter waiter) {
        final TableEntity tableEntity = createOrRetrieveTableEntity(table);
        final WaiterEntity waiterEntity = createOrRetrieveWaiterEntity(waiter);

        tableEntity.setWaiter(waiterEntity);
        waiterEntity.getTables().add(tableEntity);
    }

    private TableEntity createOrRetrieveTableEntity(Table table) {

        final Restaurant restaurant = table.getRestaurant();

        final Optional<RestaurantEntity> restaurantEntityOptional =
                restaurantEntityRepository.findByName(restaurant.getName());

        RestaurantEntity restaurantEntity;
        if (restaurantEntityOptional.isPresent()) {
            restaurantEntity = restaurantEntityOptional.get();
        } else {
            restaurantEntity = new RestaurantEntity();
            restaurantEntity.setName(restaurant.getName());
            restaurantEntity = restaurantEntityRepository.save(restaurantEntity);
        }

        final Optional<TableEntity> tableEntityOptional =
                tableEntityRepository.findByNumberAndRestaurant(table.getNumber(), restaurantEntity);

        TableEntity tableEntity;
        if (tableEntityOptional.isPresent()) {
            tableEntity = tableEntityOptional.get();
        } else {
            tableEntity = new TableEntity();
            tableEntity.setNumber(table.getNumber());

            tableEntity.setRestaurant(restaurantEntity);
            tableEntity = tableEntityRepository.save(tableEntity);

            restaurantEntity.getTables().add(tableEntity);
        }

        return tableEntity;
    }

    private WaiterEntity createOrRetrieveWaiterEntity(Waiter waiter) {
        final Optional<WaiterEntity> waiterEntityOptional = waiterEntityRepository.findByName(waiter.getName());
        WaiterEntity waiterEntity;

        if (waiterEntityOptional.isPresent()) {
            waiterEntity = waiterEntityOptional.get();
        } else {
            waiterEntity = new WaiterEntity();
            waiterEntity.setName(waiter.getName());
            waiterEntity = waiterEntityRepository.save(waiterEntity);
        }

        return waiterEntity;
    }

    // Methods for persistence setup.

    @Transactional
    public void createTable(Table table) {
        createOrRetrieveTableEntity(table);
    }

    @Transactional
    public void createWaiter(Waiter waiter) {
        createOrRetrieveWaiterEntity(waiter);
    }

    @Override
    public Set<Table> loadTables() {
        final Set<Table> result = new HashSet<>();

        for (final TableEntity tableEntity : tableEntityRepository.findAll()) {
            result.add(createTable(tableEntity));
        }

        return result;
    }

    @Override
    public Set<Waiter> loadWaiters() {
        final Set<Waiter> result = new HashSet<>();

        for (final WaiterEntity waiterEntity : waiterEntityRepository.findAll()) {
            result.add(createWaiter(waiterEntity));
        }

        return result;
    }

    @Override
    public Map<Waiter, Set<Table>> loadWaiterTableAssignments() {
        final Map<Waiter, Set<Table>> result = new HashMap<>();

        // Naive mapping from entity objects to model objects, without object reuse.
        for (final WaiterEntity waiterEntity : waiterEntityRepository.findAll()) {
            result.put(createWaiter(waiterEntity),
                    waiterEntity.getTables().stream().map(PersistenceServiceImpl::createTable)
                            .collect(Collectors.toSet()));
        }

        return result;
    }

    private static Waiter createWaiter(WaiterEntity waiterEntity) {
        return new Waiter(waiterEntity.getName());
    }

    private static Table createTable(TableEntity tableEntity) {
        return new Table(tableEntity.getNumber(), createRestaurant(tableEntity.getRestaurant()));
    }

    private static Restaurant createRestaurant(RestaurantEntity restaurantEntity) {
        return new Restaurant(restaurantEntity.getName());
    }
}

package com.mparaz.restaurant.service.impl;

import com.mparaz.restaurant.Restaurant;
import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;
import com.mparaz.restaurant.service.ManagementService;
import com.mparaz.restaurant.service.PersistenceService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Management Service implementation.
 */
@Service
public class ManagementServiceImpl implements ManagementService {

    private static final int MAX_TABLES_PER_RESTAURANT = 4;

    private final PersistenceService persistenceService;

    // Data is loaded from persistence, not at component construction but lazy-loaded at first use,
    // so that data can be stored into persistence before it is used.
    // Set initially to empty.

    private boolean loaded = false;

    private Set<Table> tables = Collections.emptySet();

    private Set<Waiter> waiters = Collections.emptySet();

    // Use two Maps for tracking assignments to avoid having to search.

    /**
     * Assignment tracking from waiter to tables.
     */
    private Map<Waiter, Set<Table>> waiterTableAssignments = Collections.emptyMap();

    /**
     * Assignment tracking from table to waiter.
     */
    private Map<Table, Waiter> tableWaiterAssignments = Collections.emptyMap();

    public ManagementServiceImpl(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    private void loadFromPersistence() {
        // Load from persistence.
        this.tables = persistenceService.loadTables();
        this.waiters = persistenceService.loadWaiters();

        this.waiterTableAssignments = persistenceService.loadWaiterTableAssignments();

        // Derive values.
        tableWaiterAssignments = new HashMap<>();
        for (final Map.Entry<Waiter, Set<Table>> entry: waiterTableAssignments.entrySet()) {
            for (final Table table: entry.getValue()) {
                tableWaiterAssignments.put(table, entry.getKey());
            }
        }
    }

    @Override
    public Set<Waiter> assignWaiter(Waiter waiter, Table table) {
        if (waiter == null) {
            throw new IllegalArgumentException("must provide waiter");
        }

        if (table == null) {
            throw new IllegalArgumentException("must provide table");
        }

        if (!loaded) {
            loadFromPersistence();
            loaded = true;
        }

        final Restaurant restaurant = table.getRestaurant();

        // Check the constraints and find other possible waiters. May be none.
        if (!checkConstraint(waiter, restaurant)) {
            return suggestOtherWaiters(waiter, restaurant);
        }

        // If the table was already in another's waiter's assignments, remove it from that waiter.
        final Waiter previousWaiter = tableWaiterAssignments.get(table);
        if ((previousWaiter != null) && (previousWaiter != waiter)) {
            final Set<Table> previousWaiterTableAssignments = waiterTableAssignments.get(previousWaiter);

            if (previousWaiterTableAssignments != null) {
                previousWaiterTableAssignments.remove(table);
            }
        }

        // In memory.
        tableWaiterAssignments.put(table, waiter);
        waiterTableAssignments.computeIfAbsent(waiter, waiter1 -> new HashSet<>()).add(table);

        // In persistence.
        persistenceService.assign(table, waiter);

        // Success
        return Collections.emptySet();
    }

    /**
     * Check the constraints for this waiter.
     *
     * @param waiter
     * @param restaurant
     * @return
     */
    private boolean checkConstraint(Waiter waiter, Restaurant restaurant) {
        // The constraint is to check if more than 4 tables already in a restaurant.
        // The check may be limited to this table's restaurant, and not all, since this is the one added.

        return isLessThanMaximumTables(waiterTableAssignments.get(waiter), restaurant);
    }

    /**
     * Find other waiters that meet the constraints. May be none.
     *
     * @param waiter
     * @param restaurant
     * @return
     */
    private Set<Waiter> suggestOtherWaiters(Waiter waiter, Restaurant restaurant) {
        return waiters.stream()
                .filter(w -> w != waiter)
                .filter(w -> isLessThanMaximumTables(waiterTableAssignments.get(w), restaurant))
                .collect(Collectors.toSet());
    }

    /**
     * Within maximum tables if no tables or less or equal to the maximum in the restaurant.
     *
     * @param tables
     * @param restaurant
     * @return
     */
    private static boolean isLessThanMaximumTables(Set<Table> tables, Restaurant restaurant) {
        return tables == null
                || tables.stream().filter(t -> restaurant.equals(t.getRestaurant())).count()
                < MAX_TABLES_PER_RESTAURANT;

    }

    @Override
    public Map<Table, Optional<Waiter>> displayAssignments(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("must provide restaurant");
        }

        if (!loaded) {
            loadFromPersistence();
            loaded = true;
        }

        // Go through the tables and pick up the assigned waiters, or empty if none.
        return tables.stream()
                .filter(table -> restaurant.equals(table.getRestaurant()))
                .collect(Collectors.toMap(Function.identity(), table ->
                Optional.ofNullable(tableWaiterAssignments.get(table))
        ));
    }

    @Override
    public Map<Waiter, Map<Restaurant, Set<Table>>> displayTables() {
        if (!loaded) {
            loadFromPersistence();
            loaded = true;
        }

        final Map<Waiter, Map<Restaurant, Set<Table>>> result = new HashMap<>();
        for (final Waiter waiter: waiterTableAssignments.keySet()) {
            result.put(waiter,
                waiterTableAssignments.getOrDefault(waiter, Collections.emptySet())
                    .stream().collect(Collectors.groupingBy(Table::getRestaurant, Collectors.toSet())));
        }

        return result;
    }

    @Override
    public Map<Restaurant, Set<Table>> displayTables(Waiter waiter) {
        if (waiter == null) {
            throw new IllegalArgumentException("must provide waiter");
        }

        if (!loaded) {
            loadFromPersistence();
            loaded = true;
        }

        // Go through the waiter to table assignments if available, and group by the restaurant.
        return waiterTableAssignments.getOrDefault(waiter, Collections.emptySet())
                .stream().collect(Collectors.groupingBy(Table::getRestaurant, Collectors.toSet()));
    }

    // Even the String-based methods need to load from persistence to work, before calling the actual method,
    // because the data is needed for the String lookups.

    /**
     * String based method for web API.
     */
    @Override
    public Set<Waiter> assignWaiter(String waiterName, String restaurantName, String tableNumber) {
        if (!loaded) {
            loadFromPersistence();
            loaded = true;
        }

        final Optional<Waiter> waiterOptional =
                waiters.stream().filter(waiter -> waiterName.equals(waiter.getName())).findFirst();

        if (!waiterOptional.isPresent()) {
            return Collections.emptySet();
        }

        final Optional<Table> tableOptional =
                tables.stream().filter(table -> (restaurantName.equals(table.getRestaurant().getName())
                && tableNumber.equals(table.getNumber()))).findFirst();

        if (!tableOptional.isPresent()) {
            return Collections.emptySet();
        }

        return assignWaiter(waiterOptional.get(), tableOptional.get());
    }

    /**
     * String based method for web API.
     */
    @Override
    public Map<Table, Optional<Waiter>> displayAssignments(String restaurantName) {
        if (!loaded) {
            loadFromPersistence();
            loaded = true;
        }

        final Optional<Restaurant> restaurantOptional =
                tables.stream().map(Table::getRestaurant)
                .filter(restaurant -> restaurantName.equals(restaurant.getName()))
                .findFirst();

        if (restaurantOptional.isPresent()) {
            return displayAssignments(restaurantOptional.get());
        } else {
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<Restaurant, Set<Table>> displayTables(String waiterName) {
        if (!loaded) {
            loadFromPersistence();
            loaded = true;
        }

        final Optional<Waiter> waiterOptional =
                waiters.stream().filter(waiter -> waiterName.equals(waiter.getName())).findFirst();

        if (!waiterOptional.isPresent()) {
            return Collections.emptyMap();
        }

        return displayTables(waiterOptional.get());
    }
}

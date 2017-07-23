package com.mparaz.restaurant.service.impl;

import com.mparaz.restaurant.Restaurant;
import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;
import com.mparaz.restaurant.service.ManagementService;

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
public class ManagementServiceImpl implements ManagementService {

    private static final int MAX_TABLES_PER_RESTAURANT = 4;

    private final Set<Restaurant> restaurants;

    private final Set<Table> tables;

    private final Set<Waiter> waiters;

    // Use two Maps for tracking assignments to avoid having to search.

    /**
     * Assignment tracking from waiter to tables.
     */
    private Map<Waiter, Set<Table>> waiterTableAssignments = new HashMap<>();

    /**
     * Assignment tracking from table to waiter.
     */
    private Map<Table, Waiter> tableWaiterAssignments = new HashMap<>();

    public ManagementServiceImpl(Set<Restaurant> restaurants, Set<Table> tables, Set<Waiter> waiters) {
        this.restaurants = restaurants;
        this.tables = tables;
        this.waiters = waiters;
    }

    @Override
    public Set<Waiter> assignWaiter(Waiter waiter, Table table) {
        if (waiter == null) {
            throw new IllegalArgumentException("must provide waiter");
        }

        if (table == null) {
            throw new IllegalArgumentException("must provide table");
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

        tableWaiterAssignments.put(table, waiter);
        waiterTableAssignments.computeIfAbsent(waiter, waiter1 -> new HashSet<>()).add(table);

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

        // Go through the tables and pick up the assigned waiters, or empty if none.
        return tables.stream()
                .filter(table -> restaurant.equals(table.getRestaurant()))
                .collect(Collectors.toMap(Function.identity(), table ->
                Optional.ofNullable(tableWaiterAssignments.get(table))
        ));
    }

    @Override
    public Map<Restaurant, Set<Table>> displayTables(Waiter waiter) {
        if (waiter == null) {
            throw new IllegalArgumentException("must provide waiter");
        }

        // Go through the waiter to table assignments if available, and group by the restaurant.
        return waiterTableAssignments.getOrDefault(waiter, Collections.emptySet())
                .stream().collect(Collectors.groupingBy(Table::getRestaurant, Collectors.toSet()));
    }
}

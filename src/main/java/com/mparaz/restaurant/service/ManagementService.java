package com.mparaz.restaurant.service;

import com.mparaz.restaurant.Restaurant;
import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Management service for assigning waiters to tables.
 */
public interface ManagementService {
    /**
     * Assign a waiter to a table if the table-restaurant constraint is matched.
     *
     * @param waiter
     * @param table
     * @return empty on success, or alternate waiters if not possible.
     */
    Set<Waiter> assignWaiter(Waiter waiter, Table table);

    /**
     * Assign a waiter, based on String parameters.
     *
     * @return empty on success, or alternate waiters if not possible.
     */
    Set<Waiter> assignWaiter(String waiterName, String restaurantName, String tableName);

    /**
     * Display the assignments between tables and their waiters.
     *
     * @param restaurant restaurant to display
     * @return Map of tables to waiters, or empty if no waiter assigned. All tables will be present.
     */
    Map<Table, Optional<Waiter>> displayAssignments(Restaurant restaurant);


    /**
     * Display assignments, based on a String parameter.
     * @param restaurantName
     * @return
     */
    Map<Table, Optional<Waiter>> displayAssignments(String restaurantName);

    /**
     * Display the tables for all waiters grouped by Restaurant.
     *
     * @return
     */
    Map<Waiter, Map<Restaurant, Set<Table>>> displayTables();

    /**
     * Display the tables for a waiter grouped by Restaurant.
     *
     * @param waiter
     * @return
     */
    Map<Restaurant, Set<Table>> displayTables(Waiter waiter);


    /**
     * Display the tables for a waiter grouped by Restaurant, based on String parameters.
     *
     * @param waiterName
     * @return
     */
    Map<Restaurant, Set<Table>> displayTables(String waiterName);
}

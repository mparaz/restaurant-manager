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
     * Display the assignments between tables and their waiters.
     *
     * @param restaurant restaurant to display
     * @return Map of tables to waiters, or empty if no waiter assigned.
     */
    Map<Table, Set<Optional<Waiter>>> displayAssignments(Restaurant restaurant);

    /**
     * Display the tables for a waiter.
     *
     * @param waiter
     * @return
     */
    Set<Table> displayTables(Waiter waiter);
}

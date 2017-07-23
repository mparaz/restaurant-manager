package com.mparaz.restaurant.service;

import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;

import java.util.Map;
import java.util.Set;

/**
 * Persistence service, hiding away persistence details.
 */
public interface PersistenceService {
    /**
     * Assign a table to a waiter.
     * @param table
     * @param waiter
     */
    void assign(Table table, Waiter waiter);

    /**
     * Create a table, and its Restaurant if needed.
     * @param table
     */
    void createTable(Table table);

    /**
     * Create a waiter.
     * @param waiter
     */
    void createWaiter(Waiter waiter);

    /**
     * Load tables.
     *
     */
    Set<Table> loadTables();

    /**
     * Load waiters.
     */
    Set<Waiter> loadWaiters();

    /**
     * Load waiters and their table assignments.
     * @return
     */
    Map<Waiter, Set<Table>> loadWaiterTableAssignments();
}

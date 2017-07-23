package com.mparaz.restaurant.service.impl;

import com.mparaz.restaurant.Restaurant;
import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;
import com.mparaz.restaurant.service.ManagementService;
import com.mparaz.restaurant.service.PersistenceService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Integration tests for Management Service.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ManagementServiceImplIntegrationTest {

    private PersistenceService persistenceService;

    private ManagementService managementService;

    @Test
    public void shouldAssignWaiterToTableWithOnlyOneWaiter() {
        final Waiter waiter = new Waiter("Waiter 1");
        final Restaurant restaurant = new Restaurant("Restaurant 1");

        final Table table = new Table("table1", restaurant);

        // Persist the initial state of tables/restaurants and waiters.
        persistenceService.createTable(table);
        persistenceService.createWaiter(waiter);

        Assert.assertThat(managementService.assignWaiter(waiter, table), Matchers.is(Collections.emptySet()));

        // Check memory

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table, Optional.of(waiter));
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables = new HashMap<>();
        expectedRestaurantTables.put(restaurant, Collections.singleton(table));

        Assert.assertThat(managementService.displayTables(waiter), Matchers.is(expectedRestaurantTables));

        // Check persistence
        final Map<Waiter, Set<Table>> waiterTableAssignments = persistenceService.loadWaiterTableAssignments();

        Assert.assertThat(waiterTableAssignments.get(waiter), Matchers.is(Collections.singleton(table)));
    }

    @Autowired
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Autowired
    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }
}

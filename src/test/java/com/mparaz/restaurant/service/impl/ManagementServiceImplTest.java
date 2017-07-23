package com.mparaz.restaurant.service.impl;

import com.mparaz.restaurant.Restaurant;
import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;
import com.mparaz.restaurant.service.PersistenceService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Unit tests for Management Service.
 */
public class ManagementServiceImplTest {

    @Test
    public void shouldHaveNoAssignments() {

        // Use mocks to model the objects as to not depend on any getter behaviour.

        final Waiter waiter = Mockito.mock(Waiter.class);
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Table table = Mockito.mock(Table.class);

        Mockito.when(table.getRestaurant()).thenReturn(restaurant);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(Collections.singleton(table));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(Collections.singleton(waiter));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);

        // Do not assign.

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table, Optional.empty());
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        Assert.assertThat(managementService.displayTables(waiter), Matchers.is(Collections.emptyMap()));
    }

    @Test
    public void shouldAssignWaiterToTableWithOnlyOneWaiter() {

        final Waiter waiter = Mockito.mock(Waiter.class);
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Table table = Mockito.mock(Table.class);

        Mockito.when(table.getRestaurant()).thenReturn(restaurant);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(Collections.singleton(table));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(Collections.singleton(waiter));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);

        Assert.assertThat(managementService.assignWaiter(waiter, table), Matchers.is(Collections.emptySet()));

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table, Optional.of(waiter));
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables = new HashMap<>();
        expectedRestaurantTables.put(restaurant, Collections.singleton(table));

        Assert.assertThat(managementService.displayTables(waiter), Matchers.is(expectedRestaurantTables));
    }

    @Test
    public void shouldAssignOneWaiterToTwoTablesWithTwoWaiters() {

        final Waiter waiter1 = Mockito.mock(Waiter.class);
        final Waiter waiter2 = Mockito.mock(Waiter.class);
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Table table1 = Mockito.mock(Table.class);
        final Table table2 = Mockito.mock(Table.class);

        Mockito.when(table1.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table2.getRestaurant()).thenReturn(restaurant);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(new HashSet<>(Arrays.asList(table1, table2)));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(new HashSet<>(Arrays.asList(waiter1, waiter2)));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);


        Assert.assertThat(managementService.assignWaiter(waiter1, table1), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table2), Matchers.is(Collections.emptySet()));

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table1, Optional.of(waiter1));
        expectedTableWaiters.put(table2, Optional.of(waiter1));
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables = new HashMap<>();
        expectedRestaurantTables.put(restaurant, new HashSet<>(Arrays.asList(table1, table2)));

        Assert.assertThat(managementService.displayTables(waiter1), Matchers.is(expectedRestaurantTables));
        Assert.assertThat(managementService.displayTables(waiter2), Matchers.is(Collections.emptyMap()));
    }

    @Test
    public void shouldAssignOneWaiterToOneTableWithTwoWaitersAndThenReplace() {

        final Waiter waiter1 = Mockito.mock(Waiter.class);
        final Waiter waiter2 = Mockito.mock(Waiter.class);
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Table table1 = Mockito.mock(Table.class);
        final Table table2 = Mockito.mock(Table.class);

        Mockito.when(table1.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table2.getRestaurant()).thenReturn(restaurant);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(new HashSet<>(Arrays.asList(table1, table2)));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(new HashSet<>(Arrays.asList(waiter1, waiter2)));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);

        Assert.assertThat(managementService.assignWaiter(waiter1, table1), Matchers.is(Collections.emptySet()));

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table1, Optional.of(waiter1));
        expectedTableWaiters.put(table2, Optional.empty());
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables = new HashMap<>();
        expectedRestaurantTables.put(restaurant, Collections.singleton(table1));

        Assert.assertThat(managementService.displayTables(waiter1), Matchers.is(expectedRestaurantTables));
        Assert.assertThat(managementService.displayTables(waiter2), Matchers.is(Collections.emptyMap()));

        Assert.assertThat(managementService.assignWaiter(waiter2, table1), Matchers.is(Collections.emptySet()));

        final Map<Table, Optional<Waiter>> expectedTableWaiters2 = new HashMap<>();
        expectedTableWaiters2.put(table1, Optional.of(waiter2));
        expectedTableWaiters2.put(table2, Optional.empty());
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters2));

        Assert.assertThat(managementService.displayTables(waiter1), Matchers.is(Collections.emptyMap()));
        Assert.assertThat(managementService.displayTables(waiter2), Matchers.is(expectedRestaurantTables));
    }

    @Test
    public void shouldAssignTwoWaitersToOneTableEachWithTwoWaiters() {

        // waiter1 is already assigned to 4 tables, while waiter2 has none.

        final Waiter waiter1 = Mockito.mock(Waiter.class);
        final Waiter waiter2 = Mockito.mock(Waiter.class);
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Table table1 = Mockito.mock(Table.class);
        final Table table2 = Mockito.mock(Table.class);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(new HashSet<>(Arrays.asList(table1, table2)));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(new HashSet<>(Arrays.asList(waiter1, waiter2)));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        Mockito.when(table1.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table2.getRestaurant()).thenReturn(restaurant);

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);

        Assert.assertThat(managementService.assignWaiter(waiter1, table1), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter2, table2), Matchers.is(Collections.emptySet()));

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table1, Optional.of(waiter1));
        expectedTableWaiters.put(table2, Optional.of(waiter2));
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables1 = new HashMap<>();
        expectedRestaurantTables1.put(restaurant, Collections.singleton(table1));
        final Map<Restaurant, Set<Table>> expectedRestaurantTables2 = new HashMap<>();
        expectedRestaurantTables2.put(restaurant, Collections.singleton(table2));

        Assert.assertThat(managementService.displayTables(waiter1), Matchers.is(expectedRestaurantTables1));
        Assert.assertThat(managementService.displayTables(waiter2), Matchers.is(expectedRestaurantTables2));
    }

    @Test
    public void shouldNotAssignMoreThanFourTablesInTheSameRestaurantAndSuggest() {

        final Waiter waiter1 = Mockito.mock(Waiter.class);
        final Waiter waiter2 = Mockito.mock(Waiter.class);
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Table table1 = Mockito.mock(Table.class);
        final Table table2 = Mockito.mock(Table.class);
        final Table table3 = Mockito.mock(Table.class);
        final Table table4 = Mockito.mock(Table.class);
        final Table table5 = Mockito.mock(Table.class);

        Mockito.when(table1.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table2.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table3.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table4.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table5.getRestaurant()).thenReturn(restaurant);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(new HashSet<>(Arrays.asList(table1, table2, table3,
                table4, table5)));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(new HashSet<>(Arrays.asList(waiter1, waiter2)));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);

        // Assign 4 tables to waiter1, and then don't assign the 5th but suggest waiter2
        Assert.assertThat(managementService.assignWaiter(waiter1, table1), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table2), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table3), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table4), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table5), Matchers.is(Collections.singleton(waiter2)));

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table1, Optional.of(waiter1));
        expectedTableWaiters.put(table2, Optional.of(waiter1));
        expectedTableWaiters.put(table3, Optional.of(waiter1));
        expectedTableWaiters.put(table4, Optional.of(waiter1));
        expectedTableWaiters.put(table5, Optional.empty());
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables1 = new HashMap<>();
        expectedRestaurantTables1.put(restaurant, new HashSet<>(Arrays.asList(table1, table2, table3, table4)));

        Assert.assertThat(managementService.displayTables(waiter1), Matchers.is(expectedRestaurantTables1));
        Assert.assertThat(managementService.displayTables(waiter2), Matchers.is(Collections.emptyMap()));
    }

    @Test
    public void shouldAssignMoreThanFourTablesInTheDifferentRestaurants() {

        final Waiter waiter1 = Mockito.mock(Waiter.class);
        final Waiter waiter2 = Mockito.mock(Waiter.class);

        final Restaurant restaurant1 = Mockito.mock(Restaurant.class);
        final Restaurant restaurant2 = Mockito.mock(Restaurant.class);

        final Table table1 = Mockito.mock(Table.class);
        final Table table2 = Mockito.mock(Table.class);
        final Table table3 = Mockito.mock(Table.class);
        final Table table4 = Mockito.mock(Table.class);
        final Table table5 = Mockito.mock(Table.class);

        Mockito.when(table1.getRestaurant()).thenReturn(restaurant1);
        Mockito.when(table2.getRestaurant()).thenReturn(restaurant1);
        Mockito.when(table3.getRestaurant()).thenReturn(restaurant1);
        Mockito.when(table4.getRestaurant()).thenReturn(restaurant1);

        // Since table5 will be in restaurant2, it is assignable.
        Mockito.when(table5.getRestaurant()).thenReturn(restaurant2);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(new HashSet<>(Arrays.asList(table1, table2, table3,
                table4, table5)));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(new HashSet<>(Arrays.asList(waiter1, waiter2)));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);

        // Assign 4 tables to waiter1 in restaurant1, and then another in restaurant2
        Assert.assertThat(managementService.assignWaiter(waiter1, table1), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table2), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table3), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table4), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table5), Matchers.is(Collections.emptySet()));

        final Map<Table, Optional<Waiter>> expectedTableWaiters1 = new HashMap<>();
        expectedTableWaiters1.put(table1, Optional.of(waiter1));
        expectedTableWaiters1.put(table2, Optional.of(waiter1));
        expectedTableWaiters1.put(table3, Optional.of(waiter1));
        expectedTableWaiters1.put(table4, Optional.of(waiter1));
        Assert.assertThat(managementService.displayAssignments(restaurant1), Matchers.is(expectedTableWaiters1));

        final Map<Table, Optional<Waiter>> expectedTableWaiters2 = new HashMap<>();
        expectedTableWaiters2.put(table5, Optional.of(waiter1));
        Assert.assertThat(managementService.displayAssignments(restaurant2), Matchers.is(expectedTableWaiters2));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables1 = new HashMap<>();
        expectedRestaurantTables1.put(restaurant1, new HashSet<>(Arrays.asList(table1, table2, table3, table4)));
        expectedRestaurantTables1.put(restaurant2, Collections.singleton(table5));

        Assert.assertThat(managementService.displayTables(waiter1), Matchers.is(expectedRestaurantTables1));
        Assert.assertThat(managementService.displayTables(waiter2), Matchers.is(Collections.emptyMap()));
    }

    @Test
    public void shouldNotAssignMoreThanFourTablesInTheSameRestaurantAndNoSuggestions() {

        final Waiter waiter1 = Mockito.mock(Waiter.class);
        final Restaurant restaurant = Mockito.mock(Restaurant.class);

        final Table table1 = Mockito.mock(Table.class);
        final Table table2 = Mockito.mock(Table.class);
        final Table table3 = Mockito.mock(Table.class);
        final Table table4 = Mockito.mock(Table.class);
        final Table table5 = Mockito.mock(Table.class);

        Mockito.when(table1.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table2.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table3.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table4.getRestaurant()).thenReturn(restaurant);
        Mockito.when(table5.getRestaurant()).thenReturn(restaurant);

        final PersistenceService persistenceService = Mockito.mock(PersistenceService.class);

        Mockito.when(persistenceService.loadTables()).thenReturn(new HashSet<>(Arrays.asList(table1, table2, table3,
                table4, table5)));
        Mockito.when(persistenceService.loadWaiters()).thenReturn(new HashSet<>(Collections.singletonList(waiter1)));
        Mockito.when(persistenceService.loadWaiterTableAssignments()).thenReturn(new HashMap<>());

        final ManagementServiceImpl managementService = new ManagementServiceImpl(persistenceService);

        // Assign 4 tables to waiter1, and then don't assign the 5th but no suggestions.
        Assert.assertThat(managementService.assignWaiter(waiter1, table1), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table2), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table3), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table4), Matchers.is(Collections.emptySet()));
        Assert.assertThat(managementService.assignWaiter(waiter1, table5), Matchers.is(Collections.emptySet()));

        final Map<Table, Optional<Waiter>> expectedTableWaiters = new HashMap<>();
        expectedTableWaiters.put(table1, Optional.of(waiter1));
        expectedTableWaiters.put(table2, Optional.of(waiter1));
        expectedTableWaiters.put(table3, Optional.of(waiter1));
        expectedTableWaiters.put(table4, Optional.of(waiter1));
        expectedTableWaiters.put(table5, Optional.empty());
        Assert.assertThat(managementService.displayAssignments(restaurant), Matchers.is(expectedTableWaiters));

        final Map<Restaurant, Set<Table>> expectedRestaurantTables1 = new HashMap<>();
        expectedRestaurantTables1.put(restaurant, new HashSet<>(Arrays.asList(table1, table2, table3, table4)));

        Assert.assertThat(managementService.displayTables(waiter1), Matchers.is(expectedRestaurantTables1));
    }
}

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
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Integration tests for persistence.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PersistenceServiceImplIntegrationTest {

    private PersistenceService persistenceService;

    private RestaurantEntityRepository restaurantEntityRepository;

    private TableEntityRepository tableEntityRepository;

    private WaiterEntityRepository waiterEntityRepository;

    @Test
    public void shouldPersist() {
        final Restaurant restaurant = new Restaurant("Test restaurant");

        final Table table = new Table("Test123", restaurant);

        final Waiter waiter = new Waiter("Test waiter");

        persistenceService.assign(table, waiter);

        // Verify using direct persistence.
        final Optional<RestaurantEntity> testRestaurant = restaurantEntityRepository.findByName("Test restaurant");
        Assert.assertTrue(testRestaurant.isPresent());
        Assert.assertThat(testRestaurant.get().getTables().size(), Matchers.is(1));
        Assert.assertThat(testRestaurant.get().getTables().iterator().next().getNumber(), Matchers.is("Test123"));

        final Optional<TableEntity> test123 = tableEntityRepository.findByNumber("Test123");
        Assert.assertTrue(test123.isPresent());
        Assert.assertThat(test123.get().getRestaurant().getName(), Matchers.is("Test restaurant"));

        final Optional<WaiterEntity> testWaiter = waiterEntityRepository.findByName("Test waiter");
        Assert.assertTrue(testWaiter.isPresent());
        Assert.assertThat(testWaiter.get().getTables().size(), Matchers.is(1));
        Assert.assertThat(testWaiter.get().getTables().iterator().next().getNumber(), Matchers.is("Test123"));
    }

    @Test
    public void shouldLoadWaitersAndTableAssignments() {

        final Restaurant restaurant = new Restaurant("Test restaurant");

        final Table table = new Table("Test123", restaurant);

        final Waiter waiter = new Waiter("Test waiter");

        persistenceService.assign(table, waiter);

        final Map<Waiter, Set<Table>> waiterTableAssignments = persistenceService.loadWaiterTableAssignments();

        Assert.assertThat(waiterTableAssignments.get(waiter), Matchers.is(Collections.singleton(table)));
    }


    @Autowired
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Autowired
    public void setRestaurantEntityRepository(RestaurantEntityRepository restaurantEntityRepository) {
        this.restaurantEntityRepository = restaurantEntityRepository;
    }

    @Autowired
    public void setTableEntityRepository(TableEntityRepository tableEntityRepository) {
        this.tableEntityRepository = tableEntityRepository;
    }

    @Autowired
    public void setWaiterEntityRepository(WaiterEntityRepository waiterEntityRepository) {
        this.waiterEntityRepository = waiterEntityRepository;
    }
}

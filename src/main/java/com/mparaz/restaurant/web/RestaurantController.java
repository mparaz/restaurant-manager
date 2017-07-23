package com.mparaz.restaurant.web;

import com.mparaz.restaurant.Restaurant;
import com.mparaz.restaurant.Table;
import com.mparaz.restaurant.Waiter;
import com.mparaz.restaurant.service.ManagementService;
import com.mparaz.restaurant.service.PersistenceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Web controller for the Restaurant Management System.
 */
@RestController
public class RestaurantController {

    private static final int RESTAURANTS = 2;

    private static final int TABLES_PER_RESTAURANT = 20;

    private static final int WAITERS = 8;

    private final ManagementService managementService;

    private final PersistenceService persistenceService;

    public RestaurantController(ManagementService managementService,
                                PersistenceService persistenceService) {
        this.managementService = managementService;
        this.persistenceService = persistenceService;
    }

    /**
     * Sets up the data, as it's simpler than doing it from startup.
     */
    @PostMapping("/setup")
    public void setup() {
        for (int i = 1; i <= RESTAURANTS; i++) {
            final Restaurant restaurant = new Restaurant("Restaurant" + i);

            for (int j = 1; j <= TABLES_PER_RESTAURANT; j++) {
                final Table table = new Table("Table" + j, restaurant);

                persistenceService.createTable(table);
            }
        }

        for (int i = 1; i <= WAITERS; i++) {
            final Waiter waiter = new Waiter("Waiter" + i);
            persistenceService.createWaiter(waiter);
        }
    }

    /**
     * HTTP GET for assignments.
     */
    @GetMapping("/assignments/{restaurantName}")
    public Map<Table, Set<Waiter>> displayAssignments(@PathVariable String restaurantName) {
        // Extra mapping needed since Optional does not render to JSON properly.
        return managementService.displayAssignments(restaurantName)
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().map(Collections::singleton).orElseGet(Collections::emptySet)));
    }

    /**
     * HTTP GET for tables for one waiter.
     */
    @GetMapping("/tables/{waiterName}")
    public Map<Restaurant, Set<Table>> displayTables(@PathVariable String waiterName) {
        return managementService.displayTables(waiterName);
    }

    /**
     * HTTP GET for tables for all waiters.
     */
    @GetMapping("/tables")
    public Map<Waiter, Map<Restaurant, Set<Table>>> displayTables() {
        return managementService.displayTables();
    }

    /**
     * HTTP POST for assignment.
     */
    @PostMapping("/assign/{waiterName}/{restaurantName}/{tableNumber}")
    public Set<Waiter> assignWaiter(@PathVariable String waiterName,
                                    @PathVariable String restaurantName,
                                    @PathVariable String tableNumber) {
        return managementService.assignWaiter(waiterName, restaurantName, tableNumber);
    }
}

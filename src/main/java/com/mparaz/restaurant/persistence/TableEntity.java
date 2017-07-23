package com.mparaz.restaurant.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Table model entity.
 */
@Entity(name = "restaurant_table")
public class TableEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The table number is a String since it is not used as a number, and may contain letters such as "1A".
     */
    private String number;

    /**
     * Many tables belong to one restaurant.
     */
    @ManyToOne
    private RestaurantEntity restaurant;

    @ManyToOne
    private WaiterEntity waiter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    public WaiterEntity getWaiter() {
        return waiter;
    }

    public void setWaiter(WaiterEntity waiter) {
        this.waiter = waiter;
    }
}

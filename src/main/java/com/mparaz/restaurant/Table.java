package com.mparaz.restaurant;

/**
 * The table model.
 */
public class Table {
    /**
     * The table number. Always present.
     */
    private final String number;

    /**
     * The restaurant the table belongs to. Always present.
     */
    private final Restaurant restaurant;

    public Table(String number, Restaurant restaurant) {
        this.number = number;
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getNumber() {
        return number;
    }
}

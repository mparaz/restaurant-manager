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
        if (number == null) {
            throw new IllegalArgumentException("table must have a number");
        }

        if (restaurant == null) {
            throw new IllegalArgumentException("table must have a Restaurant");
        }

        this.number = number;
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Table)) {
            return false;
        }

        final Table table = (Table) o;

        if (!number.equals(table.number)) {
            return false;
        }

        return restaurant.equals(table.restaurant);
    }

    private static final int PRIME = 31;

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = PRIME * result + restaurant.hashCode();
        return result;
    }
}

package com.mparaz.restaurant;

/**
 * The restaurant model.
 */
public class Restaurant {
    private final String name;

    public Restaurant(String name) {
        if (name == null) {
            throw new IllegalArgumentException("restaurant must have a name");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Restaurant{"
                + "name='" + name + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Restaurant)) {
            return false;
        }

        Restaurant that = (Restaurant) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

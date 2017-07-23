package com.mparaz.restaurant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The restaurant model.
 */
public class Restaurant {
    private final String name;

    private final Set<Table> tables;

    public Restaurant(String name, Set<Table> tables) {
        if (name == null) {
            throw new IllegalArgumentException("restaurant must have a name");
        }

        if (tables == null) {
            throw new IllegalArgumentException("restaurant must have tables");
        }

        this.name = name;

        // Defensive copy coming in.
        this.tables = new HashSet<>(tables);
    }

    public String getName() {
        return name;
    }

    public Set<Table> getTables() {
        // Defensive copy going out.
        return Collections.unmodifiableSet(tables);
    }
}

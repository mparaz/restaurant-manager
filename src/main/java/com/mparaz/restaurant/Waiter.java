package com.mparaz.restaurant;

/**
 * The waiter model.
 */
public class Waiter {
    private final String name;

    public Waiter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("waiter must have a name");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Waiter)) {
            return false;
        }

        final Waiter waiter = (Waiter) o;

        return name.equals(waiter.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

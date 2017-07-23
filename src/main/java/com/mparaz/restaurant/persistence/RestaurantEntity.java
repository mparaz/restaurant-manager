package com.mparaz.restaurant.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Restaurant, persistence entity.
 */
@Entity(name = "restaurant")
public class RestaurantEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    /**
     * One restaurant has many tables.
     */
    @OneToMany
    private Set<TableEntity> tables = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TableEntity> getTables() {
        return tables;
    }

    public void setTables(Set<TableEntity> tables) {
        this.tables = tables;
    }
}

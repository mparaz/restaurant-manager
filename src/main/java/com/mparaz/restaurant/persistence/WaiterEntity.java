package com.mparaz.restaurant.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Waiter model entity.
 */
@Entity(name = "waiter")
public class WaiterEntity {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Full name.
     */
    @Column(unique = true)
    private String name;

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

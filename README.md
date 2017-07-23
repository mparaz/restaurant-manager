Restaurant Manager Coding Challenge
===================================

Running
-------

To start:

```
./gradlew bootRun
```

To set up the initial database:

```
curl -X POST http://localhost:8080/setup
```

To assign a waiter to a restaurant and table.
Returns a set containing alternative waiters if assigning is no longer possible.
```
curl -X POST http://localhost:8080/assign/Waiter1/Restaurant1/Table1
```

To view assignments for a restaurant (manager view): 
```
curl http://localhost:8080/assignments/Restaurant1
```

To view tables for all waiters or a specific waiter:
```

curl http://localhost:8080/tables

curl http://localhost:8080/tables/Waiter

```


Design
------
Persistence layer contains the persistence-layer facing model objects, but no logic.

Service layer provides the logic.

Assumed functionality
---------------------
Assigning a waiter to a table will unassign the previously assigned waiter.

If the constraint of not assigning more than 4 tables to a waiter is not met, 
but there are no available waiters, then no suggestions or assignments are made.

Model
-----
Model objects do not have persistence logic.
Model objects are served directly to the API, without separate RESTful/JSON objects

Waiter name must be unique. 
In a real implementation, the waiter would need an employee ID or similar identifier.  

The hotel and manager are not represented as models because this is 
not a multi-tenant application.

Model objects are immutable since they are not changed after initialisation,
Model objects do not perform any logic.

Service
-------

`ManagementService` provides the core logic.
`PersistenceService` provides a facade to the Spring Data JPA persistence layer.

Persistence Model
-----------------
Embedded H2 database using `~/restaurantmanagerdb`

All entities use auto-generated surrogate keys.


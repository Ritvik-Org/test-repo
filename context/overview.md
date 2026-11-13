# test-repo

A Spring Boot REST API for managing `Item` entities, backed by PostgreSQL.

## Stack

- **Java 21**
- **Spring Boot 4.1.1** — web (Spring MVC), data (Spring Data JPA)
- **PostgreSQL** — primary database (`localhost:5432/mcptest`)
- **Maven** — build tool (`./mvnw`)

## Package structure

```
com.example.mcptest.map_test
├── MapTestApplication.java       # entry point
├── controller/
│   └── ItemController.java       # REST layer — /api/items
├── service/
│   └── ItemService.java          # business logic
├── repository/
│   └── ItemRepository.java       # JpaRepository<Item, Long>
└── entity/
    └── Item.java                 # JPA entity — table: items
```

## API surface

Base path: `/api/items`

| Method | Path          | Description         |
|--------|---------------|---------------------|
| POST   | `/api/items`  | Create an item      |
| GET    | `/api/items`  | List all items      |
| GET    | `/api/items/{id}` | Get item by ID  |
| PUT    | `/api/items/{id}` | Update item     |
| DELETE | `/api/items/{id}` | Delete item     |

## Item schema

| Field       | Type   | Nullable | Notes            |
|-------------|--------|----------|------------------|
| id          | Long   | no       | auto-generated   |
| name        | String | no       | `NOT NULL`       |
| description | String | yes      |                  |
| price       | Double | yes      |                  |

## Database config

```
url:      jdbc:postgresql://localhost:5432/mcptest
username: ritvikr
ddl-auto: update   (Hibernate auto-migrates schema)
```

## Running locally

```bash
./mvnw spring-boot:run
```

Requires PostgreSQL running on `localhost:5432` with database `mcptest` and user `ritvikr`.

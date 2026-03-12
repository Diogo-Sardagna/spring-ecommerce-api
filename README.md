# spring-ecommerce-api

![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)
![Status](https://img.shields.io/badge/status-in_development-yellow?style=flat-square)

Backend REST API for an e-commerce platform, responsible for managing users, product catalog, shopping cart, and order processing. The project applies layered architecture principles with clear separation between the HTTP, business logic, and data access layers.

Built with Spring Boot 3, Spring Data JPA, and PostgreSQL. Upcoming modules will introduce containerization with Docker, asynchronous messaging with Apache Kafka, and orchestration with Kubernetes.

---

## Why This Architecture

The application is structured in four well-defined layers — Controller, Service, Repository, and Model — following a separation of concerns approach that makes the codebase easier to test, maintain, and scale.

DTOs are used to decouple the internal domain model from the API contract, preventing implementation details from leaking into request and response payloads. This also allows the API surface to evolve independently of the persistence model.

Spring Data JPA was chosen to reduce boilerplate in data access while still allowing custom queries when needed. PostgreSQL was selected as the production database for its reliability and compatibility with JPA.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Security | Spring Security |
| Build tool | Maven |
| Containerization | Docker (in progress) |
| Messaging | Apache Kafka (upcoming) |
| Orchestration | Kubernetes (upcoming) |

---

## Project Structure

```
src/main/java/com/app/ecom/
├── controller/     # REST endpoints — handles HTTP requests and delegates to services
├── dto/            # Request and response objects — decouples API from domain model
├── model/          # JPA entities — mapped to database tables
├── repository/     # Data access interfaces using Spring Data JPA
└── service/        # Business logic — orchestrates repositories and enforces rules
```

---

## API Overview

### Users
| Method | Endpoint | Description |
|---|---|---|
| POST | `/users/register` | Register a new user |
| GET | `/users/{id}` | Retrieve user by ID |
| PUT | `/users/{id}` | Update user data |
| DELETE | `/users/{id}` | Remove a user |

### Products
| Method | Endpoint | Description |
|---|---|---|
| POST | `/products` | Create a new product |
| GET | `/products` | List all products |
| GET | `/products/{id}` | Get product details |
| PUT | `/products/{id}` | Update product |
| DELETE | `/products/{id}` | Remove product |

### Cart
| Method | Endpoint | Description |
|---|---|---|
| POST | `/cart` | Add item to cart |
| GET | `/cart/{userId}` | Get cart for a user |
| DELETE | `/cart/{itemId}` | Remove item from cart |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| POST | `/orders` | Place a new order |
| GET | `/orders/{id}` | Get order details |
| GET | `/orders/user/{userId}` | List orders by user |
| PATCH | `/orders/{id}/status` | Update order status |

---

## Domain Model

```
User (UserRole)
 └── Order (OrderStatus)
      └── OrderItem
           └── Product
User
 └── CartItem
      └── Product
Order
 └── Address
```

| Entity | Description |
|---|---|
| `User` | Platform user with role-based access (ADMIN, CUSTOMER) |
| `Product` | Catalog item with price and stock information |
| `CartItem` | Transient association between a user and a product before checkout |
| `Order` | Confirmed purchase with status lifecycle |
| `OrderItem` | Snapshot of a product at the time of purchase |
| `Address` | Shipping destination attached to an order |
| `OrderStatus` | Enum: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED |
| `UserRole` | Enum: ADMIN, CUSTOMER |

---

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker and Docker Compose

### Steps

```bash
# Clone the repository
git clone https://github.com/Diogo-Sardagna/spring-ecommerce-api.git
cd spring-ecommerce-api

# Start PostgreSQL with Docker
docker-compose up -d

# Run the application
./mvnw spring-boot:run
```

API available at: `http://localhost:8080`

### Environment Variables

| Variable | Description | Default |
|---|---|---|
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/ecomdb` |
| `DB_USERNAME` | Database user | `postgres` |
| `DB_PASSWORD` | Database password | — |

---

## Roadmap

- [x] Layered REST API with Spring Boot
- [x] JPA entities and repositories
- [x] Service layer with business logic
- [x] DTOs for request/response separation
- [ ] Docker and Docker Compose setup
- [ ] Spring Cloud Gateway integration
- [ ] Apache Kafka for async order events
- [ ] Distributed tracing with Zipkin
- [ ] Kubernetes deployment manifests
- [ ] JWT authentication with Spring Security

---

## Author

**Diogo Sardagna**  
Backend Developer — Java | Spring Boot | PostgreSQL  
[github.com/Diogo-Sardagna](https://github.com/Diogo-Sardagna)

# Stock Management System

A full-stack inventory management system for tracking products and raw materials, with relationship mapping between them.

---

## Architecture

```
.
├── stock-api/              # Backend — Quarkus REST API
│   └── src/main/java/org/acme
│       ├── entity/         # JPA entities
│       ├── dto/            # Data Transfer Objects
│       ├── repository/     # Panache repositories
│       ├── resource/       # JAX-RS REST endpoints
│       └── service/        # Business logic layer
│
└── stock-frontend/         # Frontend — React + Vite
    └── src/
        ├── components/     # Reusable UI components
        ├── assets/         # Static assets
        ├── App.jsx         # Root component & API config
        └── main.jsx        # Entry point
```

---

## Tech Stack

| Layer     | Technology              |
|-----------|-------------------------|
| Backend   | Java 17, Quarkus, JAX-RS, Panache ORM |
| Frontend  | React 18, Vite          |
| Database  | MySQL 8                 |
| Build     | Maven 3.6+, npm 9+      |
| Container | Docker (optional)       |

---

## Prerequisites

- **Java** 17+
- **Maven** 3.6+
- **Node.js** 18+ and **npm** 9+
- **MySQL** 8+
- **Docker** *(optional)*

---

## Database Setup

Create the schema before starting the application
Configure the datasource in `stock-api/src/main/resources/application.properties`:
```properties
quarkus.datasource.db-kind=mysql
quarkus.datasource.username=<seu_usuario>
quarkus.datasource.password=<sua_senha>
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/stock
quarkus.hibernate-orm.database.generation=update
```

---

## Running the Backend

```bash
cd stock-api
mvn compile
./mvnw quarkus:dev
```

The API will be available at `http://localhost:8080`.

### Available Endpoints

| Method | Route                        |
|--------|------------------------------|
| GET/POST/PUT/DELETE | `/products`                |
| GET/POST/PUT/DELETE | `/raw-materials`           |
| GET/POST/PUT/DELETE | `/product-materials` |

> Full API documentation is accessible via Swagger UI at `http://localhost:8080/q/swagger-ui` when running in dev mode.

### Running with Docker (JVM)

```bash
# Build the image
docker build -f src/main/docker/Dockerfile.jvm -t stock-api .

# Run the container
docker run -i --rm -p 8080:8080 stock-api
```

---

## Running the Frontend

```bash
cd stock-frontend
npm install
npm run dev
```

The application will be available at `http://localhost:5173`.

> Make sure the backend is running before starting the frontend, as it depends on the REST API to function correctly.

---

## API Base URL Configuration

The API base URL is configured in `stock-frontend/src/App.jsx` (or a dedicated config file). Ensure it points to the correct backend address:

```js
const API_BASE_URL = "http://localhost:8080";
```

---

## Notes

- Ensure MySQL is running and the `stock` database exists before starting the backend.
- In dev mode, Quarkus supports **hot reload** — changes to Java files are reflected automatically without restarting.
- Vite also supports **HMR (Hot Module Replacement)** for fast frontend iteration.

# 🎟️ TicketBay

A RESTful ticket management backend built with **Spring Boot 4**, **PostgreSQL**, and **OAuth2 (Google)** authentication. The application is fully containerized with Docker and includes automated database migrations via Flyway.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4 |
| Database | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Migrations | Flyway |
| Auth | Spring Security + OAuth2 (Google) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven |
| Containerization | Docker + Docker Compose |

---

## 📋 Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- A PostgreSQL instance (or use Docker)
- A Google OAuth2 client (Client ID & Secret)

---

## ⚙️ Configuration

Create a `.env` file in the project root with the following variables:

```env
DB_URL=jdbc:postgresql://<host>:<port>/<database>
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
```

> **Note:** Never commit your `.env` file. It is already listed in `.gitignore`.

---

## 🐳 Running with Docker

Build and start the application using Docker Compose:

```bash
docker compose up --build
```

The application will be available at `http://localhost:8000`.

---

## 🛠️ Running Locally (without Docker)

1. Clone the repository:
   ```bash
   git clone https://github.com/AniketBankar2004/TicketBay.git
   cd TicketBay
   ```

2. Set the required environment variables (or configure `application.properties`):
   ```bash
   export DB_URL=jdbc:postgresql://localhost:5432/ticketbay
   export GOOGLE_CLIENT_ID=your-google-client-id
   export GOOGLE_CLIENT_SECRET=your-google-client-secret
   ```

3. Build and run:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`.

---

## 📖 API Documentation

Once the application is running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

The OpenAPI JSON spec is available at:

```
http://localhost:8080/v3/api-docs
```

---

## 🔒 Authentication

TicketBay uses **Google OAuth2** for authentication via Spring Security. To log in, navigate to the application and authenticate through your Google account. Ensure your Google OAuth2 credentials are correctly configured in the `.env` file and that the authorized redirect URI in your Google Cloud Console is set to:

```
http://localhost:8080/login/oauth2/code/google
```

---

## 🗄️ Database Migrations

Database schema is managed automatically by **Flyway**. Migration scripts are located in:

```
src/main/resources/db/migration/
```

Migrations run automatically on application startup.

---

## 🧪 Running Tests

```bash
./mvnw test
```

---

## 📁 Project Structure

```
TicketBay/
├── .github/workflows/       # CI/CD workflows
├── src/
│   ├── main/
│   │   ├── java/            # Application source code
│   │   └── resources/       # Config files & Flyway migrations
│   └── test/                # Unit and integration tests
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── .env                     # (not committed) Environment variables
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 👤 Author

**Aniket Bankar** — [@AniketBankar2004](https://github.com/AniketBankar2004)

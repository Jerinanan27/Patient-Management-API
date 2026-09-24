# Patient Management API

A REST API for managing patient records, built with Spring Boot. It supports
full CRUD operations, input validation, HTTP Basic authentication on write
operations, and interactive API documentation through Swagger UI.

The project runs out of the box on an in-memory H2 database, so no database
setup is needed to try it. Configuration for MySQL and PostgreSQL is included
and commented in `application.properties`.

## Tech stack

- Java 21
- Spring Boot 3.3.4 (Web, Data JPA, Security, Validation, AOP)
- H2 (default), with MySQL and PostgreSQL drivers included
- springdoc OpenAPI (Swagger UI)
- JUnit 5 and Mockito for tests
- Maven

## Project structure

```
src/main/java/com/proma/patientmanagement
├── controller     REST endpoints
├── service        business logic (interface + implementation)
├── repository     Spring Data JPA repository
├── model          JPA entity and enum
├── dto            request/response objects and the entity mapper
├── exception      custom exceptions and the global handler
├── aspect         AOP logging aspect
└── config         security, OpenAPI, and sample data seeding
```

The layout follows a standard layered design. The controller talks to a
service interface, the service talks to a repository, and DTOs keep the JPA
entity out of the web layer. Dependencies are injected through constructors.

## Running it

You need Java 21. If Maven is not installed, IntelliJ IDEA includes a bundled
copy and can run the project directly.

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

## Running the tests

```bash
mvn test
```

The suite covers the service layer with mocked dependencies and the web layer
with `@WebMvcTest`, plus a context-load smoke test.

## API endpoints

Base path: `/api/v1/patients`

| Method | Path                    | Description        | Auth required |
|--------|-------------------------|--------------------|---------------|
| GET    | `/api/v1/patients`      | List all patients  | No            |
| GET    | `/api/v1/patients/{id}` | Get one patient    | No            |
| POST   | `/api/v1/patients`      | Create a patient   | Yes           |
| PUT    | `/api/v1/patients/{id}` | Update a patient   | Yes           |
| DELETE | `/api/v1/patients/{id}` | Delete a patient   | Yes           |

Write operations require HTTP Basic authentication. The seeded demo user is:

```
username: admin
password: admin123
```

## Example requests

List all patients:

```bash
curl http://localhost:8080/api/v1/patients
```

Create a patient (authenticated):

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/v1/patients \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "Nadia",
        "lastName": "Islam",
        "email": "nadia.islam@example.com",
        "phone": "+8801700000003",
        "dateOfBirth": "1997-06-15",
        "gender": "FEMALE",
        "bloodGroup": "A+"
      }'
```

## API documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`
OpenAPI spec: `http://localhost:8080/v3/api-docs`

## Database console

H2 console: `http://localhost:8080/h2-console`
Use JDBC URL `jdbc:h2:mem:patientdb`, username `sa`, and an empty password.

## Switching to MySQL or PostgreSQL

Open `src/main/resources/application.properties`, comment out the H2 block,
and uncomment the MySQL or PostgreSQL block. The drivers are already on the
classpath, so no other change is needed.

## Notes on design choices

- Validation runs on the request DTO with Jakarta Bean Validation, so bad
  input is rejected before it reaches the service.
- The global exception handler returns a consistent JSON error body for
  not-found, duplicate-email, validation, and unexpected errors.
- Passwords are hashed with BCrypt. The in-memory user and HTTP Basic setup
  are meant for a demo. A production version would use a database-backed user
  store and stateless JWT authentication.
- The AOP aspect logs and times every service call without touching the
  service code.

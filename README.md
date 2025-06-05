# DeviceHub
Device REST APIs for persisting and managing device resources.

## Features

- Create a new device
- Fully update (PUT) existing device
- Fetch all devices or filter by name, brand, or state
- Fetch a single device by ID
- Delete a device (with constraints)
- Validations with custom error handling
- PostgreSQL for persistence
 ---

## Tech Stack

- Java 21+
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
---

## Configuration

### PostgreSQL

Make sure PostgreSQL is running, and a DB is created (`device`).

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/devicehub
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
---

## Running the Application
Local (Maven)
```
./mvnw clean install
./mvnw spring-boot:run
```
---

## API Usage

### Overview

Base URL: http://localhost:8080/devices

Create: POST /devices

Fetch: GET /devices

Fetch by ID: GET /devices/{id}

Filter: GET /devices?brand=brand_name ##fetches all devices with brand_name

Update/Upsert: PUT /devices/{id}

Delete: DELETE /devices/{id}
---
### Access Swagger UI:
The API is documented using [springdoc-openapi](https://springdoc.org/).

Access Swagger UI
``http://localhost:8080/swagger-ui.html``

OpenAPI JSON:
``http://localhost:8080/v3/api-docs``
---

#### Constraints
- Devices in IN_USE state cannot be deleted or have name/brand modified.
- Only valid DeviceState values allowed: AVAILABLE, IN_USE, INACTIVE.
  Swagger/OpenAPI Documentation
- The API is documented using springdoc-openapi.
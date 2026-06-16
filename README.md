# NASA Asteroid API

RESTful application built with Spring Boot that integrates with the NASA NeoWs (Near Earth Object Web Service) API.

The application retrieves asteroid data from NASA, aggregates all asteroids within a specified date range, sorts them by the closest miss distance to Earth, and returns the top 10 closest asteroids.

## Prerequisites

* Java 21+
* Gradle 9+
* NASA API Key

You can obtain a free API key from:

https://api.nasa.gov

---

## Configuration

Set the following environment variable before running the application:

```bash
NASA_API_KEY=your_api_key
```

Or configure it in `application.yml`:

```yaml
nasa:
  api-key: your_api_key
```

---

## Running the Application

Start the application:

```bash
./gradlew bootRun
```

The application will be available at:

```text
http://localhost:8090
```

---

## Running Tests

Execute all unit tests and generate the JaCoCo coverage report:

```bash
./gradlew test jacocoTestReport
```

Coverage report location:

```text
build/reports/jacoco/test/html/index.html
```

Open the report in your browser to view code coverage details.

---

## API Endpoint

### Get Top 10 Closest Asteroids

```http
GET /api/v1/asteroids
```

### Query Parameters

| Parameter | Type       | Required | Description |
| --------- | ---------- | -------- | ----------- |
| startDate | YYYY-MM-DD | Yes      | Start date  |
| endDate   | YYYY-MM-DD | Yes      | End date    |

### Validation Rules

* Date format must be `YYYY-MM-DD`
* Date range must not exceed 7 days
* End date must not be before start date

---

## Example Request

```bash
curl --location --request GET \
'http://localhost:8090/api/v1/asteroids?startDate=2026-06-01&endDate=2026-06-02'
```

---

## Example Response

```json
{
  "startDate": "2026-06-01",
  "endDate": "2026-06-02",
  "count": 10,
  "asteroids": [
    {
      "id": "3410171",
      "name": "(2008 JV2)",
      "closestApproachDate": "2026-06-02",
      "missDistanceKm": 74684559.574496825,
      "estimatedDiameterMeters": 30.5179232594,
      "potentiallyHazardous": false
    }
  ]
}
```

---

## Returned Fields

| Field                   | Description                                                             |
| ----------------------- | ----------------------------------------------------------------------- |
| id                      | NASA asteroid identifier                                                |
| name                    | Asteroid name                                                           |
| closestApproachDate     | Closest approach date to Earth                                          |
| missDistanceKm          | Miss distance from Earth in kilometers                                  |
| estimatedDiameterMeters | Estimated minimum diameter in meters                                    |
| potentiallyHazardous    | Indicates whether NASA classifies the asteroid as potentially hazardous |

---

## Technical Overview

### Architecture

```text
Controller
    ↓
Service
    ↓
NASA Client (WebClient)
    ↓
NASA NeoWs API
```

### Main Components

* Controller layer for REST endpoints
* Service layer for orchestration
* NASA Client for external API communication
* MapStruct for DTO mapping
* Validation layer for request validation
* Global exception handling
* Unit tests using JUnit, Mockito, mockwebserver and okhttp3

---

## Test Coverage

The project includes tests for:

* Controller layer
* Service layer
* Processor layer
* Validation layer
* Mapper layer

Generated coverage reports are available through JaCoCo.

## Assumptions

- The closest asteroid is determined by the minimum miss distance (kilometers) returned by NASA.
- Only the first close-approach record is used when mapping asteroid data.
- Results are sorted in ascending order by miss distance.
- A maximum of 10 asteroids are returned.
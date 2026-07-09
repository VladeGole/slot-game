# Slot App – 5x3 Slot Engine (Backend)

A simplified backend implementation of the core logic (Slot Engine) for a slot game, written in Java / Spring Boot.
It allows a player to deposit money, check their account balance, and place a spin on a 5x3 slot game with a fixed wager and 5 paylines.

## Tech Stack

- Java 17+ (uses the `jakarta.*` namespace for validation – Spring Boot 3.x)
- Spring Boot (Web, Validation)
- Lombok
- MapStruct
- springdoc-openapi (Swagger UI)
- JUnit 5

## Prerequisites

This project was developed and tested with:

- **Java 17.0.12 LTS** (Oracle JDK) – download from [Oracle JDK 17 downloads](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), or use any other JDK 17 distribution (e.g. Eclipse Temurin, Amazon Corretto)
- **Apache Maven 3.9.12** – download from the [Maven download page](https://maven.apache.org/download.cgi)

After installing, make sure to set `JAVA_HOME` to your JDK 17 installation path and add `maven/bin` to your `PATH`.

## Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

By default, the application runs on:

```
http://localhost:8080
```

## Running the Tests

```bash
mvn test
```

The unit tests cover the mandatory test scenarios from the task spec (pure win, Wild substitution, an all-Wild line), as well as a losing-line case.

## API Endpoints

Base path: `/api/v1`

| Method | Path                | Description                                  |
|--------|---------------------|----------------------------------------------|
| `GET`  | `/balances`         | Returns the player's current account balance |
| `POST` | `/balances/deposit` | Deposits money into the player's account     |
| `POST` | `/spins`            | Executes a spin with a fixed wager of 5      |

### Example – Deposit

Request:
```json
POST "/api/v1/balances/deposit"
{
  "amount": 100.00
}
```

### Example – Spin

Request:
```json
POST "/api/v1/spins"
{
  "betAmount": 5
}
```

Response (example of a winning spin):
```json
{
  "matrix": [
    ["A", "W", "A", "A", "A"],
    ["Q", "K", "Q", "X", "K"],
    ["K", "Q", "W", "W", "Q"]
  ],
  "winningLines": [
    {
      "lineId": 1,
      "winningSymbol": "A",
      "payout": 10
    }
  ],
  "totalWin": 10,
  "newBalance": 105.00
}
```

## Game Rules – Summary

- Symbol strip (identical for all 5 reels, 10 positions): `[Q, A, K, W, W, A, Q, K, K, W]`
- The Wild (`W`) substitutes for any other symbol (Q, K, A) to form a winning combination.
- 5 fixed paylines (top row, middle row, bottom row, V-shape, inverted V-shape).
- The wager per spin is a fixed 5 (1 per line). Payout per line: `1 × symbol multiplier`.
- Multipliers:

| Symbol | 5-of-a-kind | 4-of-a-kind | 3-of-a-kind |
|--------|-------------|-------------|-------------|
| A      | 10x         | 5x          | 2x          |
| K      | 5x          | 3x          | 1x          |
| Q      | 2x          | 1x          | 0.5x        |

## Architecture

The logic is split by clear responsibilities:

- **`ReelSpinService`** – generates the random 5x3 matrix (RNG + strip wrap-around logic).
- **`PayoutCalculatorService`** – pure, stateless logic for evaluating a single payline (no Spring/DB dependencies, easy to unit test).
- **`SpinService`** – orchestrates a spin: charges the wager, calls `ReelSpinService` and `PayoutCalculatorService` for each line, credits any winnings, and assembles the response.
- **`BalanceService`** – manages the player's account balance (deposit, checking/charging the wager, crediting winnings). Methods that mutate the balance are `synchronized` to protect against concurrent access.
- **`GlobalHandlerException`** – centralized error handling and logging (validation errors, insufficient funds, unexpected errors).

## Swagger / OpenAPI Documentation


### 1. Open Swagger UI in your browser

```
http://localhost:8080/swagger-ui/index.html
```

## Notes / Known Limitations

- There is currently only a single (in-memory, singleton) user – no authentication or database persistence.
- The RNG uses `SecureRandom` for security and unpredictability of spin outcomes.
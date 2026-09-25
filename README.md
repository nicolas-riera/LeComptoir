# LeComptoir

## Overview
**Le Comptoir** is a full-stack checkout engine project developed as part of an intensive software architecture challenge. The objective of this project is to simulate real-world software evolution: rapid initial feature development (POO in Java 21) followed by code auditing, refactoring using design patterns, and cross-language modeling (strict TypeScript).

The project highlights the cost of technical debt and demonstrates how a clean, scalable architecture can absorb new client requirements without regressions.

## Key Features & Client Requirements
The checkout engine handles product catalogs, shopping carts, and receipts through 6 iterative releases:

- **`v1` - Basic Checkout Engine:** Itemized cart lines, total calculation, and receipt generation.
- **`v2` - Order Discounts:** Automatic 10% discount applied to any order exceeding €50.
- **`v3` - Category Promotions:** "Buy 2, Get 1 Free" on beverages (discounting the cheapest item).
- **`v4` - Differentiated VAT Rates:** Tax breakdown (5.5% for food, 20% standard) detailing pre-tax, VAT, and total amounts.
- **`v5` - Non-Cumulative Loyalty Rewards:** Loyalty points system (€1 = 1 point, 100 points = €5 discount) enforcing a strict best-deal calculation logic (discounts cannot stack).
- **`v6` - Architecture Stress Test:** Implementation of a surprise 6th client feature within 2 hours, validating the post-audit refactored design.

## Build and run

### Requirements

- Java 25 (JDK) + Maven
- Node.js (to run the TypeScript client via `npx`)

### Run the server (Java)

```
./run-server
```

or on Windows, you can just execute ``run-server.cmd``.

Compiles and starts the Java server (`com.andrenicolas.App`) on `http://127.0.0.1:8080`.

### Run the client (TypeScript)

```
./run-client
```

or on Windows, you can just execute ``run-client.cmd``.

Runs the TypeScript demo client (`demo/demo.ts`), which consumes the server's API.

> The server must be running before you start the client.


## Authors

This project has been realised by [Nicolas](https://github.com/nicolas-riera/) and [André](https://github.com/andrebtw).

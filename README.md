# Coupon Management System

A robust RESTful API built with **Spring Boot** and **Java 21** that implements a complex coupon eligibility and discount engine for e-commerce operations.

## Project Overview
This service acts as the "decision engine" for an e-commerce platform. It allows admins to configure sophisticated coupon rules (based on user tiers, cart value, and history) and intelligently selects the **single best coupon** for a customer by analyzing multiple constraints and tie-breaking rules.

**Core Capabilities:**
* **Dynamic Eligibility Engine:** Validates coupons against a multi-layered rule set including User Tier, Lifetime Spend, and Category Exclusions.
* **Smart Tie-Breaker Logic:** Implements a strict hierarchy (Discount Amount > Expiry Date > Code) to deterministically select the best offer.
* **Optimized Performance:** Uses in-memory data structures for effectively O(1) retrieval speeds during the assignment scope.

## Tech Stack
* **Language:** Java 21 (Leveraging latest features)
* **Framework:** Spring Boot 3.x
* **Build Tool:** Maven
* **Containerization:** Docker
* **Testing:** Postman

##  How to Run Locally

### Prerequisites
* JDK 21 installed.
* Maven installed.

### Setup Steps
1.  Clone the repository:
    ```bash
    git clone [https://github.com/YOUR_USERNAME/coupon-manager.git](https://github.com/YOUR_USERNAME/coupon-manager.git)
    ```
2.  Navigate to the project directory:
    ```bash
    cd coupon-manager
    ```
3.  Build the project:
    ```bash
    mvn clean install
    ```
4.  Run the application:
    ```bash
    mvn spring-boot:run
    ```
The server will start at `http://localhost:8080`.

## API Endpoints

### 1. Create Coupon
* **URL:** `POST /coupons`
* **Description:** Configures a new coupon with specific eligibility rules.
* **Body:**
    ```json
    {
      "code": "SAVE10",
      "discountType": "PERCENT",
      "discountValue": 10,
      "eligibility": { "minCartValue": 100 }
    }
    ```

### 2. Get Best Coupon
* **URL:** `POST /applicable-coupons`
* **Description:** Analyzes the Cart and User Profile to return the highest-value valid coupon.
* **Body:**
    ```json
    {
      "user": { "userTier": "GOLD", "ordersPlaced": 0 },
      "cart": { "items": [ { "productId": "p1", "unitPrice": 500, "quantity": 1 } ] }
    }
    ```

## AI Usage Disclosure
In the interest of transparency, I leveraged Generative AI (LLMs) as a "Pair Programmer" and productivity accelerator during this assignment. My approach focused on using AI to handle boilerplate implementation so I could focus on the core business logic and architecture.

**How I Used It:**
* **Boilerplate Acceleration:** Generated standard DTOs (Data Transfer Objects) and getter/setter patterns to save manual typing time.
* **Syntax Reference:** Used as a quick reference for Java 21 Stream API nuances, specifically for chaining multiple `Comparator` logic in the tie-breaker algorithm.
* **Edge Case Brainstorming:** Prompted the AI to suggest potential failure scenarios (e.g., "What happens to the sort order if a coupon has a null end date?") to ensure my logic was robust.

**Strategic Prompts Used:**
* *"Generate a robust Java POJO for a Coupon model that includes nested eligibility fields using Lombok."* (Focus on structure).
* *"Refactor this 'if-else' chain into a cleaner Java Stream filter to improve readability."* (Focus on code quality).
* *"Create a JSON payload to test a scenario where a coupon is excluded due to a specific category constraint."* (Focus on testing coverage).

**Note:** All core logic, specifically the `getBestCoupon` algorithm and eligibility validation, was manually reviewed, refined, and tested to ensure it strictly meets the assignment requirements.
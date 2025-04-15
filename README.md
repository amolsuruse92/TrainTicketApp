
# Train Ticketing API

A Spring Boot application that simulates purchasing train tickets from **London to France**. Users can buy tickets, view receipts, see who’s seated in each section, update seat assignments, or remove users.

---

## Features

- Purchase train tickets
- Generate user-specific receipts
- View users by train section (A or B)
- Modify a user's seat
- Remove a user from the train

---

## 🚀 Running the Application

### Prerequisites
- Java 17+
- Maven

---

## API Endpoints

### 1. Purchase Ticket

**POST** `/api/tickets/purchase`

Creates a new ticket and assigns a seat.

#### Request Body:
```json
{
  "firstName": "Alice",
  "lastName": "Smith",
  "email": "alice@example.com",
}
```

### 2. Get Receipt by Email

**GET** `/api/tickets/receipt/{email}`

Returns the receipt for a given user's email.

#### Sample:
**GET** `/api/tickets/receipt/alice@example.com`
---

### 3. Get Users by Section

**GET** `/api/tickets/users/{section}`

Returns all users in a specific section (A or B).

#### Sample:
**GET** `/api/tickets/users/A`

---

### 4. Remove a User

**DELETE** `/api/tickets/remove/{email}`

Removes a user and their ticket from the system.

#### Sample:
**DELETE** `/api/tickets/remove/alice@example.com`

---

### 5. Modify Seat Section

**PUT** `/api/tickets/modify-seat`

Changes the section for a given user.

#### Request Body:
```json
{
   "email": "alice@example.com",
   "section": "B",
   "seatNumber": 3


}
```

## TODO
- unit/integration test cases.
- Add persistent DB (PostgreSQL/MySQL)
- Add authentication (Spring Security + JWT)
- Dockerize the app
- Add Swagger/OpenAPI docs


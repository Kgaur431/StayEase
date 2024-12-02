# StayEase - Hotel Aggregator System

StayEase is a comprehensive hotel booking system that allows users to browse hotels, manage bookings, and for hotel managers to manage their properties. The system supports role-based access for users and hotel managers and implements JWT authentication to ensure secure communication.

---

## Features

- **User Registration & Authentication**: Secure registration and login using JWT tokens.
- **Hotel Browsing**: Users can browse hotels based on location and available rooms.
- **Room Booking**: Users can book available rooms in hotels.
- **Role-Based Access Control**: Admins can manage hotels, hotel managers can manage bookings, and customers can make bookings.
- **Hotel Management**: Admins and hotel managers can manage hotel properties.
- **Booking Management**: Users can view, update, and cancel bookings. Admins and hotel managers can manage all bookings.
- **Secure API**: All private endpoints are protected with JWT authentication.

---

## Technology Stack

- **Backend**: Java, Spring Boot
- **Database**: MySQL
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Spring Security
- **Build Tool**: Maven/Gradle
- **IDE**: IntelliJ IDEA / Eclipse

---

## Project Setup

Follow these steps to set up StayEase locally:

### 1. Clone the repository
```bash
git clone https://github.com/Kgaur431/StayEase.git
```


### CREATE DATABASE StayEaseDB;
### USE StayEaseDB;

### 3. Database Tables
```sql
CREATE TABLE Users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER', 'HOTEL_MANAGER', 'ADMIN') DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Hotel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    description TEXT,
    availableRooms INT NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    hotel_id BIGINT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (hotel_id) REFERENCES hotel(id)
);
```

### 4. Configure Application Properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/StayEaseDB
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
``` 

### 5. Build the Application
```bash
gradle build
``` 

### 6. Run the Application
```bash
gradle bootRun
```

### API Endpoints
``` 
Public Endpoints
POST /register: Register a new user.
POST /login: Login and receive a JWT token.
GET /hotels: Browse hotels (public access).
Private Endpoints
POST /bookings: Create a new booking (requires authentication).
GET /bookings/{id}: Get details of a specific booking (requires authentication).
PUT /bookings/{id}: Update booking details (requires authentication).
DELETE /bookings/{id}: Cancel a booking (requires authentication).
POST /hotels: Admin - Add a new hotel.
PUT /hotels/{id}: Admin/Hotel Manager - Update hotel details.
DELETE /hotels/{id}: Admin/Hotel Manager - Delete a hotel.

```


### Role-Based Access Control (RBAC)
``` 
ADMIN: Can manage users, bookings, and hotels.
HOTEL_MANAGER: Can manage hotels, view bookings for their hotel.
CUSTOMER: Can book hotels, view their bookings.
```

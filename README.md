# Helper App

APIs for Help application that allows Residents of an apartment to hire and schedule Home services

## Features

- **User Registration API**:
    - Three types of users: Residents, Helpers, and Admins.
    - Admins approve Resident and Helper registrations.
- **Login & Logout API**:
    - Allows Residents and Helpers to log in with valid credentials.
    - Handles session timeout (3 hours post-login).
- **Helper Management API**:
    - Admins can add, modify, or delete Helpers.
- **Appointment Booking API**:
    - Residents can view and book available Helpers/Technicians.
    - Helpers can view their scheduled appointments

## Tech Stack

- **Backend**: Java (1.8+), Spring Boot
- **Database**: MySQL 
- **Build Tool**: Maven/Gradle
- **Testing**: JUnit, Mockito
- **API Testing**: Postman

---

## Prerequisites

1. **Java**: Ensure you have JDK 19 installed.
2. **SQL**: Install MySQL
3. **Maven**: Ensure Maven is installed and configured.

---

## Documentation

### Postman API Collection

import from `HelperApp.postman_collection.json`

---

## Setup Instructions

### Clone the Repository
```bash
git clone https://github.com/KavyapriyaJG/cdw-helper-app.git
```

### Configure MySQL
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/helper_app
spring.datasource.username=<username>
spring.datasource.password=<password>
```

### Build the project
```bash
mvn clean install
```

### Run the project
```bash
mvn spring-boot:run
```

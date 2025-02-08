# Car Lease Management System

## Overview
The Car Lease Management System is a Spring Boot application designed to manage car leases. It provides functionalities to register cars, manage leases, and handle customer information.

## Features
- Register new cars
- Update car status
- Register customers
- Start and end leases
- Retrieve lease history for cars and customers
- Retrieve all cars and customers

## Technologies Used
- Java
- Spring Boot
- JPA/Hibernate
- Maven
- JUnit 5
- Mockito

## Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher
- A database (e.g., MySQL, PostgreSQL)

## Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/car-lease-management.git
    cd car-lease-management
    ```

2. Configure the database in `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/car_lease_db
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
    ```

3. Build the project:
    ```sh
    mvn clean install
    ```

4. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Running Tests
To run the tests, use the following command:
```sh
mvn test


# Event Ticketing System

## Overview

The Event Ticketing System is a web application that allows users to manage events, tickets, and transactions. The backend is built using Java, Spring Boot, and Maven, while the frontend is developed using Angular.

## Prerequisites

- **Java**: JDK 11 or higher
- **Maven**: Apache Maven
- **MySQL**: MySQL database server

## Backend Setup

### Step-by-Step Guide

1. **Install Java**
   - Download and install JDK from the [official Oracle website](https://www.oracle.com/java/technologies/javase-downloads.html).
   - Verify the installation:
     ```sh
     java -version
     ```

2. **Install Maven**
   - Download and install Maven from the [official Maven website](https://maven.apache.org/download.cgi).
   - Verify the installation:
     ```sh
     mvn -version
     ```

3. **Install MySQL**
   - Download and install MySQL from the [official MySQL website](https://dev.mysql.com/downloads/installer/).
   - Start the MySQL server and create a database:
     ```sql
     CREATE DATABASE event_ticketing_system;
     ```

### Cloning the Repository and Setting Up the Database

1. **Clone the Repository**
   ```sh
   git clone https://github.com/Walapalam/EventTicketingSystemBackend.git
   cd EventTicketingSystemBackend
2. **Configure the Database**
   ```sh
   spring.datasource.url=jdbc:mysql://localhost:3306/event_ticketing_system
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
3. **Build the Project**
   ```sh
   mvn clean install
4. **Run the Application**
   ```sh
   mvn spring-boot:run

### Accessing the Application
- Open your web-browser and navigate to:
  ```sh
  http://localhost:8080

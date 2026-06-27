# Tasks Management API

A Spring Boot REST API for managing task lists and tasks. This application provides endpoints to create, read, update, and delete task lists and tasks within those lists.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Data Model](#data-model)
- [Configuration](#configuration)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)

## Overview

This is a RESTful API built with Spring Boot for managing task lists and individual tasks. Each task list can contain multiple tasks, and tasks can have properties like title, description, due date, status, and priority.

## Features

- Create, read, update, and delete task lists
- Create, read, update, and delete tasks within task lists
- Task filtering by task list
- Task properties: title, description, due date, status (OPEN, IN_PROGRESS, DONE), priority (LOW, MEDIUM, HIGH)
- Automatic timestamp tracking (created/updated)
- UUID-based identifiers
- RESTful API design
- PostgreSQL database integration
- Spring Data JPA for data access
- H2 console for database inspection (during development)

## Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 4.1.0
- **Data Access**: Spring Data JPA
- **Database**: PostgreSQL (development) / H2 (testing)
- **Build Tool**: Maven
- **API Documentation**: Self-descriptive REST endpoints

## Project Structure

```
src/main/java/com/example/Tasks/
├── TasksApplication.java             # Main application class
├/controllers                         # REST controllers
│   ├── TaskListController.java       # Task list endpoints
│   └── TasksController.java          # Task endpoints
├/domain                              # Domain models
│   ├── dto                           # Data Transfer Objects
│   │   ├── ErrorResponse.java
│   │   ├── TaskDto.java
│   │   └── TaskListDto.java
│   ├── entities                      # JPA entities
│   │   ├── Task.java
│   │   ├── TaskList.java
│   │   ├── TaskPriority.java (enum)
│   │   └── TaskStatus.java (enum)
│   └── mappers                       # Mapper interfaces and implementations
│       ├── TaskListMapper.java
│       ├── TaskListMapperImpl.java
│       ├── TaskMapper.java
│       └── TaskMapperImpl.java
├/repositories                        # Spring Data JPA repositories
│   ├── TaskListRepository.java
│   └── TaskRepository.java
├/services                            # Business logic
│   ├── TaskListService.java
│   ├── TaskService.java
│   ├── impl
│   │   ├── TaskListServiceImpl.java
│   │   └── TaskServiceImpl.java
└/util                                # Utility classes (if any)
```

## API Endpoints

### Task Lists

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/task-lists` | Get all task lists |
| POST | `/task-lists` | Create a new task list |
| GET | `/task-lists/{task_list_id}` | Get a specific task list by ID |
| PUT | `/task-lists/{task_list_id}` | Update a specific task list |
| DELETE | `/task-lists/{task_list_id}` | Delete a specific task list |

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/task-lists/{task_list_id}/tasks` | Get all tasks for a specific task list |
| POST | `/task-lists/{task_list_id}/tasks` | Create a new task in a specific task list |

## Data Model

### TaskList

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier (auto-generated) |
| title | String (required) | Title of the task list |
| description | String | Description of the task list |
| tasks | List<Task> | List of tasks in this list |
| created | LocalDateTime | Creation timestamp |
| updated | LocalDateTime | Last update timestamp |

### Task

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier (auto-generated) |
| title | String (required) | Title of the task |
| description | String | Description of the task |
| dueDate | LocalDateTime | Due date for the task |
| status | TaskStatus | Status of the task (OPEN, IN_PROGRESS, DONE) |
| priority | TaskPriority | Priority of the task (LOW, MEDIUM, HIGH) |
| taskList | TaskList | Reference to the parent task list |
| created | LocalDateTime | Creation timestamp |
| updated | LocalDateTime | Last update timestamp |

### Enums

#### TaskStatus
- `OPEN` - Task is open and not yet started
- `IN_PROGRESS` - Task is currently being worked on
- `DONE` - Task is completed

#### TaskPriority
- `LOW` - Low priority task
- `MEDIUM` - Medium priority task
- `HIGH` - High priority task

## Configuration

The application uses Spring Boot's `application.properties` for configuration:

```properties
spring.application.name=Tasks
server.port=8080
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=changemeinprod!
spring.jpa.hibernate.ddl-auto=update
```

### Database Setup
- Ensure PostgreSQL is running on localhost:5432
- Create a database named `postgres` (or modify the URL in application.properties)
- The username is `postgres` and password should be set as configured
- On startup, the application will create tables automatically via Hibernate (`spring.jpa.hibernate.ddl-auto=update`)

## Setup and Installation

### Prerequisites
- Java 21 JDK or higher
- Maven 3.6+
- PostgreSQL database

### Steps
1. Clone the repository
2. Ensure PostgreSQL is running and create the database
3. Update `src/main/resources/application.properties` with your database credentials if needed
4. Build the project: `mvn clean install`
5. Run the application: `mvn spring-boot:run`

## Running the Application

The application will start on port 8080 by default.

### Access the H2 Console (for development)
While the application is running, you can access the H2 console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- User: sa
- Password: (leave blank)

### API Testing
You can test the API using tools like curl, Postman, or any HTTP client.


## API Design Notes

- All IDs are UUIDs generated by the database
- Timestamps are automatically managed by the application
- Validation is performed on required fields (title for both task lists and tasks)
- Default values are applied when not specified:
  - Task priority defaults to MEDIUM
  - Task status defaults to OPEN
- The API follows RESTful conventions with appropriate HTTP methods and status codes
- Error handling is implemented via a global exception handler

## Implementation Details

### Layers
1. **Controller Layer**: Handles HTTP requests and responses
2. **Service Layer**: Contains business logic
3. **Repository Layer**: Handles data access using Spring Data JPA
4. **Domain Layer**: Contains JPA entities and DTOs
5. **Mapper Layer**: Converts between entities and DTOs

### Key Features
- **Separation of Concerns**: Clear separation between layers
- **DTO Pattern**: Uses Data Transfer Objects to decouple internal entities from API contracts
- **Mapping Layer**: Uses MapStruct-style manual mappers for clean conversion between entities and DTOs
- **Validation**: Basic validation for required fields
- **Automatic Timestamps**: Created and updated timestamps are managed automatically
- **Cascading Operations**: Task removal when a task list is deleted

## Error Handling

The application includes a global exception handler (`GlobalExceptionHandler.java`) that catches:
- `IllegalArgumentException` for validation errors (returns 400 Bad Request)
- Other exceptions (returns 500 Internal Server Error)

Error responses follow this format:
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "message": "Error description",
  "details": "Additional details"
}
```

## License

This project is open source and available under the MIT License.
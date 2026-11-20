
# Library Management REST API

A Spring Boot RESTful API for managing books, students,
and library borrowing operations.

## Technologies Used

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Postman
- Git and GitHub

## Features

### Book Management
- Add, retrieve, update, and delete books
- Search books by category
- Validate ISBN uniqueness
- Track total and available copies
- Protect books with borrowing history

### Student Management
- Register and manage students
- Validate student email addresses
- Prevent duplicate email registration
- Protect students with borrowing history

### Book Issue Management
- Issue books to registered students
- Automatically update available copies
- Return borrowed books
- View borrowing history by student
- Identify overdue books
- Limit students to two active issues
- Set due dates to 14 days after issue

### Validation and Error Handling
- Required field validation
- Duplicate ISBN and email prevention
- Global exception handling
- Meaningful HTTP status codes
- Transactional borrowing operations

## Project Architecture

Controller → Service → Repository → MySQL Database

## API Endpoints

### Book APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/books | Add book |
| GET | /api/books | Get all books |
| GET | /api/books/{id} | Get book by ID |
| PUT | /api/books/{id} | Update book |
| DELETE | /api/books/{id} | Delete book |
| GET | /api/books/category/{category} | Search by category |

### Student APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/students | Register student |
| GET | /api/students | Get all students |
| GET | /api/students/{id} | Get student by ID |
| PUT | /api/students/{id} | Update student |
| DELETE | /api/students/{id} | Delete student |

### Book Issue APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/issues/{studentId}/{bookId} | Issue book |
| GET | /api/issues | Get all issues |
| GET | /api/issues/student/{studentId} | Student history |
| PUT | /api/issues/return/{issueId} | Return book |
| GET | /api/issues/overdue | Get overdue issues |

## Database Setup

Create a MySQL database:

```sql
CREATE DATABASE library_management;
```

Configure the following environment variable:

```text
DB_PASSWORD=your_mysql_password
```

Optional environment variables:

```text
DB_USERNAME=root
DB_URL=jdbc:mysql://localhost:3306/library_management
```

Spring Data JPA and Hibernate manage database tables
according to the application's entity mappings.

## Running the Application

Requirements:
- Java 21
- MySQL Server
- Maven or the included Maven Wrapper

### Windows PowerShell

Set the database password:

```powershell
$env:DB_PASSWORD = "your_mysql_password"
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs at:

http://localhost:8080

## Postman Testing

Import these files from the postman directory:

1. LibraryManagementAPI.postman_collection.json
2. LibraryManagementLocal.postman_environment.json

Select the imported environment.

The baseUrl variable should be:

```text
http://localhost:8080
```

Execute the requests to test the API endpoints.

## HTTP Status Codes

| Status | Meaning |
|--------|---------|
| 200 | Successful request |
| 201 | Resource created |
| 204 | Resource deleted |
| 400 | Invalid request |
| 404 | Resource not found |
| 409 | Conflict or duplicate resource |

## Git Branches

- main: Stable project branch
- development: Integration branch
- feature/book-management: Book Management feature branch

## Additional Documentation

See docs/BOOK_API.md for Book Management documentation.

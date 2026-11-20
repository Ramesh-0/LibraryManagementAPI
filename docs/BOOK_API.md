
# Book Management API

## Overview

The Book Management module provides REST endpoints
for managing books in the Library Management System.

Base URL: http://localhost:8080/api/books

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/books | Add a new book |
| GET | /api/books | Retrieve all books |
| GET | /api/books/{id} | Retrieve a book by ID |
| PUT | /api/books/{id} | Update book details |
| DELETE | /api/books/{id} | Delete a book |
| GET | /api/books/category/{category} | Search by category |

## Book Fields

- bookId
- title
- author
- isbn
- category
- totalCopies
- availableCopies

## Validation Rules

- ISBN must be unique.
- Required text fields cannot be blank.
- Copy counts cannot be negative.
- Available copies cannot exceed total copies.
- Books with borrowing history cannot be deleted.

## HTTP Status Codes

- 200 OK: Successful retrieval or update
- 201 Created: Book added successfully
- 204 No Content: Book deleted successfully
- 400 Bad Request: Invalid input
- 404 Not Found: Book does not exist
- 409 Conflict: Duplicate ISBN or borrowing history

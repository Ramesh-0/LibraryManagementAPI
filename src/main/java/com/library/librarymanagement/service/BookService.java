
package com.library.librarymanagement.service;

import com.library.librarymanagement.entity.Book;

import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.repository.BookIssueRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookIssueRepository bookIssueRepository;

    // Constructor Injection
    public BookService(
            BookRepository bookRepository,
            BookIssueRepository bookIssueRepository) {

        this.bookRepository = bookRepository;
        this.bookIssueRepository = bookIssueRepository;
    }

    // 1. Add a new book
    @Transactional
    public Book addBook(Book book) {

        if (book.getBookId() != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Book ID must not be provided"
            );
        }

        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Book with this ISBN already exists"
            );
        }

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Available copies cannot exceed total copies"
            );
        }

        return bookRepository.save(book);
    }

    // 2. Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 3. Get book by ID
    public Book getBookById(Long id) {

        return bookRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book not found with ID: " + id
                )
            );
    }

    // 4. Update book details
    @Transactional
    public Book updateBook(Long id, Book updatedBook) {

        Book existingBook = getBookById(id);

        if (!existingBook.getIsbn().equals(updatedBook.getIsbn())
                && bookRepository.findByIsbn(updatedBook.getIsbn())
                    .isPresent()) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Book with this ISBN already exists"
            );
        }

        int borrowedCopies = existingBook.getTotalCopies()
                - existingBook.getAvailableCopies();

        if (updatedBook.getTotalCopies() < borrowedCopies) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Total copies cannot be less than borrowed copies"
            );
        }

        if (updatedBook.getAvailableCopies() !=
                updatedBook.getTotalCopies() - borrowedCopies) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Available copies do not match borrowed copies"
            );
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setTotalCopies(updatedBook.getTotalCopies());
        existingBook.setAvailableCopies(updatedBook.getAvailableCopies());

        return bookRepository.save(existingBook);
    }

    // 5. Delete a book
    @Transactional
    public void deleteBook(Long id) {

        Book book = getBookById(id);

        // Check for any borrowing history
        // Includes both ISSUED and RETURNED records
        boolean hasIssueHistory =
            bookIssueRepository.existsByBookBookId(id);

        if (hasIssueHistory) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Cannot delete a book with borrowing history"
            );
        }

        bookRepository.delete(book);
    }

    // 6. Search books by category
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryIgnoreCase(category);
    }
}

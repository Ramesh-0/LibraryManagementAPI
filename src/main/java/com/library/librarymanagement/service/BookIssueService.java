
package com.library.librarymanagement.service;

import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.entity.Student;
import com.library.librarymanagement.entity.BookIssue;
import com.library.librarymanagement.entity.BookIssue.IssueStatus;

import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.repository.StudentRepository;
import com.library.librarymanagement.repository.BookIssueRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookIssueService {

    private final BookIssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    public BookIssueService(
            BookIssueRepository issueRepository,
            BookRepository bookRepository,
            StudentRepository studentRepository) {

        this.issueRepository = issueRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    // 1. Issue a book
    @Transactional
    public BookIssue issueBook(Long studentId, Long bookId) {

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Student not found"
            ));

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Book not found"
            ));

        // Check maximum active issues
        long activeIssues =
            issueRepository.countByStudentStudentIdAndStatus(
                studentId,
                IssueStatus.ISSUED
            );

        if (activeIssues >= 2) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A student cannot borrow more than 2 books"
            );
        }

        // Check book availability
        if (book.getAvailableCopies() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "No copies available for this book"
            );
        }

        // Reduce available copies
        book.setAvailableCopies(
            book.getAvailableCopies() - 1
        );

        bookRepository.save(book);

        // Create issue record
        BookIssue issue = new BookIssue();

        issue.setStudent(student);
        issue.setBook(book);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(LocalDate.now().plusDays(14));
        issue.setReturnDate(null);
        issue.setStatus(IssueStatus.ISSUED);

        return issueRepository.save(issue);
    }

    // 2. Get all book issues
    public List<BookIssue> getAllIssues() {
        return issueRepository.findAll();
    }

    // 3. Get issues by student
    public List<BookIssue> getIssuesByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Student not found"
            );
        }

        return issueRepository.findByStudentStudentId(studentId);
    }

    // 4. Return a book
    @Transactional
    public BookIssue returnBook(Long issueId) {

        BookIssue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Book issue record not found"
            ));

        if (issue.getStatus() == IssueStatus.RETURNED) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "This book has already been returned"
            );
        }

        issue.setReturnDate(LocalDate.now());
        issue.setStatus(IssueStatus.RETURNED);

        Book book = issue.getBook();

        book.setAvailableCopies(
            book.getAvailableCopies() + 1
        );

        bookRepository.save(book);

        return issueRepository.save(issue);
    }

    // 5. Get overdue issues
    public List<BookIssue> getOverdueIssues() {

        return issueRepository.findByStatusAndDueDateBefore(
            IssueStatus.ISSUED,
            LocalDate.now()
        );
    }
}

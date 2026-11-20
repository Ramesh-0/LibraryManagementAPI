
package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.BookIssue;
import com.library.librarymanagement.service.BookIssueService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class BookIssueController {

    private final BookIssueService bookIssueService;

    public BookIssueController(
            BookIssueService bookIssueService) {
        this.bookIssueService = bookIssueService;
    }

    // 1. Issue a book to a student
    @PostMapping("/{studentId}/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookIssue issueBook(
            @PathVariable Long studentId,
            @PathVariable Long bookId) {

        return bookIssueService.issueBook(
            studentId, bookId
        );
    }

    // 2. Get all book issues
    @GetMapping
    public List<BookIssue> getAllIssues() {
        return bookIssueService.getAllIssues();
    }

    // 3. Get issues for a specific student
    @GetMapping("/student/{studentId}")
    public List<BookIssue> getIssuesByStudent(
            @PathVariable Long studentId) {

        return bookIssueService.getIssuesByStudent(
            studentId
        );
    }

    // 4. Return an issued book
    @PutMapping("/return/{issueId}")
    public BookIssue returnBook(
            @PathVariable Long issueId) {

        return bookIssueService.returnBook(issueId);
    }

    // 5. Get overdue book issues
    @GetMapping("/overdue")
    public List<BookIssue> getOverdueIssues() {
        return bookIssueService.getOverdueIssues();
    }
}

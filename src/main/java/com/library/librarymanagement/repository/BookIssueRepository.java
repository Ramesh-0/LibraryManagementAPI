
package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.BookIssue;
import com.library.librarymanagement.entity.BookIssue.IssueStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookIssueRepository
        extends JpaRepository<BookIssue, Long> {

    // 1. Find all issues for a particular student
    List<BookIssue> findByStudentStudentId(Long studentId);

    // 2. Count active issues for a student
    long countByStudentStudentIdAndStatus(
            Long studentId,
            IssueStatus status
    );

    // 3. Find all overdue book issues
    List<BookIssue> findByStatusAndDueDateBefore(
            IssueStatus status,
            LocalDate date
    );

    // 4. Check active issues for a book
    boolean existsByBookBookIdAndStatus(
            Long bookId,
            IssueStatus status
    );

    // 5. Check active issues for a student
    boolean existsByStudentStudentIdAndStatus(
            Long studentId,
            IssueStatus status
    );

    // 6. Check any borrowing history for a book
    // Includes both ISSUED and RETURNED records
    boolean existsByBookBookId(Long bookId);

    // 7. Check any borrowing history for a student
    // Includes both ISSUED and RETURNED records
    boolean existsByStudentStudentId(Long studentId);
}


package com.library.librarymanagement.service;

import com.library.librarymanagement.entity.Student;

import com.library.librarymanagement.repository.StudentRepository;
import com.library.librarymanagement.repository.BookIssueRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BookIssueRepository bookIssueRepository;

    // Constructor Injection
    public StudentService(
            StudentRepository studentRepository,
            BookIssueRepository bookIssueRepository) {

        this.studentRepository = studentRepository;
        this.bookIssueRepository = bookIssueRepository;
    }

    // 1. Register a new student
    @Transactional
    public Student addStudent(Student student) {

        if (student.getStudentId() != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Student ID must not be provided"
            );
        }

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Student with this email already exists"
            );
        }

        return studentRepository.save(student);
    }

    // 2. Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 3. Get student by ID
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student not found with ID: " + id
                )
            );
    }

    // 4. Update student details
    @Transactional
    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = getStudentById(id);

        if (!existingStudent.getEmail()
                .equals(updatedStudent.getEmail())
                && studentRepository.existsByEmail(
                    updatedStudent.getEmail())) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email is already registered"
            );
        }

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setDepartment(updatedStudent.getDepartment());
        existingStudent.setPhone(updatedStudent.getPhone());

        return studentRepository.save(existingStudent);
    }

    // 5. Delete student
    @Transactional
    public void deleteStudent(Long id) {

        Student student = getStudentById(id);

        // Check for any borrowing history
        // Includes both ISSUED and RETURNED records
        boolean hasIssueHistory =
            bookIssueRepository.existsByStudentStudentId(id);

        if (hasIssueHistory) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Cannot delete a student with borrowing history"
            );
        }

        studentRepository.delete(student);
    }
}

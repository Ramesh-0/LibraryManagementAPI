
package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategoryIgnoreCase(String category);

    Optional<Book> findByIsbn(String isbn);
}

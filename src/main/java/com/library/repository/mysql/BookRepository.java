package com.library.repository.mysql;

import com.library.model.mysql.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books by title (search feature)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books by author
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Find books by category
    List<Book> findByCategory(String category);

    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);

    // Find available books
    List<Book> findByAvailableQuantityGreaterThan(int quantity);
}
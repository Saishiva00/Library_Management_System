package com.library.controller;

import com.library.model.mysql.Book;
import com.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    // GET all books
    // URL: GET http://localhost:8080/api/books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // GET book by ID
    // URL: GET http://localhost:8080/api/books/1
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Add new book
    // URL: POST http://localhost:8080/api/books
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // PUT - Update book
    // URL: PUT http://localhost:8080/api/books/1
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return ResponseEntity.ok(updatedBook);
    }

    // DELETE - Delete book
    // URL: DELETE http://localhost:8080/api/books/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully!");
    }

    // GET - Search by title
    // URL: GET http://localhost:8080/api/books/search/title?keyword=java
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchByTitle(
            @RequestParam String keyword) {
        List<Book> books = bookService.searchByTitle(keyword);
        return ResponseEntity.ok(books);
    }

    // GET - Search by author
    // URL: GET http://localhost:8080/api/books/search/author?keyword=james
    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchByAuthor(
            @RequestParam String keyword) {
        List<Book> books = bookService.searchByAuthor(keyword);
        return ResponseEntity.ok(books);
    }
}
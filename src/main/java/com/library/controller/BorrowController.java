package com.library.controller;

import com.library.model.mysql.BorrowRecord;
import com.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BorrowController {

    private final BorrowService borrowService;

    // POST - Issue book
    // URL: POST /api/borrow/issue?memberId=1&bookId=1
    @PostMapping("/issue")
    public ResponseEntity<?> issueBook(
            @RequestParam Long memberId,
            @RequestParam Long bookId) {
        try {
            BorrowRecord record = borrowService.issueBook(memberId, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT - Return book
    // URL: PUT /api/borrow/return/{borrowRecordId}
    @PutMapping("/return/{borrowRecordId}")
    public ResponseEntity<?> returnBook(
            @PathVariable Long borrowRecordId) {
        try {
            BorrowRecord record = borrowService.returnBook(borrowRecordId);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET - All borrow records
    @GetMapping
    public ResponseEntity<List<BorrowRecord>> getAllBorrowRecords() {
        return ResponseEntity.ok(borrowService.getAllBorrowRecords());
    }

    // GET - Active borrows
    @GetMapping("/active")
    public ResponseEntity<List<BorrowRecord>> getActiveBorrows() {
        return ResponseEntity.ok(borrowService.getActiveBorrows());
    }

    // GET - Overdue books
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowRecord>> getOverdueBooks() {
        return ResponseEntity.ok(borrowService.getOverdueBooks());
    }

    // GET - Member borrow history
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getMemberHistory(
            @PathVariable Long memberId) {
        try {
            return ResponseEntity.ok(
                    borrowService.getMemberBorrowHistory(memberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package com.library.model.mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which member borrowed
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Which book was borrowed
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // When was it borrowed
    @Column(nullable = false)
    private LocalDate borrowDate;

    // When is it due
    @Column(nullable = false)
    private LocalDate dueDate;

    // When was it returned
    @Column
    private LocalDate returnDate;

    // Status of borrow
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status = BorrowStatus.BORROWED;

    // Fine amount
    @Column
    private Double fineAmount = 0.0;

    // Fine paid or not
    @Column
    private boolean finePaid = false;

    public enum BorrowStatus {
        BORROWED,
        RETURNED,
        OVERDUE,
        LOST
    }
}
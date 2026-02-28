package com.library.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "borrow_history")
public class BorrowHistory {

    @Id
    private String id;

    // Reference to MySQL BorrowRecord
    private Long borrowRecordId;

    // Member details snapshot
    private Long memberId;
    private String memberName;
    private String membershipId;

    // Book details snapshot
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;

    // Action details
    private String action; // BORROWED, RETURNED, OVERDUE
    private LocalDateTime actionDate;

    // Fine details
    private Double fineAmount;
    private boolean finePaid;

    // Extra notes
    private String notes;
}
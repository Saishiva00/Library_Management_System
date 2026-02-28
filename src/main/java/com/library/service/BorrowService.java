package com.library.service;

import com.library.model.mongodb.ActivityLog;
import com.library.model.mongodb.BorrowHistory;
import com.library.model.mysql.Book;
import com.library.model.mysql.BorrowRecord;
import com.library.model.mysql.Member;
import com.library.repository.mongodb.ActivityLogRepository;
import com.library.repository.mongodb.BorrowHistoryRepository;
import com.library.repository.mysql.BookRepository;
import com.library.repository.mysql.BorrowRecordRepository;
import com.library.repository.mysql.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowHistoryRepository borrowHistoryRepository;
    private final ActivityLogRepository activityLogRepository;

    // Fine per day in rupees
    private static final double FINE_PER_DAY = 5.0;

    // Borrow period in days
    private static final int BORROW_DAYS = 14;

    // ===============================
    // ISSUE BOOK
    // ===============================
    public BorrowRecord issueBook(Long memberId, Long bookId) {

        // Find member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found: " + memberId));

        // Find book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found: " + bookId));

        // Check member is active
        if (member.getStatus() != Member.MemberStatus.ACTIVE) {
            throw new RuntimeException("Member is not active!");
        }

        // Check book availability
        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException(
                    "Book is not available: " + book.getTitle());
        }

        // Check if member already borrowed this book
        borrowRecordRepository.findByMemberAndBookAndStatus(
                        member, book, BorrowRecord.BorrowStatus.BORROWED)
                .ifPresent(b -> {
                    throw new RuntimeException(
                            "Member already borrowed this book!");
                });

        // Create borrow record
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setMember(member);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(BORROW_DAYS));
        borrowRecord.setStatus(BorrowRecord.BorrowStatus.BORROWED);
        borrowRecord.setFineAmount(0.0);

        // Reduce available quantity
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        // Save borrow record
        BorrowRecord saved = borrowRecordRepository.save(borrowRecord);

        // Save to MongoDB history
        saveBorrowHistory(saved, "BORROWED");

        // Save activity log
        saveActivityLog(
                "BORROW",
                "system",
                "Book '" + book.getTitle() +
                        "' borrowed by " + member.getFirstName(),
                "BORROW",
                saved.getId()
        );

        return saved;
    }

    // ===============================
    // RETURN BOOK
    // ===============================
    public BorrowRecord returnBook(Long borrowRecordId) {

        // Find borrow record
        BorrowRecord borrowRecord = borrowRecordRepository
                .findById(borrowRecordId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Borrow record not found: " + borrowRecordId));

        // Check if already returned
        if (borrowRecord.getStatus() == BorrowRecord.BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned!");
        }

        // Set return date
        borrowRecord.setReturnDate(LocalDate.now());

        // Calculate fine
        double fine = calculateFine(
                borrowRecord.getDueDate(),
                LocalDate.now()
        );
        borrowRecord.setFineAmount(fine);

        // Update status
        borrowRecord.setStatus(BorrowRecord.BorrowStatus.RETURNED);

        // Increase available quantity
        Book book = borrowRecord.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        // Save updated borrow record
        BorrowRecord updated = borrowRecordRepository.save(borrowRecord);

        // Save to MongoDB history
        saveBorrowHistory(updated, "RETURNED");

        // Save activity log
        saveActivityLog(
                "RETURN",
                "system",
                "Book '" + book.getTitle() +
                        "' returned by " + borrowRecord.getMember().getFirstName() +
                        (fine > 0 ? " | Fine: ₹" + fine : ""),
                "BORROW",
                updated.getId()
        );

        return updated;
    }

    // ===============================
    // CALCULATE FINE
    // ===============================
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysLate * FINE_PER_DAY;
        }
        return 0.0;
    }

    // ===============================
    // GET ALL BORROW RECORDS
    // ===============================
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    // ===============================
    // GET ACTIVE BORROWS
    // ===============================
    public List<BorrowRecord> getActiveBorrows() {
        return borrowRecordRepository
                .findByStatus(BorrowRecord.BorrowStatus.BORROWED);
    }

    // ===============================
    // GET OVERDUE BOOKS
    // ===============================
    public List<BorrowRecord> getOverdueBooks() {
        return borrowRecordRepository
                .findByStatusAndDueDateBefore(
                        BorrowRecord.BorrowStatus.BORROWED,
                        LocalDate.now()
                );
    }

    // ===============================
    // GET BORROW HISTORY BY MEMBER
    // ===============================
    public List<BorrowRecord> getMemberBorrowHistory(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found: " + memberId));
        return borrowRecordRepository.findByMember(member);
    }

    // ===============================
    // SAVE TO MONGODB HISTORY
    // ===============================
    private void saveBorrowHistory(BorrowRecord record, String action) {
        BorrowHistory history = new BorrowHistory();
        history.setBorrowRecordId(record.getId());
        history.setMemberId(record.getMember().getId());
        history.setMemberName(
                record.getMember().getFirstName() + " " +
                        record.getMember().getLastName()
        );
        history.setMembershipId(record.getMember().getMembershipId());
        history.setBookId(record.getBook().getId());
        history.setBookTitle(record.getBook().getTitle());
        history.setBookAuthor(record.getBook().getAuthor());
        history.setBookIsbn(record.getBook().getIsbn());
        history.setAction(action);
        history.setActionDate(LocalDateTime.now());
        history.setFineAmount(record.getFineAmount());
        history.setFinePaid(record.isFinePaid());
        borrowHistoryRepository.save(history);
    }

    // ===============================
    // SAVE ACTIVITY LOG TO MONGODB
    // ===============================
    private void saveActivityLog(String action, String performedBy,
                                 String description, String entityType, Long entityId) {
        ActivityLog log = new ActivityLog();
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        activityLogRepository.save(log);
    }
}

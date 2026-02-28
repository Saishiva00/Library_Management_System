package com.library.repository.mysql;

import com.library.model.mysql.BorrowRecord;
import com.library.model.mysql.Member;
import com.library.model.mysql.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository
        extends JpaRepository<BorrowRecord, Long> {

    // Find all borrows by member
    List<BorrowRecord> findByMember(Member member);

    // Find all borrows by book
    List<BorrowRecord> findByBook(Book book);

    // Find active borrows by member
    List<BorrowRecord> findByMemberAndStatus(
            Member member,
            BorrowRecord.BorrowStatus status);

    // Find overdue records
    List<BorrowRecord> findByStatusAndDueDateBefore(
            BorrowRecord.BorrowStatus status,
            LocalDate date);

    // Check if member has already borrowed this book
    Optional<BorrowRecord> findByMemberAndBookAndStatus(
            Member member,
            Book book,
            BorrowRecord.BorrowStatus status);

    // Find all active borrows
    List<BorrowRecord> findByStatus(BorrowRecord.BorrowStatus status);
}
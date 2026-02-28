package com.library.repository.mongodb;

import com.library.model.mongodb.BorrowHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowHistoryRepository
        extends MongoRepository<BorrowHistory, String> {

    // Find history by member
    List<BorrowHistory> findByMemberId(Long memberId);

    // Find history by book
    List<BorrowHistory> findByBookId(Long bookId);

    // Find history by action
    List<BorrowHistory> findByAction(String action);

    // Find by borrowRecordId
    List<BorrowHistory> findByBorrowRecordId(Long borrowRecordId);
}
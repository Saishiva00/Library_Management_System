package com.library.repository.mongodb;

import com.library.model.mongodb.ActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository
        extends MongoRepository<ActivityLog, String> {

    List<ActivityLog> findByPerformedBy(String performedBy);
    List<ActivityLog> findByEntityType(String entityType);
    List<ActivityLog> findByAction(String action);
}
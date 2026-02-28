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
@Document(collection = "activity_logs")
public class ActivityLog {

    @Id
    private String id;

    private String action;        // ADD_BOOK, DELETE_BOOK, BORROW, RETURN
    private String performedBy;   // username
    private String description;   // what happened
    private LocalDateTime timestamp;
    private String entityType;    // BOOK, MEMBER, BORROW
    private Long entityId;        // ID of affected entity
}
package com.example.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlanDTO {

    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer storageLimit;

    private Integer maxUsers;

    private Integer maxDocuments;

    private BigDecimal additionalDocumentPrice;

    private Boolean hasDocumentConstructor;

    private Boolean hasLegalDatabaseAccess;

    private Boolean hasAdvancedLegalDatabase;

    private Boolean hasTemplates;

    private Integer templatesCount;

    private Boolean hasExpertSupport;

    private Boolean hasReviewFunctionality;

    private Integer maxReviews;

    private Boolean unlimitedDocuments;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

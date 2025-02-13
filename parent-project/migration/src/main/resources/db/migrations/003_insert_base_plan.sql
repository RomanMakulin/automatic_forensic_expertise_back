-- Вставка трех тарифных планов в таблицу Plan

INSERT INTO Plan (name, description, price, storage_limit, max_users, max_documents, additional_document_price,
                  has_document_constructor, has_legal_database_access, has_advanced_legal_database, has_templates, templates_count,
                  has_expert_support, has_review_functionality, max_reviews, unlimited_documents, active, created_at, updated_at)
VALUES
    (
     'Basic Plan',
     'Registration of 1 expert, legal document constructor, access to the NPB, 10 templates, 10 free downloads, 10 GB of storage',
     20000.00,
     10,  -- GB of storage
     1,  -- 1 user
     10, -- 10 free downloads
     2500.00,
     TRUE,
     TRUE,
     FALSE, -- No advanced legal database
     TRUE,
     10,
     FALSE,
     FALSE,
     NULL,
     FALSE,
     TRUE,
     NOW(),
     NOW()
    ),
    (
     'Advanced Plan',
     'All features of the Basic Plan + more legal documents, 20 free downloads, 25 GB of storage',
     45000.00,
     25,  -- GB of storage
     1,  -- 1 user
     20, -- 20 free downloads
     2500.00,
     TRUE,
     TRUE,
     FALSE, -- No advanced legal database
     TRUE,
     10,
     FALSE,
     FALSE,
     NULL,
     FALSE,
     TRUE,
     NOW(),
     NOW()
    ),
    (
     'Professional Plan',
     'All features of the Advanced Plan + expert support, review functionality, unlimited downloads, extended NPB, 100 GB of storage',
     350000.00,
     100,  -- GB of storage
     5,  -- 5 users
     NULL, -- Unlimited downloads
     0.00,
     TRUE,
     TRUE,
     TRUE, -- Extended legal database
     TRUE,
     10,
     TRUE, -- Expert support
     TRUE, -- Review functionality
     10, -- 10 reviews
     TRUE, -- Unlimited downloads
     TRUE,
     NOW(),
     NOW()
    );


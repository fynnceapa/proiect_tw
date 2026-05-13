SET search_path = project, pg_catalog;

CREATE TABLE feedback (
    id UUID PRIMARY KEY,
    topic TEXT NOT NULL,
    tone TEXT NOT NULL,
    allow_contact BOOLEAN NOT NULL DEFAULT FALSE,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    user_id UUID NOT NULL,
    review_id UUID,
    FOREIGN KEY (user_id) REFERENCES project.users (id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES project.reviews (id) ON DELETE SET NULL
);

-- V2__create_events.sql

CREATE TABLE events (
    id              UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    title           VARCHAR(255)   NOT NULL,
    description     TEXT,
    venue           VARCHAR(255)   NOT NULL,
    event_date      TIMESTAMP      NOT NULL,
    total_seats     INT            NOT NULL,
    available_seats INT            NOT NULL,
    price           NUMERIC(10, 2) NOT NULL,
    created_by      UUID           NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    status          VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_event_status         CHECK (status IN ('ACTIVE', 'CANCELLED', 'COMPLETED')),
    CONSTRAINT chk_total_seats_pos      CHECK (total_seats > 0),
    CONSTRAINT chk_available_seats_pos  CHECK (available_seats >= 0),
    CONSTRAINT chk_seats_consistency    CHECK (available_seats <= total_seats),
    CONSTRAINT chk_price_pos            CHECK (price >= 0)
);

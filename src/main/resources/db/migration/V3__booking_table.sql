
CREATE TABLE bookings (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_ref VARCHAR(20) NOT NULL,
    user_id     UUID        NOT NULL REFERENCES users(id)  ON DELETE CASCADE,
    event_id    UUID        NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    status      VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    seats_booked INT        NOT NULL DEFAULT 1,
    booked_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    cancelled_at TIMESTAMP,

    CONSTRAINT uq_booking_ref       UNIQUE (booking_ref),
    CONSTRAINT chk_booking_status   CHECK  (status IN ('CONFIRMED', 'CANCELLED')),
    CONSTRAINT chk_seats_booked_pos CHECK  (seats_booked > 0)
);
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    capacity INTEGER NOT NULL,
    location VARCHAR(180) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_rooms_capacity CHECK (capacity > 0)
);

CREATE TABLE room_resources (
    room_id BIGINT NOT NULL REFERENCES rooms (id) ON DELETE CASCADE,
    resource VARCHAR(40) NOT NULL,
    PRIMARY KEY (room_id, resource)
);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id),
    room_id BIGINT NOT NULL REFERENCES rooms (id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    rejection_reason VARCHAR(255),
    reminder_sent BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_reservations_period CHECK (end_time > start_time),
    CONSTRAINT no_overlapping_confirmed_reservations
        EXCLUDE USING gist (
            room_id WITH =,
            tsrange(start_time, end_time, '[)') WITH &&
        )
        WHERE (status = 'CONFIRMADA')
);

CREATE INDEX idx_reservations_room_period ON reservations (room_id, start_time, end_time);
CREATE INDEX idx_reservations_user ON reservations (user_id);
CREATE INDEX idx_reservations_status ON reservations (status);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id),
    reservation_id BIGINT REFERENCES reservations (id) ON DELETE SET NULL,
    type VARCHAR(40) NOT NULL,
    message VARCHAR(500) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_notifications_user ON notifications (user_id);
CREATE INDEX idx_notifications_created_at ON notifications (created_at);

CREATE TABLE address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(150) NOT NULL,
    number VARCHAR(20) NOT NULL,
    city VARCHAR(100) NOT NULL,
    cep VARCHAR(15) NOT NULL
);

CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY UNIQUE NOT NULL,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    login VARCHAR(128) NOT NULL,
    role VARCHAR(16) NOT NULL,
    date_last_change TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    address_id INTEGER,

    CONSTRAINT fk_user_address
        FOREIGN KEY (address_id)
        REFERENCES address (id)
);

INSERT INTO users (
    id,
    name,
    email,
    password,
    login,
    role,
    date_last_change,
    address_id
) VALUES (
    'd47f875a-e3dc-4935-8617-df1f51ad5bb1',
    'admin',
    'admin@admin.com',
    '$2a$10$1jlsF3sQi9v7eC7796jgQup6TyK7L.HFg8ks22qzCl.U4TbPDyDaW',
    'admin',
    'OWNER',
    '2026-01-19 22:58:26.607782',
    NULL
);
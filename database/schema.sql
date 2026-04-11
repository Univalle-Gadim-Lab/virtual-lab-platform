CREATE TABLE IF NOT EXISTS users
(
    id             VARCHAR(100) NOT NULL PRIMARY KEY,
    name           VARCHAR(200) NOT NULL,
    last_name      VARCHAR(200) NOT NULL,
    external_code  VARCHAR(100),
    password       VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    created_date   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles
(
    id       VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id  VARCHAR(100) NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    UNIQUE(user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
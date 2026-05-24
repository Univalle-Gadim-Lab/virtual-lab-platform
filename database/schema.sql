CREATE TABLE IF NOT EXISTS users
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    name          VARCHAR(200) NOT NULL,
    last_name     VARCHAR(200) NOT NULL,
    external_code VARCHAR(100),
    password      VARCHAR(255) NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    created_date  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles
(
    id      VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    role    VARCHAR(20)  NOT NULL,
    UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS instances
(
    id               VARCHAR(100) NOT NULL PRIMARY KEY,
    name             VARCHAR(200) NOT NULL,
    description      VARCHAR(500),
    external_ip      VARCHAR(45),
    image_name       VARCHAR(200) NOT NULL,
    image_version    VARCHAR(50)  NOT NULL,
    image_registry   VARCHAR(200),
    cpu_cores        INTEGER      NOT NULL,
    memory_mb        INTEGER      NOT NULL,
    storage_mb       INTEGER      NOT NULL,
    gpu_enabled      BOOLEAN      NOT NULL,
    exposed_port     INTEGER,
    internal_ip      VARCHAR(45),
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at       TIMESTAMP,
    started_at       TIMESTAMP,
    stopped_at       TIMESTAMP,
    deleted_at       TIMESTAMP,
    last_accessed_at TIMESTAMP,
    status           VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS instance_metrics
(
    id                   VARCHAR(100) NOT NULL PRIMARY KEY,
    instance_id          VARCHAR(100) NOT NULL,
    current_cpu_usage    DOUBLE PRECISION,
    current_memory_usage DOUBLE PRECISION,
    current_disk_usage   DOUBLE PRECISION,
    current_time_usage   DOUBLE PRECISION,
    FOREIGN KEY (instance_id) REFERENCES instances (id)
);

CREATE TABLE IF NOT EXISTS instance_users
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,
    instance_id VARCHAR(100) NOT NULL,
    user_id     VARCHAR(100) NOT NULL,
    FOREIGN KEY (instance_id) REFERENCES instances (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
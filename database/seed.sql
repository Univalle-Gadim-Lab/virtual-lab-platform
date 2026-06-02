-- Seed data for Virtual Lab Platform
-- Default users: admin, teacher, student
-- Run this script after schema.sql:
--   psql -U postgres -d gadim_virtual_lab -f database/seed.sql

-- Default admin user (username: admin@correounivalle.edu.co, password: admin.GADYM.2026)
INSERT INTO users (id, name, last_name, password, status, created_date)
VALUES ('000000000000000000000001', 'admin@correounivalle.edu.co', 'Admin',
        '$2b$10$4KHdtMcWe1DrieG14gijg.aIrr9YKEUxjJuHPsF.kW/wWH.k1fFfG', 'ACTIVE', NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (id, user_id, role)
VALUES ('000000000000000000000002', '000000000000000000000001', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

-- Default teacher user (username: teacher@correounivalle.edu.co, password: teacher.GADYM.2026)
INSERT INTO users (id, name, last_name, password, status, created_date)
VALUES ('000000000000000000000003', 'teacher@correounivalle.edu.co', 'Teacher',
        '$2b$10$CAzosnqiDuz4XIHp7fpc6ejff7fIz6ADgofGHPztTyoD6oPahlNua', 'ACTIVE', NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (id, user_id, role)
VALUES ('000000000000000000000004', '000000000000000000000003', 'TEACHER')
ON CONFLICT (id) DO NOTHING;

-- Default student user (username: student@correounivalle.edu.co, password: student.GADYM.2026)
INSERT INTO users (id, name, last_name, password, status, created_date)
VALUES ('000000000000000000000005', 'student@correounivalle.edu.co', 'Student',
        '$2b$10$Xp6ldYVymY5nCRfYEQk9AuuwV6IpR7x8M0OITwrgaHyt7GmCtW4Fy', 'ACTIVE', NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (id, user_id, role)
VALUES ('000000000000000000000006', '000000000000000000000005', 'STUDENT')
ON CONFLICT (id) DO NOTHING;
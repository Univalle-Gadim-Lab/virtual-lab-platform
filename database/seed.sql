-- Seed data for Virtual Lab Platform
-- Default users: admin, teacher, student
-- Run this script after schema.sql:
--   psql -U postgres -d gadim_virtual_lab -f database/seed.sql

-- Default admin user (email: admin@correounivalle.edu.co, password: admin.GADYM.2026)
INSERT INTO users (id, name, last_name, password, status, created_date)
VALUES ('admin@correounivalle.edu.co', 'Administrador', 'GADIM',
        '$2b$10$4KHdtMcWe1DrieG14gijg.aIrr9YKEUxjJuHPsF.kW/wWH.k1fFfG', 'ACTIVE', NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (id, user_id, role)
VALUES ('000000000000000000000002', 'admin@correounivalle.edu.co', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

-- Default teacher user (email: teacher@correounivalle.edu.co, password: teacher.GADYM.2026)
INSERT INTO users (id, name, last_name, password, status, created_date)
VALUES ('teacher@correounivalle.edu.co', 'Docente', 'GADIM',
        '$2b$10$CAzosnqiDuz4XIHp7fpc6ejff7fIz6ADgofGHPztTyoD6oPahlNua', 'ACTIVE', NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (id, user_id, role)
VALUES ('000000000000000000000004', 'teacher@correounivalle.edu.co', 'TEACHER')
ON CONFLICT (id) DO NOTHING;

-- Default student user (email: student@correounivalle.edu.co, password: student.GADYM.2026)
INSERT INTO users (id, name, last_name, password, status, created_date)
VALUES ('student@correounivalle.edu.co', 'Estudiante', 'GADIM',
        '$2b$10$Xp6ldYVymY5nCRfYEQk9AuuwV6IpR7x8M0OITwrgaHyt7GmCtW4Fy', 'ACTIVE', NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (id, user_id, role)
VALUES ('000000000000000000000006', 'student@correounivalle.edu.co', 'STUDENT')
ON CONFLICT (id) DO NOTHING;
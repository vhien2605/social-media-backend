--role and permission init data
INSERT INTO permission (id, create_at, update_at, name)
SELECT 1, NULL, NULL, 'read_user'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 1);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 2, NULL, NULL, 'create_user'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 2);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 3, NULL, NULL, 'update_user'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 3);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 4, NULL, NULL, 'delete_user'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 4);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 5, NULL, NULL, 'read_role'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 5);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 6, NULL, NULL, 'create_role'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 6);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 7, NULL, NULL, 'update_role'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 7);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 8, NULL, NULL, 'delete_role'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 8);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 9, NULL, NULL, 'read_product'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 9);
INSERT INTO permission (id, create_at, update_at, name)
SELECT 10, NULL, NULL, 'create_product'
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE id = 10);

INSERT INTO role (id, create_at, update_at, name)
SELECT 1, NULL, NULL, 'USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 1);
INSERT INTO role (id, create_at, update_at, name)
SELECT 2, NULL, NULL, 'GROUP_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 2);
INSERT INTO role (id, create_at, update_at, name)
SELECT 3, NULL, NULL, 'GROUP_USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 3);
INSERT INTO role (id, create_at, update_at, name)
SELECT 4, NULL, NULL, 'SYS_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 4);

--insert admin user
-- Insert admin user (ignore if exists)
INSERT IGNORE INTO `usr` (
    id,
    address,
    auth_provider,
    date_of_birth,
    education,
    email,
    full_name,
    image_url,
    password,
    provider_id,
    user_status,
    work,
    create_at,
    update_at,
    gender
) VALUES (
    1,
    'asasd',
    'STANDARD',
    '2005-02-03 00:00:00',
    'sad',
    'hvu6582@gmail.com',
    'Minh Hiến Vũ',
    'https://lh3.googleusercontent.com/a/ACg8ocJAonabm0ZRj3S8diVF_K3nrS6wF06C5NKhTooLi7piaRBzIw=s96-c',
    '$2a$10$t57F0BQCUeyePPAanxw3/OwCjtDAgD4Qejxavm/USimM47.49y2iO',
    '111510411124161790461',
    'OFFLINE',
    'asddas',
    '2025-07-22 12:14:08',
    '2025-07-22 12:14:08',
    'MALE'
);


-- User 1 → SYS_ADMIN
INSERT INTO user_role (user_id, role_id)
SELECT 1, 4
WHERE NOT EXISTS (
    SELECT 1 FROM users_roles WHERE user_id = 1 AND role_id = 4
);






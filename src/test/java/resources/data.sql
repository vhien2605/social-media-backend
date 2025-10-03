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
INSERT IGNORE INTO `usr` (
    id,
    address,
    auth_provider,
    date_of_birth,
    education,
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
    8,
    'Nam Định',
    'GOOGLE',
    '2005-10-02 07:00:00.000000',
    'Đại học Công Nghệ',
    'Minh Hiến Vũ',
    'https://sample/avatar',
    NULL,
    '111510411124161790461',
    'ONLINE',
    'backend engineer',
    '2025-08-12 23:34:49.382000',
    '2025-08-12 23:52:21.188000',
    'MALE'
);

-- User 1 → SYS_ADMIN
INSERT INTO user_role (user_id, role_id)
SELECT 1, 4
WHERE NOT EXISTS (
    SELECT 1 FROM users_roles WHERE user_id = 1 AND role_id = 4
);






CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO role (
    id,
    name,
    description,
    created_at,
    updated_at
)
SELECT
    gen_random_uuid(),
    'ADMIN',
    'System administrator',
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
    SELECT 1
    FROM role
    WHERE UPPER(name) = 'ADMIN'
);

UPDATE app_user
SET
    role_id = (
        SELECT id
        FROM role
        WHERE UPPER(name) = 'ADMIN'
    LIMIT 1
    ),
    updated_at = CURRENT_TIMESTAMP
WHERE LOWER(TRIM(email)) = 'alice.martin@example.com';
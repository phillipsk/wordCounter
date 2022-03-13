CREATE TABLE IF NOT EXISTS messages (
    id VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    word_entity_id  INTEGER NULL,
    word_count      INTEGER NULL
);
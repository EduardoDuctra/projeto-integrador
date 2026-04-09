ALTER TABLE conector
    ADD COLUMN IF NOT EXISTS connector_id INTEGER;


ALTER TABLE conector
    RENAME COLUMN id_conector TO id;
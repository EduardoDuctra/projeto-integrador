ALTER TABLE transacao
ALTER COLUMN id_transacao TYPE BIGINT
USING id_transacao::bigint;
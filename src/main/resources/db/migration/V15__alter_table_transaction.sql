
ALTER TABLE transacao_recarga RENAME TO transacao;

DROP TABLE IF EXISTS transacao_financeira CASCADE;

ALTER TABLE transacao
    RENAME COLUMN id_transacao TO id;

ALTER TABLE transacao
    ADD COLUMN id_transacao varchar(50),
    ADD COLUMN tipo varchar(50),
    ADD COLUMN valor_recarga DOUBLE PRECISION,
    ADD COLUMN valor_maximo DOUBLE PRECISION,
    ADD COLUMN valor_energia DOUBLE PRECISION;

ALTER TABLE transacao
DROP COLUMN IF EXISTS data_soc;
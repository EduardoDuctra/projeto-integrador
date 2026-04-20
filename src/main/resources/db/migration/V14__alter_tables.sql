ALTER TABLE veiculo
DROP CONSTRAINT IF EXISTS fk_veiculo_modelo;

ALTER TABLE veiculo
DROP COLUMN IF EXISTS id_modelo;


ALTER TABLE carregador
DROP CONSTRAINT IF EXISTS fk_carregador_eletroposto;

ALTER TABLE carregador
DROP COLUMN IF EXISTS id_eletroposto;
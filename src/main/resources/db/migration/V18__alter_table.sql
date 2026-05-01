ALTER TABLE usuario
    ADD COLUMN id_veiculo_principal BIGINT,
    ADD CONSTRAINT fk_usuario_veiculo_principal
    FOREIGN KEY (id_veiculo_principal)  REFERENCES veiculo(id_veiculo);
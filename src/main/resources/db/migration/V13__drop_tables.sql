drop table eletroposto cascade;

alter table carregador
add column endereco varchar(50),
add column cidade varchar(50);


ALTER TABLE transacao_recarga
DROP CONSTRAINT fk_transacao_conector;


ALTER TABLE veiculo
DROP CONSTRAINT fk_veiculo_modelo;

ALTER TABLE modelo
DROP CONSTRAINT IF EXISTS fk_modelo_marca;

ALTER TABLE conector
DROP CONSTRAINT conector_pkey;


ALTER TABLE conector
    ADD PRIMARY KEY (id);

ALTER TABLE carregador
ALTER COLUMN id_carregador TYPE VARCHAR(50);

ALTER TABLE conector
ALTER COLUMN id_carregador TYPE VARCHAR(50);


ALTER TABLE conector
DROP CONSTRAINT IF EXISTS fk_conector_carregador;


ALTER TABLE conector
    ADD CONSTRAINT fk_conector_carregador
        FOREIGN KEY (id_carregador)
            REFERENCES carregador(id_carregador);


ALTER TABLE conector
    ADD CONSTRAINT unique_conector_por_carregador
        UNIQUE (id_carregador, connector_id);


ALTER TABLE conector
ADD COLUMN nome_conector VARCHAR(50),
ADD COLUMN valor_energia DOUBLE PRECISION;


drop table modelo;
drop table marca;

alter table veiculo

add column modelo_carro varchar(50),
add column nome_marca varchar(50);

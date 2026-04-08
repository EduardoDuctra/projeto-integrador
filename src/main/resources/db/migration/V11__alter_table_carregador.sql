ALTER TABLE conector
DROP CONSTRAINT fk_conector_carregador;


ALTER TABLE carregador
ALTER COLUMN id_carregador TYPE VARCHAR(50);

ALTER TABLE conector
ALTER COLUMN id_carregador TYPE VARCHAR(50);



ALTER TABLE conector
    ADD CONSTRAINT fk_conector_carregador
        FOREIGN KEY (id_carregador)
            REFERENCES carregador(id_carregador);
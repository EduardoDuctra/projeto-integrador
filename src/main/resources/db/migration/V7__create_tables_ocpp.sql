create table eletroposto (
    id_eletroposto bigserial primary key,
    nome_eletroposto varchar (50),
    cidade varchar (50),
    uf char(2),
    endereco varchar(100),
    status_eletroposto varchar (50)
);


create table carregador (
    id_carregador bigserial primary key,
    numero_serie varchar (50),
    nome_marca varchar (50),
    nome_modelo varchar(50),
    firmware_version varchar(50),
    potencia_cc double precision,
    potencia_ca double precision,
    status_carregador varchar(50),

    id_eletroposto bigint, constraint fk_carregador_eletroposto
        foreign key (id_eletroposto) references eletroposto(id_eletroposto)
);


create table conector(
    id_conector bigserial primary key,
    tipo_conector CHAR(2),
    em_uso boolean,
    status_conector varchar(50),
    soc_recarga_atual double precision,
    data_ultima_atualizacao timestamp,

    id_carregador  bigint, constraint fk_conector_carregador
        foreign key (id_carregador) references carregador(id_carregador)
);

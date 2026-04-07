create table usuario (
                         id_usuario bigserial primary key,
                         nome_usuario varchar(100) not null,
                         cpf varchar(11) not null unique,
                         telefone varchar(20),
                         email varchar(100) not null unique,
                         senha varchar(255) not null
);

create table marca (
                       id_marca bigserial primary key,
                       nome_marca varchar(100) not null,
                       pais varchar(100) not null
);

create table modelo (
                        id_modelo bigserial primary key,
                        modelo_carro varchar(100) not null,
                        id_marca bigint not null,

                        constraint fk_modelo_marca
                            foreign key (id_marca)
                                references marca(id_marca)
);

create table veiculo (
                         id_veiculo bigserial primary key,
                         id_usuario bigint not null,
                         id_modelo bigint not null,

                         constraint fk_veiculo_usuario
                             foreign key (id_usuario)
                                 references usuario(id_usuario),

                         constraint fk_veiculo_modelo
                             foreign key (id_modelo)
                                 references modelo(id_modelo)
);

create table transacaoRecarga (
                           id_transacao bigserial primary key,
                           tipo varchar(50) not null,
                           valor double precision not null,
                           status_transacao varchar(50) not null,
                           data timestamp not null,
                           id_usuario bigint not null,

                           constraint fk_transacao_usuario
                               foreign key (id_usuario)
                                   references usuario(id_usuario)
);
create table transacao_financeira (
                                      id_transacao_financeira bigserial primary key,

                                      tipo varchar(50) not null,
                                      valor numeric(10,2) not null,
                                      status_transacao_financeira varchar(50) not null,

                                      data timestamp not null,

                                      id_usuario bigint not null,
                                      id_sessao_recarga bigint,

                                      constraint fk_transacao_usuario
                                          foreign key (id_usuario)
                                              references usuario(id_usuario),

                                      constraint fk_transacao_sessao
                                          foreign key (id_sessao_recarga)
                                              references transacaoRecarga(id_transacao)
);

alter table transacaoRecarga drop column tipo;
alter table transacaoRecarga drop column valor;


ALTER TABLE transacaoRecarga RENAME TO transacao_recarga;
alter table transacaoRecarga
    add column meter_start double precision,
    add column meter_stop double precision,
    add column data_inicio timestamp,
    add column data_fim timestamp,
    add column energia_consumida double precision,
    add column soc_atual double precision,
    add column data_soc timestamp;


alter table transacaoRecarga
    add column id_conector bigint;

alter table transacaoRecarga
    add constraint fk_transacao_conector
        foreign key (id_conector)
            references conector(id_conector);
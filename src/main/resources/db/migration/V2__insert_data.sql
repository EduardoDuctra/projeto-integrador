insert into marca (nome_marca, pais) values
                                         ('BYD', 'China'),
                                         ('Tesla', 'Estados Unidos'),
                                         ('Volvo', 'Suécia'),
                                         ('BMW', 'Alemanha'),
                                         ('Nissan', 'Japão'),
                                         ('Chevrolet', 'Estados Unidos'),
                                         ('Renault', 'França'),
                                         ('GWM', 'China'),
                                         ('JAC', 'China');
insert into modelo (modelo_carro, id_marca) values
                                                ('Dolphin', (select id_marca from marca where nome_marca = 'BYD')),
                                                ('Dolphin Mini', (select id_marca from marca where nome_marca = 'BYD')),
                                                ('Seal', (select id_marca from marca where nome_marca = 'BYD')),
                                                ('Yuan Plus', (select id_marca from marca where nome_marca = 'BYD')),
                                                ('Han EV', (select id_marca from marca where nome_marca = 'BYD')),
                                                ('Tang EV', (select id_marca from marca where nome_marca = 'BYD'));

insert into modelo (modelo_carro, id_marca) values
                                                ('Model 3', (select id_marca from marca where nome_marca = 'Tesla')),
                                                ('Model Y', (select id_marca from marca where nome_marca = 'Tesla')),
                                                ('Model S', (select id_marca from marca where nome_marca = 'Tesla')),
                                                ('Model X', (select id_marca from marca where nome_marca = 'Tesla'));

insert into modelo (modelo_carro, id_marca) values
                                                ('XC40 Recharge', (select id_marca from marca where nome_marca = 'Volvo')),
                                                ('C40 Recharge', (select id_marca from marca where nome_marca = 'Volvo')),
                                                ('EX30', (select id_marca from marca where nome_marca = 'Volvo'));


insert into modelo (modelo_carro, id_marca) values
                                                ('i3', (select id_marca from marca where nome_marca = 'BMW')),
                                                ('i4', (select id_marca from marca where nome_marca = 'BMW')),
                                                ('iX', (select id_marca from marca where nome_marca = 'BMW'));

insert into modelo (modelo_carro, id_marca) values
    ('Leaf', (select id_marca from marca where nome_marca = 'Nissan'));



insert into modelo (modelo_carro, id_marca) values
    ('Bolt EV', (select id_marca from marca where nome_marca = 'Chevrolet'));



insert into modelo (modelo_carro, id_marca) values
                                                ('Kwid E-Tech', (select id_marca from marca where nome_marca = 'Renault')),
                                                ('Zoe', (select id_marca from marca where nome_marca = 'Renault'));


insert into modelo (modelo_carro, id_marca) values
    ('Ora 03', (select id_marca from marca where nome_marca = 'GWM'));


insert into modelo (modelo_carro, id_marca) values
                                                ('E-JS1', (select id_marca from marca where nome_marca = 'JAC')),
                                                ('E-JS4', (select id_marca from marca where nome_marca = 'JAC'));

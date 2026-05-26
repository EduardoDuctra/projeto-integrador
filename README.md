# Projeto Integrador – Backend para Gerenciamento de Recarga Veicular

## Sobre o projeto

Este projeto consiste em um backend desenvolvido em Java com Spring Boot para gerenciamento de eletropostos da UFSM de recarga de veículos elétricos.

O sistema realiza:

* Cadastro e autenticação de usuários
* Gerenciamento de veículos
* Controle de carregadores e conectores
* Controle de transações de recarga
* Integração com Mercado Pago
* Comunicação em tempo real via WebSocket
* Integração com sistema OCPP para comunicação com carregadores

O backend foi desenvolvido para funcionar em conjunto com os códigos do servidor OCPP. Também foi utilizado um emulador, visto que os carregadores físicos estão desativados. Os códigos do servidor OCPP e do emulador foram desenvolvidos separadamente por terceiros, externamente à disciplina de Projeto Integrador.

---

## Tecnologias utilizadas

* Java
* Spring Boot
* Spring Security
* JWT
* PostgreSQL
* Flyway
* Maven
* WebSocket
* Mercado Pago API
* OCPP

---

## Arquitetura do sistema

O projeto segue uma arquitetura baseada em:

* Controllers
* Services
* Repositories
* DTOs
* Comunicação REST
* Comunicação em tempo real via WebSocket

---

## Integração com OCPP

Este backend depende da comunicação com um servidor OCPP externo.

Os códigos do servidor OCPP não fazem parte deste repositório e foram desenvolvidos separadamente.

A integração é utilizada para:

* Receber status dos carregadores
* Atualizar estado dos conectores
* Iniciar/parar recargas remotamente
* Sincronizar transações
* Comunicação em tempo real com estações de recarga

---


## API REST

A aplicação disponibiliza endpoints REST para:

* Usuários
* Veículos
* Carregadores
* Conectores
* Transações
* Pagamentos

---

## WebSocket

O sistema utiliza WebSocket para atualização em tempo real de:

* Saldo do usuário
* Estado dos carregadores
* Estado dos conectores
* Transações ativas

---

## Observações

* Este projeto foi desenvolvido para fins acadêmicos.
* O servidor OCPP utilizado na comunicação das estações não está incluído neste repositório.



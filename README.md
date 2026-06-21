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

## Como Rodar

1. Altere na classe PagamentoService o campo 
```java
preference.getSandboxInitPoint();
```  
para 

```java
preference.getInitPoint();
```

2. Crie o arquivo .env.prod com os seguintes itens 

```java
DB_URL=jdbc:postgresql://postgres:5432/projeto_integrador

MAIL_USERNAME=
MAIL_PASSWORD=

MP_TOKEN=

CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=

OCPP_SECRET=OcppUfsm2021
OCPP_ISSUER=backend-java

WEBHOOK_URL=http://IP-servidor:porta/projeto-integrador/webhook
URL_OCPP_SERVER=http://IP-servidor:porta
```

3. Na página da API do MercadoPago, configure o Webhook
```java
//IP-servidor:porta/projeto-integrador/webhook
```

4. No servidor, rode o comando
```java
docker compose up --build -d
```

---

## Observações


* O servidor OCPP utilizado na comunicação das estações não está incluído neste repositório.



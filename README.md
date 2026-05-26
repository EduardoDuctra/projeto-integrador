# Projeto Integrador – Backend para Gerenciamento de Recarga Veicular

## Sobre o projeto

Este projeto consiste em um backend desenvolvido em Java com Spring Boot para gerenciamento de recarga de veículos elétricos.

O sistema realiza:

* Cadastro e autenticação de usuários
* Gerenciamento de veículos
* Controle de carregadores e conectores
* Controle de transações de recarga
* Integração com Mercado Pago
* Comunicação em tempo real via WebSocket
* Integração com sistema OCPP para comunicação com carregadores

O backend foi desenvolvido para funcionar em conjunto com os códigos do servidor OCPP desenvolvidos por outro integrante/equipe do projeto.

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
* Docker
* OCPP

---

## Arquitetura do sistema

O projeto segue uma arquitetura baseada em:

* Controllers
* Services
* Repositories
* DTOs
* Configurações centralizadas
* Tratamento global de erros
* Comunicação REST
* Comunicação em tempo real via WebSocket

---

## Integração com OCPP

Este backend depende da comunicação com um servidor OCPP externo.

Os códigos do servidor OCPP não fazem parte deste repositório e foram desenvolvidos separadamente por outra pessoa/equipe.

A integração é utilizada para:

* Receber status dos carregadores
* Atualizar estado dos conectores
* Iniciar/parar recargas remotamente
* Sincronizar transações
* Comunicação em tempo real com estações de recarga

---

## Funcionalidades principais

### Usuários

* Cadastro de usuários
* Login com JWT
* Recuperação de senha
* Atualização de dados
* Controle de saldo

### Veículos

* Cadastro de veículos
* Listagem de veículos
* Seleção de veículo principal

### Carregadores

* Listagem de carregadores
* Atualização de status
* Consulta de conectores disponíveis

### Recargas

* Criação de transações
* Controle de recarga ativa
* Histórico de transações
* Atualização em tempo real

### Pagamentos

* Integração com Mercado Pago
* Webhook de confirmação de pagamento
* Atualização automática de saldo

---

## Estrutura do projeto

```text
src/main/java/
 ├── config
 ├── controller
 ├── exception
 ├── infra
 ├── mercadoPago
 ├── model
 ├── repository
 ├── security
 ├── service
 └── websocket
```

---

## Configuração do banco de dados

O projeto utiliza PostgreSQL.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projeto_integrador
spring.datasource.username=postgres
spring.datasource.password=senha
```

---

## Executando o projeto

### Clonar o repositório

```bash
git clone <url-do-repositorio>
```

### Executar com Maven

```bash
./mvnw spring-boot:run
```

Ou:

```bash
mvn spring-boot:run
```

---

## Docker

O projeto pode ser executado utilizando Docker.

Exemplo:

```bash
docker compose up --build
```

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
* Algumas integrações dependem de variáveis e serviços externos.

---

## Autor

Projeto desenvolvido por Eduardo Ductra.

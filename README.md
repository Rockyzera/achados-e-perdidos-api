# API de Achados e Perdidos

API REST para registrar e localizar itens perdidos e encontrados.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- PostgreSQL

## Estrutura

O código será organizado em camadas e pacotes:

- `controller`: endpoints HTTP
- `service`: regras de negócio e casos de uso
- `repository`: persistência com Spring Data JPA
- `model`: entidades do domínio
- `dto`: dados de entrada e saída da API
- `exception`: erros e respostas de erro
- `config`: configurações da aplicação

## Situação atual

Projeto inicial gerado pelo Spring Initializr. A estrutura de pacotes está preparada; a modelagem do domínio, os endpoints, as regras de negócio, a configuração do banco e a documentação OpenAPI serão implementados nas próximas etapas.

## Requisitos da atividade

- API REST com operações HTTP e JSON
- Banco de dados relacional e entidades relacionadas
- CRUD para pelo menos duas entidades
- No mínimo oito endpoints funcionais
- Pelo menos duas regras de negócio e validação dos dados
- Documentação com Swagger/OpenAPI

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

## Modelo do domínio

- `Item` registra um objeto perdido ou encontrado e guarda a data da ocorrência, dados para contato, status, categoria e local.
- `Categoria` classifica itens, como documentos, eletrônicos ou acessórios.
- `Local` representa o lugar da ocorrência, com nome, detalhes, cidade e estado.
- Cada `Item` pertence a uma `Categoria` e a um `Local`.

O status começa como `ABERTO`. O fluxo permitido é `ABERTO` para `EM_CONTATO` ou `CANCELADO`; `EM_CONTATO` pode ir para `RESOLVIDO` ou `CANCELADO`. `RESOLVIDO` e `CANCELADO` são estados finais. A data da ocorrência não pode estar no futuro. Nomes de categoria não podem se repetir, e categorias ou locais associados a itens não podem ser excluídos.

## Situação atual

Projeto inicial gerado pelo Spring Initializr. A estrutura, o domínio, os repositórios, DTOs, serviços e controllers estão implementados. A configuração do banco e a documentação OpenAPI ainda precisam ser concluídas.

## Endpoints disponíveis

- `/api/itens`: listar, buscar por ID, cadastrar, atualizar, alterar status e excluir itens.
- `/api/categorias`: listar, buscar por ID, cadastrar, atualizar e excluir categorias.
- `/api/locais`: listar, buscar por ID, cadastrar, atualizar e excluir locais.

Os cadastros retornam `201 Created` com o endereço do recurso criado. Exclusões retornam `204 No Content`. Erros de validação retornam `400`, recursos inexistentes `404` e conflitos de regra de negócio `409`.

## Requisitos da atividade

- API REST com operações HTTP e JSON
- Banco de dados relacional e entidades relacionadas
- CRUD para pelo menos duas entidades
- No mínimo oito endpoints funcionais
- Pelo menos duas regras de negócio e validação dos dados
- Documentação com Swagger/OpenAPI

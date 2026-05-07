# Desafio Técnico — API de Biblioteca Digital

## Contexto

Você foi contratado para desenvolver uma API REST para uma **Biblioteca Digital**.

A biblioteca precisa controlar seus livros, autores, categorias, usuários, empréstimos e devoluções. O objetivo é criar uma aplicação backend organizada, com boas práticas de desenvolvimento usando **Java com Spring Boot**.

Este desafio foi pensado para uma vaga de **Desenvolvedor Backend Júnior**, mas espera-se que o candidato demonstre atenção à arquitetura, separação de responsabilidades, tratamento de erros e clareza na modelagem do domínio.

---

# Objetivo

Desenvolver uma API REST que permita:

```text
Cadastrar autores
Cadastrar categorias
Cadastrar livros
Cadastrar usuários
Realizar empréstimos de livros
Registrar devoluções
Consultar livros disponíveis
Consultar empréstimos ativos
Controlar regras de negócio da biblioteca
```

A aplicação deve seguir uma estrutura organizada, utilizando:

```text
Controllers
Services
Repositories
Entities
DTOs
Exceptions
Validações
Mappers
Tratamento global de erros
```

---

# Requisitos técnicos obrigatórios

A aplicação deve ser desenvolvida com:

```text
Java 17 ou superior
Spring Boot
Spring Web
Spring Data JPA
Bean Validation
Banco H2 ou PostgreSQL
Maven ou Gradle
```

Opcionalmente, você pode usar:

```text
Lombok
Springdoc OpenAPI / Swagger
Docker
Testes com JUnit e Mockito
```

---

# Domínio da aplicação

A aplicação deve conter, no mínimo, os seguintes recursos:

```text
Author
Category
Book
User
Loan
```

---

# Recurso: Author

Representa um autor de livros.

## Campos mínimos

```text
id
name
bio
createdAt
```

## Endpoints esperados

```http
POST   /authors
GET    /authors
GET    /authors/{id}
PUT    /authors/{id}
DELETE /authors/{id}
```

## Regras

O nome do autor é obrigatório.

Não deve ser possível buscar, atualizar ou deletar um autor inexistente.

---

# Recurso: Category

Representa uma categoria de livros.

## Campos mínimos

```text
id
name
description
createdAt
```

## Endpoints esperados

```http
POST   /categories
GET    /categories
GET    /categories/{id}
PUT    /categories/{id}
DELETE /categories/{id}
```

## Regras

O nome da categoria é obrigatório.

Não deve ser possível buscar, atualizar ou deletar uma categoria inexistente.

---

# Recurso: Book

Representa um livro disponível na biblioteca.

## Campos mínimos

```text
id
title
isbn
status
author
category
createdAt
```

## Status possíveis

```text
AVAILABLE
BORROWED
INACTIVE
```

## Endpoints esperados

```http
POST   /books
GET    /books
GET    /books/{id}
PUT    /books/{id}
DELETE /books/{id}
GET    /books/available
```

## Regras

O título é obrigatório.

O ISBN é obrigatório.

Todo livro deve estar vinculado a um autor existente.

Todo livro deve estar vinculado a uma categoria existente.

Ao cadastrar um livro, ele deve iniciar com status:

```text
AVAILABLE
```

Não deve ser possível emprestar um livro com status:

```text
BORROWED
INACTIVE
```

A remoção de um livro pode ser lógica, alterando seu status para:

```text
INACTIVE
```

---

# Recurso: User

Representa um usuário da biblioteca.

## Campos mínimos

```text
id
name
email
status
createdAt
```

## Status possíveis

```text
ACTIVE
BLOCKED
```

## Endpoints esperados

```http
POST   /users
GET    /users
GET    /users/{id}
PUT    /users/{id}
PATCH  /users/{id}/block
PATCH  /users/{id}/activate
```

## Regras

O nome é obrigatório.

O e-mail é obrigatório e deve ter formato válido.

Ao cadastrar um usuário, ele deve iniciar com status:

```text
ACTIVE
```

Usuários bloqueados não podem realizar empréstimos.

---

# Recurso: Loan

Representa um empréstimo de livro.

## Campos mínimos

```text
id
user
book
loanDate
dueDate
returnDate
status
```

## Status possíveis

```text
ACTIVE
RETURNED
LATE
```

## Endpoints esperados

```http
POST  /loans
GET   /loans
GET   /loans/{id}
GET   /loans/user/{userId}
GET   /loans?status=ACTIVE
PATCH /loans/{id}/return
```

---

# Regras de negócio para empréstimos

## 1. Usuário inexistente

Não deve ser possível criar empréstimo para um usuário inexistente.

A API deve retornar erro adequado.

---

## 2. Livro inexistente

Não deve ser possível criar empréstimo para um livro inexistente.

A API deve retornar erro adequado.

---

## 3. Usuário bloqueado

Usuários com status:

```text
BLOCKED
```

não podem realizar empréstimos.

---

## 4. Livro indisponível

Livros com status diferente de:

```text
AVAILABLE
```

não podem ser emprestados.

---

## 5. Limite de empréstimos ativos

Um usuário não pode ter mais de **3 empréstimos ativos** ao mesmo tempo.

Empréstimos ativos são aqueles com status:

```text
ACTIVE
```

---

## 6. Prazo de devolução

Ao criar um empréstimo, o sistema deve definir:

```text
loanDate = data atual
dueDate = data atual + 7 dias
status = ACTIVE
```

---

## 7. Alteração de status do livro

Ao criar um empréstimo, o livro emprestado deve ter seu status alterado para:

```text
BORROWED
```

---

## 8. Devolução de livro

Ao registrar a devolução:

```text
returnDate = data atual
book.status = AVAILABLE
```

Se a devolução ocorrer dentro do prazo, o empréstimo deve ficar com status:

```text
RETURNED
```

Se a devolução ocorrer após o prazo, o empréstimo deve ficar com status:

```text
LATE
```

---

## 9. Empréstimo já devolvido

Não deve ser possível devolver novamente um empréstimo já encerrado.

---

# DTOs obrigatórios

A API não deve expor diretamente as entidades JPA nas requisições e respostas.

Você deve criar DTOs para entrada e saída.

Exemplo:

```text
AuthorRequestDTO
AuthorResponseDTO

CategoryRequestDTO
CategoryResponseDTO

BookRequestDTO
BookResponseDTO

UserRequestDTO
UserResponseDTO

LoanRequestDTO
LoanResponseDTO

ErrorResponseDTO
```

---

# Exemplo de payloads

## Criar autor

```http
POST /authors
```

```json
{
  "name": "Robert C. Martin",
  "bio": "Software engineer and author."
}
```

Resposta esperada:

```json
{
  "id": 1,
  "name": "Robert C. Martin",
  "bio": "Software engineer and author.",
  "createdAt": "2026-05-02"
}
```

---

## Criar categoria

```http
POST /categories
```

```json
{
  "name": "Software Engineering",
  "description": "Books about software development and architecture."
}
```

---

## Criar livro

```http
POST /books
```

```json
{
  "title": "Clean Code",
  "isbn": "9780132350884",
  "authorId": 1,
  "categoryId": 1
}
```

Resposta esperada:

```json
{
  "id": 1,
  "title": "Clean Code",
  "isbn": "9780132350884",
  "status": "AVAILABLE",
  "authorName": "Robert C. Martin",
  "categoryName": "Software Engineering"
}
```

---

## Criar usuário

```http
POST /users
```

```json
{
  "name": "John Doe",
  "email": "john.doe@email.com"
}
```

Resposta esperada:

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@email.com",
  "status": "ACTIVE",
  "createdAt": "2026-05-02"
}
```

---

## Criar empréstimo

```http
POST /loans
```

```json
{
  "userId": 1,
  "bookId": 1
}
```

Resposta esperada:

```json
{
  "id": 1,
  "userName": "John Doe",
  "bookTitle": "Clean Code",
  "loanDate": "2026-05-02",
  "dueDate": "2026-05-09",
  "returnDate": null,
  "status": "ACTIVE"
}
```

---

## Devolver livro

```http
PATCH /loans/1/return
```

Resposta esperada:

```json
{
  "id": 1,
  "userName": "John Doe",
  "bookTitle": "Clean Code",
  "loanDate": "2026-05-02",
  "dueDate": "2026-05-09",
  "returnDate": "2026-05-02",
  "status": "RETURNED"
}
```

---

# Tratamento de erros

A API deve possuir tratamento global de exceções usando:

```java
@RestControllerAdvice
```

A resposta de erro deve seguir um formato padronizado.

Exemplo:

```json
{
  "code": "RESOURCE_NOT_FOUND",
  "message": "Book not found",
  "timestamp": "2026-05-02T10:30:00"
}
```

Exemplos de erros esperados:

```text
AUTHOR_NOT_FOUND
CATEGORY_NOT_FOUND
BOOK_NOT_FOUND
USER_NOT_FOUND
LOAN_NOT_FOUND
BOOK_UNAVAILABLE
USER_BLOCKED
LOAN_ALREADY_RETURNED
MAX_ACTIVE_LOANS_REACHED
VALIDATION_ERROR
```

---

# Validações obrigatórias

Use Bean Validation nos DTOs de entrada.

Exemplos:

```java
@NotBlank
@NotNull
@Email
@Size
@Positive
```

Validações mínimas:

```text
Author.name não pode ser vazio
Category.name não pode ser vazio
Book.title não pode ser vazio
Book.isbn não pode ser vazio
Book.authorId não pode ser nulo
Book.categoryId não pode ser nulo
User.name não pode ser vazio
User.email deve ser válido
Loan.userId não pode ser nulo
Loan.bookId não pode ser nulo
```

---

# Arquitetura esperada

A aplicação deve ter separação clara de responsabilidades.

## Controller

Responsável por:

```text
Receber requisições HTTP
Validar entrada com @Valid
Chamar os services
Retornar ResponseEntity
```

Não deve conter regra de negócio.

---

## Service

Responsável por:

```text
Regras de negócio
Orquestração de operações
Uso de transações
Busca de entidades necessárias
Lançamento de exceções de domínio
```

---

## Repository

Responsável por:

```text
Acesso ao banco de dados
Consultas com Spring Data JPA
```

---

## Entity

Responsável por:

```text
Representar o modelo de domínio
Mapear tabelas com JPA
Conter comportamentos simples de domínio quando fizer sentido
```

Exemplo:

```java
book.borrow();
book.returnBook();
user.validateCanBorrow();
loan.returnLoan();
```

---

## DTO

Responsável por:

```text
Definir dados de entrada e saída da API
Evitar exposição direta das entidades
```

---

## Mapper

Responsável por:

```text
Converter Entity para DTO
Converter DTO para Entity quando fizer sentido
```

Pode ser implementado manualmente.

---

# Sugestão de estrutura de pacotes

```text
src/main/java/com.example.libraryapi
│
├── domain
│   ├── author
│   │   ├── Author.java
│   │   ├── AuthorRepository.java
│   │   ├── AuthorService.java
│   │   ├── AuthorMapper.java
│   │   └── dto
│   │       ├── AuthorRequestDTO.java
│   │       └── AuthorResponseDTO.java
│   │
│   ├── category
│   │   ├── Category.java
│   │   ├── CategoryRepository.java
│   │   ├── CategoryService.java
│   │   ├── CategoryMapper.java
│   │   └── dto
│   │       ├── CategoryRequestDTO.java
│   │       └── CategoryResponseDTO.java
│   │
│   ├── book
│   │   ├── Book.java
│   │   ├── BookRepository.java
│   │   ├── BookService.java
│   │   ├── BookMapper.java
│   │   ├── BookStatus.java
│   │   └── dto
│   │       ├── BookRequestDTO.java
│   │       └── BookResponseDTO.java
│   │
│   ├── user
│   │   ├── User.java
│   │   ├── UserRepository.java
│   │   ├── UserService.java
│   │   ├── UserMapper.java
│   │   ├── UserStatus.java
│   │   └── dto
│   │       ├── UserRequestDTO.java
│   │       └── UserResponseDTO.java
│   │
│   └── loan
│       ├── Loan.java
│       ├── LoanRepository.java
│       ├── LoanService.java
│       ├── LoanMapper.java
│       ├── LoanStatus.java
│       └── dto
│           ├── LoanRequestDTO.java
│           └── LoanResponseDTO.java
│
├── controller
│   ├── AuthorController.java
│   ├── CategoryController.java
│   ├── BookController.java
│   ├── UserController.java
│   └── LoanController.java
│
├── exception
│   ├── BusinessException.java
│   ├── ResourceNotFoundException.java
│   ├── GlobalExceptionHandler.java
│   └── ErrorResponseDTO.java
│
└── LibraryApiApplication.java
```

---

# Consultas esperadas nos repositories

## BookRepository

```java
List<Book> findByStatus(BookStatus status);
Optional<Book> findByIsbn(String isbn);
boolean existsByIsbn(String isbn);
```

## LoanRepository

```java
long countByUserIdAndStatus(Long userId, LoanStatus status);
List<Loan> findByUserId(Long userId);
List<Loan> findByStatus(LoanStatus status);
```

---

# Critérios de avaliação

O projeto será avaliado considerando:

```text
Organização do código
Separação correta de responsabilidades
Uso correto de Controllers, Services e Repositories
Modelagem das entidades
Uso de DTOs
Tratamento de exceções
Validações
Clareza dos endpoints REST
Boas práticas com HTTP status codes
Uso adequado de transações
Legibilidade do código
README claro com instruções de execução
```

---

# Diferenciais

Não são obrigatórios, mas contam pontos extras:

```text
Swagger/OpenAPI
Paginação em listagens
Filtros por status
Testes unitários
Testes de integração
Docker Compose com PostgreSQL
Autenticação JWT
Soft delete para livros
Logs estruturados
Padronização avançada de erros
```

---

# Entregáveis

O candidato deve entregar:

```text
Link do repositório GitHub
Código-fonte da aplicação
README com instruções de execução
Exemplos de requisições
Descrição das regras de negócio implementadas
```

O README deve conter:

```text
Tecnologias utilizadas
Como executar o projeto
Como acessar o banco H2, se usado
Como acessar Swagger, se usado
Lista de endpoints
Exemplos de payloads
```

---

# Ordem recomendada de implementação

Para não se perder, implemente nesta ordem:

```text
1. Configuração inicial do projeto
2. Author
3. Category
4. Book
5. User
6. Loan
7. Exceptions globais
8. Validações
9. Swagger
10. Testes
```

---

# Resultado esperado

Ao final, a aplicação deve permitir o fluxo completo:

```text
Cadastrar um autor
Cadastrar uma categoria
Cadastrar um livro vinculado ao autor e à categoria
Cadastrar um usuário
Criar um empréstimo para esse usuário
Alterar o status do livro para emprestado
Impedir novo empréstimo do mesmo livro
Registrar a devolução
Alterar o status do livro para disponível novamente
Consultar o histórico de empréstimos do usuário
```

Esse desafio cobre muito bem os fundamentos de Spring Boot e simula um contexto realista de desenvolvimento backend júnior.

# Project Manager

## Descrição geral da aplicação

O **Project Manager** é uma API desenvolvida em Spring Boot para o gerenciamento de projetos e membros. Ela permite criar, consultar, atualizar, deletar projetos, gerenciar membros, alocar membros em projetos, alterar status e cancelar projetos. A aplicação é voltada para facilitar o controle de times, alocação de pessoas e o acompanhamento do ciclo de vida dos projetos.

---

## Tecnologias utilizadas com descrição técnica

- **Java 17**: Linguagem principal da aplicação.
- **Spring Boot 3.5.3**: Framework para desenvolvimento rápido de aplicações Java.
- **Spring Data JPA**: Abstração para persistência de dados com bancos relacionais.
- **Spring Security**: Segurança e autenticação.
- **Spring Web e WebFlux**: Criação de APIs REST e suporte a programação reativa.
- **PostgreSQL**: Banco de dados relacional utilizado.
- **Redis**: Cache distribuído para otimização de performance.
- **Wiremock**: Mock de serviços externos para testes.
- **Lombok**: Redução de boilerplate no código Java.
- **ModelMapper**: Conversão entre entidades e DTOs.
- **Resilience4j**: Circuit breaker e resiliência.
- **Springdoc OpenAPI**: Documentação automática dos endpoints REST.
- **Docker Compose**: Orquestração de containers para Redis e Wiremock em ambiente local.
- **JUnit e Mockito**: Testes unitários e de integração.

---

## Diagrama de fluxos

```
Usuário (cliente HTTP)
        |
        v
[Controllers REST]
        |
        v
[Services] <----> [HttpClient para integrações externas]
        |
        v
[Repositories (JPA)]
        |
        v
[PostgreSQL / Redis]
```

- O usuário interage via endpoints REST.
- Controllers recebem as requisições e delegam para os Services.
- Services aplicam regras de negócio, interagem com repositórios e clientes HTTP.
- Repositórios acessam o banco de dados relacional.
- Redis é utilizado para cache.
- Wiremock pode simular integrações externas para testes.

---

## Como executar a aplicação

1. **Pré-requisitos**:
   - Java 17 instalado
   - Docker e Docker Compose instalados
   - Maven instalado

2. **Suba os serviços auxiliares (Redis e Wiremock) com Docker Compose**:

   ```sh
   cd projectManager
   docker compose up -d
   ```

3. **Execute a aplicação Spring Boot**:

   ```sh
   ./mvnw spring-boot:run
   ```

   Ou, se preferir, gere o JAR e execute:

   ```sh
   ./mvnw clean package
   java -jar target/projectManager-0.0.1-SNAPSHOT.jar
   ```

4. **Acesse a documentação OpenAPI/Swagger**:

   - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Listagem dos endpoints

### Membros

- `POST /api/v1/members`  
  Cria um novo membro.

- `GET /api/v1/members/{memberName}`  
  Consulta um membro pelo nome.

### Projetos

- `POST /api/v1/projects`  
  Cria um novo projeto.

- `GET /api/v1/projects`  
  Lista todos os projetos (com paginação).

- `GET /api/v1/projects/{projectId}`  
  Consulta um projeto pelo ID.

- `PUT /api/v1/projects/update/{projectId}`  
  Atualiza um projeto existente.

- `DELETE /api/v1/projects/{projectId}`  
  Deleta um projeto pelo ID.

- `PATCH /api/v1/projects/status-up/{projectId}`  
  Altera o status do projeto para o próximo estágio.

- `PATCH /api/v1/projects/project-cancel/{projectId}`  
  Cancela um projeto.

- `POST /api/v1/projects/project-members/{projectId}`  
  Adiciona membros a um projeto.

---

## Proprietário

- Felipe Fraga  
- Grupo: com.fraga  
- Projeto: projectManager 
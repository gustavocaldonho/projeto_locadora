# Locadora Passatempo

Sistema da Locadora de Vídeo Passatempo, desenvolvido na disciplina Desenvolvimento Web III (IFES).

| Pasta | O que é |
| --- | --- |
| `locadora-backend/` | API REST em Spring Boot 3.5, Java 21, Spring Data JPA e PostgreSQL |
| `locadora-frontend/` | Interface em Angular 20 com Angular Material |
| `docs/` | Coleção do Postman para testar a API |

## Release 1 (Sprints 1 e 2): Controle de Acervo

- **Sprint 1:** CRUD de Ator, Diretor e Classe.
- **Sprint 2:** CRUD de Título e Item, documentação da API com Swagger e tratamento personalizado de exceções.

Regras de integridade implementadas (do documento de análise):

- Ator e diretor não podem ser excluídos se estiverem ligados a algum título.
- Classe não pode ser excluída se existir título ligado a ela.
- Título não pode ser excluído se tiver itens.
- Título exige pelo menos um ator, um diretor e uma classe.
- O número de série do item não pode se repetir.

## Como rodar

### 1. Banco de dados

O backend usa o banco PostgreSQL **`locadora`** em `localhost:5432` (crie pelo pgAdmin se ainda não existir). A conexão fica em `locadora-backend/src/main/resources/application.properties`.

O usuário padrão é `postgres` e a senha padrão é `admin`. Para usar outros valores sem mudar o arquivo, defina as variáveis de ambiente `DB_USERNAME` e `DB_PASSWORD` (no STS4: `Run > Run Configurations... > LocadoraBackendApplication > aba Environment`).

As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`), sem apagar dados existentes. Na primeira execução, com o banco vazio, a classe `config/DadosIniciais` cadastra alguns atores, diretores, classes, títulos e itens de exemplo.

### 2. Backend

Pelo **Spring Tool Suite 4**: `File > Import > Maven > Existing Maven Projects`, escolha a pasta `locadora-backend` e rode `LocadoraBackendApplication` com `Run As > Spring Boot App`.

Ou pelo terminal, dentro de `locadora-backend` (no PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

Endereços:

- API: http://localhost:8080/locadora/resources/atores (também `diretores`, `classes`, `titulos`, `itens`)
- Swagger: http://localhost:8080/locadora/swagger-ui.html

Os testes automáticos (`mvnw test`) usam um banco H2 em memória e não mexem no PostgreSQL.

### 3. Frontend

Dentro de `locadora-frontend`:

```bash
npm install     # só na primeira vez
ng serve
```

Abra http://localhost:4200.

### 4. Postman

Importe `docs/locadora.postman_collection.json`. A variável `baseUrl` já aponta para `http://localhost:8080/locadora/resources`.

## Estrutura do backend

Pacote `br.edu.ifes.col.locadorabackend`:

- `domain` — entidades JPA (`Ator`, `Diretor`, `Classe`, `Titulo`, `Item`, enum `TipoItem`) e repositórios (`JpaRepository`)
- `services` — regras de negócio (validações de exclusão, número de série único, busca de atores/diretor/classe do título)
- `resources` — controllers REST, documentados com `@Tag`, `@Operation` e `@ApiResponse`
- `exceptions` — `ObjetoNaoEncontradoException` (404), `IntegridadeException` (400), `ErroPadrao` e o `ResourceExceptionHandler` (`@ControllerAdvice`)
- `config` — CORS para o Angular, informações do Swagger e dados iniciais de exemplo

Exemplo de erro devolvido pela API:

```json
{
  "timestamp": "2026-10-16T11:39:45.757",
  "status": 400,
  "error": "Violação de integridade",
  "message": "Não é possível excluir o ator, pois ele participa de algum título",
  "path": "/locadora/resources/atores/1"
}
```

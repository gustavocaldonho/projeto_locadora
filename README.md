# Locadora Passatempo

Sistema da Locadora de Vídeo Passatempo, desenvolvido na disciplina Desenvolvimento Web III (IFES).

| Pasta | O que é |
| --- | --- |
| `locadora-backend/` | API REST em Spring Boot 3.5, Java 21, Spring Data JPA e PostgreSQL |
| `locadora-frontend/` | Interface em Angular 20 com Angular Material |
| `docs/` | Coleção do Postman para testar a API |

## Release 2 (Sprint 3): Atendimento a Cliente

- **Cadastrar Cliente:** inscrever sócio (o número de inscrição é gerado), incluir dependente, alterar, desativar, reativar e excluir.
- **Efetuar Locação:** nova locação, alteração (valor, data prevista, pagamento) e cancelamento.
- **Efetuar Devolução:** pelo número de série do item, com cálculo de multa por atraso.

Regras implementadas (do documento de análise):

- Um sócio tem no máximo 3 dependentes ativos. Só sócio ativo pode receber dependente.
- Desativar um sócio desativa todos os dependentes; reativar o sócio reativa até 3 deles.
- Excluir um sócio exclui os dependentes. Não se exclui cliente (nem item) que tenha locações.
- Cliente inativo ou em débito (com locação em atraso) não pode locar; a mensagem lista as locações em atraso.
- Valor e data prevista vêm da classe do título; o funcionário pode alterar. A data prevista tem de ser maior que a data da locação.
- Um item não pode ter duas locações vigentes (a mensagem informa a data prevista de devolução).
- Só locação não paga pode ser cancelada.

Decisões tomadas onde o documento é omisso:

- **Multa:** cada dia de atraso custa o valor da classe do título (ex.: 3 dias de atraso na classe Catálogo, R$ 4,00, dão R$ 12,00 de multa). A regra fica em `LocacaoService.calcularMulta`.
- **Locação paga:** foi criado o campo `paga`. O funcionário marca se o cliente pagou na retirada; na devolução, a locação passa a ser paga.
- **Reservas:** não existem no diagrama de classes e ficaram de fora.

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

As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`), sem apagar dados existentes. Com o banco vazio, a classe `config/DadosIniciais` cadastra dados de exemplo: o acervo (se não houver nenhum ator) e os clientes com algumas locações (se não houver nenhum cliente). Entre eles, o sócio João da Silva tem uma locação em atraso, para testar o débito e a multa.

### 2. Backend

Pelo **Spring Tool Suite 4**: `File > Import > Maven > Existing Maven Projects`, escolha a pasta `locadora-backend` e rode `LocadoraBackendApplication` com `Run As > Spring Boot App`.

Ou pelo terminal, dentro de `locadora-backend` (no PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

Endereços:

- API: http://localhost:8080/locadora/resources/atores (também `diretores`, `classes`, `titulos`, `itens`, `socios`, `dependentes`, `clientes`, `locacoes`, `devolucoes`)
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

- `domain` — entidades JPA: acervo (`Ator`, `Diretor`, `Classe`, `Titulo`, `Item`, enum `TipoItem`) e atendimento (`Cliente`, abstrata, herdada por `Socio` e `Dependente` com herança JOINED; `Locacao`; enum `Sexo`)
- `repositories` — repositórios (DAO) que estendem `JpaRepository`
- `services` — regras de negócio (exclusões, número de série único, limite de dependentes, débito, multa)
- `dto` — objetos de entrada/saída que não são entidades: `LocacaoRequest` e `DevolucaoInfo`
- `resources` — controllers REST, documentados com `@Tag`, `@Operation` e `@ApiResponse`
- `exceptions` — `ObjetoNaoEncontradoException` (404), `IntegridadeException` (400), `RegraNegocioException` (400), `ErroPadrao` e o `ResourceExceptionHandler` (`@ControllerAdvice`)
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

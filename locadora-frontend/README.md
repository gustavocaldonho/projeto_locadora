# locadora-frontend

Front-end Angular 20 + Angular Material da Locadora Passatempo.

```bash
npm install     # só na primeira vez
ng serve        # abre em http://localhost:4200
```

O backend precisa estar rodando em `http://localhost:8080/locadora` (veja `src/app/core/api.ts`). Entre com `admin` / `admin123` (funcionário) ou `cliente` / `cliente123` (cliente).

## Estrutura

- `core/` — modelos (interfaces), services HTTP (`services.ts` do acervo e usuários, `atendimento-services.ts` do atendimento, `consulta-service.ts` da consulta), avisos (snackbar) e o login: `auth-service.ts` (sessão), `auth-interceptor.ts` (envia o cabeçalho `Authorization` e volta ao login no 401) e `auth-guards.ts` (bloqueia rotas por perfil)
- `shared/` — módulos do Material, diálogo de confirmação e as classes base de lista e formulário
- `home/` — página inicial
- `atores/`, `diretores/`, `classes/`, `titulos/`, `itens/` — acervo: um módulo por cadastro, carregado sob demanda (lazy loading), cada um com a tela de lista (Material Table) e a de formulário (Reactive Forms)
- `clientes/` — sócios (lista e formulário, com os dependentes na tela do sócio) e formulário de dependente
- `locacoes/` — lista de locações (vigentes ou todas), nova locação, alteração e cancelamento
- `devolucao/` — devolução pelo número de série, mostrando atraso, multa e total a pagar
- `login/` — tela de login
- `consulta/` — consulta de títulos por nome, categoria e ator, com as cópias disponíveis (funcionário e cliente)
- `usuarios/` — cadastro de usuários e perfis (só funcionário)

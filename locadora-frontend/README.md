# locadora-frontend

Front-end Angular 20 + Angular Material da Locadora Passatempo.

```bash
npm install     # só na primeira vez
ng serve        # abre em http://localhost:4200
```

O backend precisa estar rodando em `http://localhost:8080/locadora` (veja `src/app/core/api.ts`).

## Estrutura

- `core/` — modelos (interfaces), services HTTP e avisos (snackbar)
- `shared/` — módulos do Material, diálogo de confirmação e as classes base de lista e formulário
- `home/` — página inicial
- `atores/`, `diretores/`, `classes/`, `titulos/`, `itens/` — um módulo por cadastro, carregado sob demanda (lazy loading), cada um com a tela de lista (Material Table) e a de formulário (Reactive Forms)

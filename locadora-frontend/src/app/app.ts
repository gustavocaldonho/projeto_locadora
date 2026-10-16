import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from './core/auth-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.css',
})
export class App {
  /** Itens da barra de navegação. Cada um abre um módulo carregado sob demanda. */
  readonly acervo = [
    { nome: 'Atores', rota: '/atores', icone: 'person' },
    { nome: 'Diretores', rota: '/diretores', icone: 'videocam' },
    { nome: 'Classes', rota: '/classes', icone: 'sell' },
    { nome: 'Títulos', rota: '/titulos', icone: 'local_movies' },
    { nome: 'Itens', rota: '/itens', icone: 'album' },
  ];

  readonly atendimento = [
    { nome: 'Clientes', rota: '/clientes', icone: 'groups' },
    { nome: 'Locações', rota: '/locacoes', icone: 'shopping_bag' },
    { nome: 'Devolução', rota: '/devolucao', icone: 'assignment_return' },
  ];

  constructor(
    readonly auth: AuthService,
    private router: Router,
  ) {}

  sair(): void {
    this.auth.sair();
    this.router.navigate(['/login']);
  }
}

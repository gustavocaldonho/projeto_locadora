import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.css',
})
export class App {
  /** Itens da barra de navegação. Cada um abre um módulo carregado sob demanda. */
  readonly menu = [
    { nome: 'Atores', rota: '/atores', icone: 'person' },
    { nome: 'Diretores', rota: '/diretores', icone: 'videocam' },
    { nome: 'Classes', rota: '/classes', icone: 'sell' },
    { nome: 'Títulos', rota: '/titulos', icone: 'local_movies' },
    { nome: 'Itens', rota: '/itens', icone: 'album' },
  ];
}

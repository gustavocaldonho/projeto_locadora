import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  readonly cadastros = [
    { nome: 'Atores', rota: '/atores', icone: 'person', descricao: 'Atores que participam dos títulos.' },
    { nome: 'Diretores', rota: '/diretores', icone: 'videocam', descricao: 'Diretores dos títulos.' },
    { nome: 'Classes', rota: '/classes', icone: 'sell', descricao: 'Valor da locação e prazo de devolução.' },
    { nome: 'Títulos', rota: '/titulos', icone: 'local_movies', descricao: 'Filmes do acervo, com elenco, diretor e classe.' },
    { nome: 'Itens', rota: '/itens', icone: 'album', descricao: 'Cópias físicas: fitas, DVDs e Blu-rays.' },
  ];
}

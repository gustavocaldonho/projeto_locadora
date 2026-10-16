import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  readonly atendimento = [
    { nome: 'Clientes', rota: '/clientes', icone: 'groups', descricao: 'Sócios e seus dependentes (até 3 ativos).' },
    { nome: 'Locações', rota: '/locacoes', icone: 'shopping_bag', descricao: 'Locar itens, alterar e cancelar locações.' },
    { nome: 'Devolução', rota: '/devolucao', icone: 'assignment_return', descricao: 'Receber um item pelo número de série, com multa se houver atraso.' },
    { nome: 'Consultar títulos', rota: '/consulta', icone: 'search', descricao: 'Buscar por nome, categoria ou ator e ver as cópias disponíveis.' },
  ];

  readonly acervo = [
    { nome: 'Atores', rota: '/atores', icone: 'person', descricao: 'Atores que participam dos títulos.' },
    { nome: 'Diretores', rota: '/diretores', icone: 'videocam', descricao: 'Diretores dos títulos.' },
    { nome: 'Classes', rota: '/classes', icone: 'sell', descricao: 'Valor da locação e prazo de devolução.' },
    { nome: 'Títulos', rota: '/titulos', icone: 'local_movies', descricao: 'Filmes do acervo, com elenco, diretor e classe.' },
    { nome: 'Itens', rota: '/itens', icone: 'album', descricao: 'Cópias físicas: fitas, DVDs e Blu-rays.' },
  ];
}

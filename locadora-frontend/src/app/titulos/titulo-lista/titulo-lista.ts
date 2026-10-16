import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { MensagemService } from '../../core/mensagem-service';
import { Titulo } from '../../core/models';
import { TituloService } from '../../core/services';
import { ListaBase } from '../../shared/lista-base';

@Component({
  selector: 'app-titulo-lista',
  standalone: false,
  templateUrl: './titulo-lista.html',
})
export class TituloLista extends ListaBase<Titulo> {
  readonly colunas = ['nome', 'ano', 'categoria', 'diretor', 'atores', 'classe', 'acoes'];

  constructor(service: TituloService, mensagem: MensagemService, dialog: MatDialog) {
    super(service, mensagem, dialog);
    // Permite ordenar as colunas que guardam objetos (diretor e classe) pelo nome
    this.dados.sortingDataAccessor = (titulo, coluna) => {
      switch (coluna) {
        case 'diretor':
          return titulo.diretor.nome;
        case 'classe':
          return titulo.classe.nome;
        case 'atores':
          return this.nomesAtores(titulo);
        default:
          return (titulo as any)[coluna];
      }
    };
    // O filtro procura também no nome do diretor, da classe e dos atores
    this.dados.filterPredicate = (titulo, filtro) =>
      [titulo.nome, titulo.categoria, String(titulo.ano), titulo.diretor.nome, titulo.classe.nome, this.nomesAtores(titulo)]
        .join(' ')
        .toLowerCase()
        .includes(filtro);
  }

  descrever(x: Titulo): string {
    return `o título "${x.nome}"`;
  }

  nomesAtores(titulo: Titulo): string {
    return titulo.atores.map((a) => a.nome).join(', ');
  }
}

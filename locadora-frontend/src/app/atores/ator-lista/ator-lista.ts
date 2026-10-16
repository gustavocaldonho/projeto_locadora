import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { MensagemService } from '../../core/mensagem-service';
import { Ator } from '../../core/models';
import { AtorService } from '../../core/services';
import { ListaBase } from '../../shared/lista-base';

@Component({
  selector: 'app-ator-lista',
  standalone: false,
  templateUrl: './ator-lista.html',
})
export class AtorLista extends ListaBase<Ator> {
  readonly colunas = ['nome', 'acoes'];

  constructor(service: AtorService, mensagem: MensagemService, dialog: MatDialog) {
    super(service, mensagem, dialog);
  }

  descrever(x: Ator): string {
    return `o ator "${x.nome}"`;
  }
}

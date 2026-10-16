import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { MensagemService } from '../../core/mensagem-service';
import { Diretor } from '../../core/models';
import { DiretorService } from '../../core/services';
import { ListaBase } from '../../shared/lista-base';

@Component({
  selector: 'app-diretor-lista',
  standalone: false,
  templateUrl: './diretor-lista.html',
})
export class DiretorLista extends ListaBase<Diretor> {
  readonly colunas = ['nome', 'acoes'];

  constructor(service: DiretorService, mensagem: MensagemService, dialog: MatDialog) {
    super(service, mensagem, dialog);
  }

  descrever(x: Diretor): string {
    return `o diretor "${x.nome}"`;
  }
}

import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { MensagemService } from '../../core/mensagem-service';
import { Classe } from '../../core/models';
import { ClasseService } from '../../core/services';
import { ListaBase } from '../../shared/lista-base';

@Component({
  selector: 'app-classe-lista',
  standalone: false,
  templateUrl: './classe-lista.html',
})
export class ClasseLista extends ListaBase<Classe> {
  readonly colunas = ['nome', 'valor', 'prazoDevolucao', 'acoes'];

  constructor(service: ClasseService, mensagem: MensagemService, dialog: MatDialog) {
    super(service, mensagem, dialog);
  }

  descrever(x: Classe): string {
    return `a classe "${x.nome}"`;
  }
}

import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { MensagemService } from '../../core/mensagem-service';
import { Item, TIPOS_ITEM, TipoItem } from '../../core/models';
import { ItemService } from '../../core/services';
import { ListaBase } from '../../shared/lista-base';

@Component({
  selector: 'app-item-lista',
  standalone: false,
  templateUrl: './item-lista.html',
})
export class ItemLista extends ListaBase<Item> {
  readonly colunas = ['numSerie', 'titulo', 'tipoItem', 'dtAquisicao', 'acoes'];

  constructor(service: ItemService, mensagem: MensagemService, dialog: MatDialog) {
    super(service, mensagem, dialog);
    this.dados.sortingDataAccessor = (item, coluna) =>
      coluna === 'titulo' ? item.titulo.nome : (item as any)[coluna];
    this.dados.filterPredicate = (item, filtro) =>
      [item.numSerie, item.titulo.nome, this.descricaoTipo(item.tipoItem), item.dtAquisicao]
        .join(' ')
        .toLowerCase()
        .includes(filtro);
  }

  descrever(x: Item): string {
    return `o item "${x.numSerie}"`;
  }

  descricaoTipo(tipo: TipoItem): string {
    return TIPOS_ITEM.find((t) => t.valor === tipo)?.descricao ?? tipo;
  }
}

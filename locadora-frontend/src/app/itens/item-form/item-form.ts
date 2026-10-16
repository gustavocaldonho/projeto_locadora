import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MensagemService } from '../../core/mensagem-service';
import { Item, TIPOS_ITEM, TipoItem, Titulo } from '../../core/models';
import { ItemService, TituloService } from '../../core/services';
import { FormBase } from '../../shared/form-base';

@Component({
  selector: 'app-item-form',
  standalone: false,
  templateUrl: './item-form.html',
})
export class ItemForm extends FormBase<Item> {
  form;
  titulos: Titulo[] = [];
  readonly tipos = TIPOS_ITEM;
  /** Data de hoje no formato yyyy-MM-dd, usada como limite do campo de data. */
  readonly hoje = new Date().toLocaleDateString('sv-SE');

  constructor(
    fb: FormBuilder,
    service: ItemService,
    mensagem: MensagemService,
    route: ActivatedRoute,
    router: Router,
    tituloService: TituloService,
  ) {
    super(service, mensagem, route, router);
    this.form = fb.group({
      numSerie: ['', [Validators.required, Validators.maxLength(50)]],
      dtAquisicao: [this.hoje, Validators.required],
      tipoItem: [null as TipoItem | null, Validators.required],
      tituloId: [null as number | null, Validators.required],
    });
    tituloService.listar().subscribe((lista) => (this.titulos = lista));
  }

  protected override preencher(item: Item): void {
    this.form.patchValue({
      numSerie: item.numSerie,
      dtAquisicao: item.dtAquisicao,
      tipoItem: item.tipoItem,
      tituloId: item.titulo.id!,
    });
  }

  protected override montar(): Item {
    const v = this.form.getRawValue();
    return {
      id: this.id,
      numSerie: v.numSerie!,
      dtAquisicao: v.dtAquisicao!,
      tipoItem: v.tipoItem!,
      titulo: { id: v.tituloId! } as Titulo,
    };
  }
}

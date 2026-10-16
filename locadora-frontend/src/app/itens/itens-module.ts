import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared-module';
import { ItensRoutingModule } from './itens-routing-module';
import { ItemLista } from './item-lista/item-lista';
import { ItemForm } from './item-form/item-form';

@NgModule({
  declarations: [ItemLista, ItemForm],
  imports: [SharedModule, ItensRoutingModule],
})
export class ItensModule {}

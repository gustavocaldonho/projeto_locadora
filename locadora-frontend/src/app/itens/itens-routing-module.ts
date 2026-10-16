import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ItemLista } from './item-lista/item-lista';
import { ItemForm } from './item-form/item-form';

const routes: Routes = [
  { path: '', component: ItemLista },
  { path: 'novo', component: ItemForm },
  { path: ':id', component: ItemForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ItensRoutingModule {}

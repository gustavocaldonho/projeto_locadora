import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TituloLista } from './titulo-lista/titulo-lista';
import { TituloForm } from './titulo-form/titulo-form';

const routes: Routes = [
  { path: '', component: TituloLista },
  { path: 'novo', component: TituloForm },
  { path: ':id', component: TituloForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TitulosRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LocacaoForm } from './locacao-form/locacao-form';
import { LocacaoLista } from './locacao-lista/locacao-lista';

const routes: Routes = [
  { path: '', component: LocacaoLista },
  { path: 'nova', component: LocacaoForm },
  { path: ':id', component: LocacaoForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LocacoesRoutingModule {}

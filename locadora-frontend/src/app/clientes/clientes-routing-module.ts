import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DependenteForm } from './dependente-form/dependente-form';
import { SocioForm } from './socio-form/socio-form';
import { SocioLista } from './socio-lista/socio-lista';

const routes: Routes = [
  { path: '', component: SocioLista },
  { path: 'novo', component: SocioForm },
  { path: ':id', component: SocioForm },
  { path: ':socioId/dependentes/novo', component: DependenteForm },
  { path: ':socioId/dependentes/:id', component: DependenteForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ClientesRoutingModule {}

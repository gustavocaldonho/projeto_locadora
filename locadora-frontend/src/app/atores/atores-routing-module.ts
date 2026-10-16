import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AtorLista } from './ator-lista/ator-lista';
import { AtorForm } from './ator-form/ator-form';

const routes: Routes = [
  { path: '', component: AtorLista },
  { path: 'novo', component: AtorForm },
  { path: ':id', component: AtorForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AtoresRoutingModule {}

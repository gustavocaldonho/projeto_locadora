import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DiretorLista } from './diretor-lista/diretor-lista';
import { DiretorForm } from './diretor-form/diretor-form';

const routes: Routes = [
  { path: '', component: DiretorLista },
  { path: 'novo', component: DiretorForm },
  { path: ':id', component: DiretorForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DiretoresRoutingModule {}

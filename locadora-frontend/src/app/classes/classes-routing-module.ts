import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ClasseLista } from './classe-lista/classe-lista';
import { ClasseForm } from './classe-form/classe-form';

const routes: Routes = [
  { path: '', component: ClasseLista },
  { path: 'novo', component: ClasseForm },
  { path: ':id', component: ClasseForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ClassesRoutingModule {}

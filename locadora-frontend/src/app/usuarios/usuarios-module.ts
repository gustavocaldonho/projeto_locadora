import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SharedModule } from '../shared/shared-module';
import { UsuarioForm } from './usuario-form/usuario-form';
import { UsuarioLista } from './usuario-lista/usuario-lista';

const routes: Routes = [
  { path: '', component: UsuarioLista },
  { path: 'novo', component: UsuarioForm },
  { path: ':id', component: UsuarioForm },
];

@NgModule({
  declarations: [UsuarioLista, UsuarioForm],
  imports: [SharedModule, RouterModule.forChild(routes)],
})
export class UsuariosModule {}

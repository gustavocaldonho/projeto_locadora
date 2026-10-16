import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { Home } from './home/home';

/** Cada cadastro é um módulo carregado só quando o usuário entra nele (lazy loading). */
const routes: Routes = [
  { path: '', component: Home },
  { path: 'atores', loadChildren: () => import('./atores/atores-module').then((m) => m.AtoresModule) },
  { path: 'diretores', loadChildren: () => import('./diretores/diretores-module').then((m) => m.DiretoresModule) },
  { path: 'classes', loadChildren: () => import('./classes/classes-module').then((m) => m.ClassesModule) },
  { path: 'titulos', loadChildren: () => import('./titulos/titulos-module').then((m) => m.TitulosModule) },
  { path: 'itens', loadChildren: () => import('./itens/itens-module').then((m) => m.ItensModule) },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { funcionarioGuard, logadoGuard } from './core/auth-guards';
import { Home } from './home/home';

/**
 * Cada cadastro é um módulo carregado só quando o usuário entra nele (lazy loading).
 * Os guards controlam o acesso: a consulta é de qualquer usuário logado,
 * o resto é só de funcionário.
 */
const routes: Routes = [
  { path: 'login', loadChildren: () => import('./login/login-module').then((m) => m.LoginModule) },
  { path: 'consulta', loadChildren: () => import('./consulta/consulta-module').then((m) => m.ConsultaModule), canActivate: [logadoGuard] },
  { path: '', component: Home, canActivate: [funcionarioGuard] },
  { path: 'atores', loadChildren: () => import('./atores/atores-module').then((m) => m.AtoresModule), canActivate: [funcionarioGuard] },
  { path: 'diretores', loadChildren: () => import('./diretores/diretores-module').then((m) => m.DiretoresModule), canActivate: [funcionarioGuard] },
  { path: 'classes', loadChildren: () => import('./classes/classes-module').then((m) => m.ClassesModule), canActivate: [funcionarioGuard] },
  { path: 'titulos', loadChildren: () => import('./titulos/titulos-module').then((m) => m.TitulosModule), canActivate: [funcionarioGuard] },
  { path: 'itens', loadChildren: () => import('./itens/itens-module').then((m) => m.ItensModule), canActivate: [funcionarioGuard] },
  { path: 'clientes', loadChildren: () => import('./clientes/clientes-module').then((m) => m.ClientesModule), canActivate: [funcionarioGuard] },
  { path: 'locacoes', loadChildren: () => import('./locacoes/locacoes-module').then((m) => m.LocacoesModule), canActivate: [funcionarioGuard] },
  { path: 'devolucao', loadChildren: () => import('./devolucao/devolucao-module').then((m) => m.DevolucaoModule), canActivate: [funcionarioGuard] },
  { path: 'usuarios', loadChildren: () => import('./usuarios/usuarios-module').then((m) => m.UsuariosModule), canActivate: [funcionarioGuard] },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

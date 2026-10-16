import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../shared/shared-module';
import { LoginPagina } from './login-pagina';

@NgModule({
  declarations: [LoginPagina],
  imports: [SharedModule, RouterModule.forChild([{ path: '', component: LoginPagina }])],
})
export class LoginModule {}

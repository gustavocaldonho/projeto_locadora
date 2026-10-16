import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../shared/shared-module';
import { ConsultaPagina } from './consulta-pagina';

@NgModule({
  declarations: [ConsultaPagina],
  imports: [SharedModule, RouterModule.forChild([{ path: '', component: ConsultaPagina }])],
})
export class ConsultaModule {}

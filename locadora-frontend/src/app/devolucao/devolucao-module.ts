import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../shared/shared-module';
import { DevolucaoPagina } from './devolucao-pagina';

@NgModule({
  declarations: [DevolucaoPagina],
  imports: [SharedModule, RouterModule.forChild([{ path: '', component: DevolucaoPagina }])],
})
export class DevolucaoModule {}

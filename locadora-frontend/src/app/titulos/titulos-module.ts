import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared-module';
import { TitulosRoutingModule } from './titulos-routing-module';
import { TituloLista } from './titulo-lista/titulo-lista';
import { TituloForm } from './titulo-form/titulo-form';

@NgModule({
  declarations: [TituloLista, TituloForm],
  imports: [SharedModule, TitulosRoutingModule],
})
export class TitulosModule {}

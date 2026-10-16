import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared-module';
import { ClientesRoutingModule } from './clientes-routing-module';
import { DependenteForm } from './dependente-form/dependente-form';
import { SocioForm } from './socio-form/socio-form';
import { SocioLista } from './socio-lista/socio-lista';

@NgModule({
  declarations: [SocioLista, SocioForm, DependenteForm],
  imports: [SharedModule, ClientesRoutingModule],
})
export class ClientesModule {}

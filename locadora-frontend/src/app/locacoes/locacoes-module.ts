import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared-module';
import { LocacaoForm } from './locacao-form/locacao-form';
import { LocacaoLista } from './locacao-lista/locacao-lista';
import { LocacoesRoutingModule } from './locacoes-routing-module';

@NgModule({
  declarations: [LocacaoLista, LocacaoForm],
  imports: [SharedModule, LocacoesRoutingModule],
})
export class LocacoesModule {}

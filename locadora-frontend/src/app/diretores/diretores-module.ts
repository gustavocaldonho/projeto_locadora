import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared-module';
import { DiretoresRoutingModule } from './diretores-routing-module';
import { DiretorLista } from './diretor-lista/diretor-lista';
import { DiretorForm } from './diretor-form/diretor-form';

@NgModule({
  declarations: [DiretorLista, DiretorForm],
  imports: [SharedModule, DiretoresRoutingModule],
})
export class DiretoresModule {}

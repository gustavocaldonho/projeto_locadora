import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared-module';
import { AtoresRoutingModule } from './atores-routing-module';
import { AtorLista } from './ator-lista/ator-lista';
import { AtorForm } from './ator-form/ator-form';

@NgModule({
  declarations: [AtorLista, AtorForm],
  imports: [SharedModule, AtoresRoutingModule],
})
export class AtoresModule {}

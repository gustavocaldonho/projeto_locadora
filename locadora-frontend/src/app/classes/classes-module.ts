import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared-module';
import { ClassesRoutingModule } from './classes-routing-module';
import { ClasseLista } from './classe-lista/classe-lista';
import { ClasseForm } from './classe-form/classe-form';

@NgModule({
  declarations: [ClasseLista, ClasseForm],
  imports: [SharedModule, ClassesRoutingModule],
})
export class ClassesModule {}

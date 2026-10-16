import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MensagemService } from '../../core/mensagem-service';
import { Classe } from '../../core/models';
import { ClasseService } from '../../core/services';
import { FormBase } from '../../shared/form-base';

@Component({
  selector: 'app-classe-form',
  standalone: false,
  templateUrl: './classe-form.html',
})
export class ClasseForm extends FormBase<Classe> {
  form;

  constructor(fb: FormBuilder, service: ClasseService, mensagem: MensagemService, route: ActivatedRoute, router: Router) {
    super(service, mensagem, route, router);
    this.form = fb.group({
      nome: ['', [Validators.required, Validators.maxLength(50)]],
      valor: [null as number | null, [Validators.required, Validators.min(0)]],
      prazoDevolucao: [null as number | null, [Validators.required, Validators.min(1)]],
    });
  }
}

import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MensagemService } from '../../core/mensagem-service';
import { Diretor } from '../../core/models';
import { DiretorService } from '../../core/services';
import { FormBase } from '../../shared/form-base';

@Component({
  selector: 'app-diretor-form',
  standalone: false,
  templateUrl: './diretor-form.html',
})
export class DiretorForm extends FormBase<Diretor> {
  form;

  constructor(fb: FormBuilder, service: DiretorService, mensagem: MensagemService, route: ActivatedRoute, router: Router) {
    super(service, mensagem, route, router);
    this.form = fb.nonNullable.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
    });
  }
}

import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MensagemService } from '../../core/mensagem-service';
import { Ator } from '../../core/models';
import { AtorService } from '../../core/services';
import { FormBase } from '../../shared/form-base';

@Component({
  selector: 'app-ator-form',
  standalone: false,
  templateUrl: './ator-form.html',
})
export class AtorForm extends FormBase<Ator> {
  form;

  constructor(fb: FormBuilder, service: AtorService, mensagem: MensagemService, route: ActivatedRoute, router: Router) {
    super(service, mensagem, route, router);
    this.form = fb.nonNullable.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
    });
  }
}

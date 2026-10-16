import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MensagemService } from '../../core/mensagem-service';
import { PERFIS, Perfil, Usuario } from '../../core/models';
import { UsuarioService } from '../../core/services';
import { FormBase } from '../../shared/form-base';

@Component({
  selector: 'app-usuario-form',
  standalone: false,
  templateUrl: './usuario-form.html',
})
export class UsuarioForm extends FormBase<Usuario> implements OnInit {
  readonly perfis = PERFIS;
  form;

  constructor(fb: FormBuilder, service: UsuarioService, mensagem: MensagemService, route: ActivatedRoute, router: Router) {
    super(service, mensagem, route, router);
    this.form = fb.group({
      login: ['', [Validators.required, Validators.maxLength(50)]],
      nome: ['', [Validators.required, Validators.maxLength(100)]],
      perfil: ['FUNCIONARIO' as Perfil, Validators.required],
      senha: ['', Validators.minLength(6)],
    });
  }

  override ngOnInit(): void {
    super.ngOnInit();
    // Na inclusão a senha é obrigatória; na alteração, em branco mantém a atual
    if (!this.editando) {
      this.form.controls.senha.addValidators(Validators.required);
    }
  }
}

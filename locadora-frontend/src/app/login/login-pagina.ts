import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../core/auth-service';

@Component({
  selector: 'app-login-pagina',
  standalone: false,
  templateUrl: './login-pagina.html',
  styleUrl: './login-pagina.css',
})
export class LoginPagina {
  form;
  erro = '';
  entrando = false;
  mostrarSenha = false;

  constructor(
    fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
  ) {
    this.form = fb.nonNullable.group({
      login: ['', Validators.required],
      senha: ['', Validators.required],
    });
  }

  entrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.erro = '';
    this.entrando = true;
    const { login, senha } = this.form.getRawValue();
    this.auth.entrar(login, senha).subscribe({
      next: (usuario) => {
        // Funcionário vai para a página inicial; cliente vai direto para a consulta
        this.router.navigate([usuario.perfil === 'FUNCIONARIO' ? '/' : '/consulta']);
      },
      error: (resposta: HttpErrorResponse) => {
        this.entrando = false;
        this.erro =
          resposta.status === 401
            ? 'Usuário ou senha inválidos.'
            : resposta.status === 0
              ? 'Não foi possível conectar ao servidor. O backend está rodando?'
              : 'Não foi possível entrar. Tente novamente.';
      },
    });
  }
}

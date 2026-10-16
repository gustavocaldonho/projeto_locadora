import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthService } from './auth-service';

/**
 * Coloca o cabeçalho Authorization em todas as chamadas à API.
 * Se a API responder 401 (sessão inválida), volta para a tela de login.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const cabecalho = auth.cabecalhoAutorizacao();

  // A tela de login já manda o próprio cabeçalho
  const requisicao =
    cabecalho && !req.headers.has('Authorization') ? req.clone({ setHeaders: { Authorization: cabecalho } }) : req;

  return next(requisicao).pipe(
    catchError((erro: HttpErrorResponse) => {
      if (erro.status === 401 && !req.url.endsWith('/auth/usuario')) {
        auth.sair();
        router.navigate(['/login']);
      }
      return throwError(() => erro);
    }),
  );
};

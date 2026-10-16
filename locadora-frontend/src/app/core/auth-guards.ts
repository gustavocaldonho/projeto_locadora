import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from './auth-service';

/** Libera a rota para qualquer usuário logado (funcionário ou cliente). */
export const logadoGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  return auth.logado() ? true : inject(Router).createUrlTree(['/login']);
};

/** Libera a rota só para funcionário. O cliente é mandado para a Consulta de Títulos. */
export const funcionarioGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (!auth.logado()) {
    return router.createUrlTree(['/login']);
  }
  return auth.ehFuncionario() ? true : router.createUrlTree(['/consulta']);
};

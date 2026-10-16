import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

import { ErroPadrao } from './models';

/** Mostra avisos de sucesso e de erro no rodapé da tela. */
@Injectable({ providedIn: 'root' })
export class MensagemService {
  constructor(private snackBar: MatSnackBar) {}

  sucesso(texto: string): void {
    this.snackBar.open(texto, 'OK', { duration: 3000 });
  }

  /** Usa a mensagem do ErroPadrao do backend quando ela existir. */
  erro(resposta: HttpErrorResponse): void {
    const erro = resposta.error as ErroPadrao | null;
    let texto = erro?.message;
    if (!texto) {
      texto =
        resposta.status === 0
          ? 'Não foi possível conectar ao servidor. O backend está rodando?'
          : 'Ocorreu um erro inesperado.';
    }
    this.snackBar.open(texto, 'Fechar', { duration: 6000, panelClass: 'snack-erro' });
  }
}

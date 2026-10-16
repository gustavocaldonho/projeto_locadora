import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

/** Diálogo "Tem certeza?" usado antes de excluir um registro. */
@Component({
  selector: 'app-confirmacao-dialog',
  standalone: false,
  template: `
    <h2 mat-dialog-title>Confirmar exclusão</h2>
    <mat-dialog-content>{{ mensagem }}</mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button [mat-dialog-close]="false">Cancelar</button>
      <button mat-flat-button color="warn" [mat-dialog-close]="true">Excluir</button>
    </mat-dialog-actions>
  `,
})
export class ConfirmacaoDialog {
  constructor(@Inject(MAT_DIALOG_DATA) public mensagem: string) {}
}

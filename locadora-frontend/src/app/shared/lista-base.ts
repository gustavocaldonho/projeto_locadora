import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Directive, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

import { CrudService } from '../core/crud-service';
import { MensagemService } from '../core/mensagem-service';
import { ConfirmacaoDialog } from './confirmacao-dialog/confirmacao-dialog';

/**
 * Comportamento comum das telas de listagem: carregar a tabela,
 * filtrar, ordenar e excluir com confirmação.
 */
@Directive()
export abstract class ListaBase<T extends { id?: number }> implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort!: MatSort;

  dados = new MatTableDataSource<T>([]);
  carregando = false;

  constructor(
    protected service: CrudService<T>,
    protected mensagem: MensagemService,
    protected dialog: MatDialog,
  ) {}

  /** Texto mostrado no diálogo de exclusão, ex.: 'o ator "Keanu Reeves"'. */
  abstract descrever(objeto: T): string;

  ngOnInit(): void {
    this.carregar();
  }

  ngAfterViewInit(): void {
    this.dados.sort = this.sort;
  }

  carregar(): void {
    this.carregando = true;
    this.service.listar().subscribe({
      next: (lista) => {
        this.dados.data = lista;
        this.carregando = false;
      },
      error: (erro: HttpErrorResponse) => {
        this.carregando = false;
        this.mensagem.erro(erro);
      },
    });
  }

  filtrar(evento: Event): void {
    this.dados.filter = (evento.target as HTMLInputElement).value.trim().toLowerCase();
  }

  excluir(objeto: T): void {
    this.dialog
      .open(ConfirmacaoDialog, { data: `Deseja excluir ${this.descrever(objeto)}?` })
      .afterClosed()
      .subscribe((confirmou) => {
        if (!confirmou) {
          return;
        }
        this.service.excluir(objeto.id!).subscribe({
          next: () => {
            this.mensagem.sucesso('Excluído com sucesso.');
            this.carregar();
          },
          error: (erro: HttpErrorResponse) => this.mensagem.erro(erro),
        });
      });
  }
}

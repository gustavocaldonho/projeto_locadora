import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

import { SocioService } from '../../core/atendimento-services';
import { MensagemService } from '../../core/mensagem-service';
import { Socio } from '../../core/models';
import { ConfirmacaoDialog } from '../../shared/confirmacao-dialog/confirmacao-dialog';

@Component({
  selector: 'app-socio-lista',
  standalone: false,
  templateUrl: './socio-lista.html',
})
export class SocioLista implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort!: MatSort;

  readonly colunas = ['numInscricao', 'nome', 'cpf', 'tel', 'estahAtivo', 'acoes'];
  dados = new MatTableDataSource<Socio>([]);
  carregando = false;

  constructor(
    private service: SocioService,
    private mensagem: MensagemService,
    private dialog: MatDialog,
  ) {}

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

  /** Desativa (com os dependentes) ou reativa o sócio. */
  alternarSituacao(socio: Socio): void {
    const acao = socio.estahAtivo
      ? this.service.desativar(socio.numInscricao!)
      : this.service.reativar(socio.numInscricao!);
    acao.subscribe({
      next: () => {
        this.mensagem.sucesso(socio.estahAtivo ? 'Sócio e dependentes desativados.' : 'Sócio reativado.');
        this.carregar();
      },
      error: (erro: HttpErrorResponse) => this.mensagem.erro(erro),
    });
  }

  excluir(socio: Socio): void {
    this.dialog
      .open(ConfirmacaoDialog, {
        data: `Deseja excluir o sócio "${socio.nome}"? Os dependentes dele também serão excluídos.`,
      })
      .afterClosed()
      .subscribe((confirmou) => {
        if (!confirmou) {
          return;
        }
        this.service.excluir(socio.numInscricao!).subscribe({
          next: () => {
            this.mensagem.sucesso('Sócio excluído.');
            this.carregar();
          },
          error: (erro: HttpErrorResponse) => this.mensagem.erro(erro),
        });
      });
  }
}

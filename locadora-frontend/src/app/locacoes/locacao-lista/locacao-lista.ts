import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

import { LocacaoService } from '../../core/atendimento-services';
import { MensagemService } from '../../core/mensagem-service';
import { Locacao, SituacaoLocacao } from '../../core/models';
import { ConfirmacaoDialog } from '../../shared/confirmacao-dialog/confirmacao-dialog';

@Component({
  selector: 'app-locacao-lista',
  standalone: false,
  templateUrl: './locacao-lista.html',
})
export class LocacaoLista implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort!: MatSort;

  readonly colunas = ['item', 'cliente', 'dtLocacao', 'dtDevolucaoPrevista', 'dtDevolucaoEfetiva', 'valorCobrado', 'multaCobrada', 'situacao', 'acoes'];
  dados = new MatTableDataSource<Locacao>([]);
  somenteVigentes = true;
  carregando = false;

  constructor(
    private service: LocacaoService,
    private mensagem: MensagemService,
    private dialog: MatDialog,
  ) {
    this.dados.sortingDataAccessor = (l, coluna) => {
      switch (coluna) {
        case 'item':
          return l.item.numSerie;
        case 'cliente':
          return l.cliente.nome;
        default:
          return (l as any)[coluna] ?? '';
      }
    };
    this.dados.filterPredicate = (l, filtro) =>
      [l.item.numSerie, l.item.titulo.nome, l.cliente.nome, String(l.cliente.numInscricao), this.descricaoSituacao(l.situacao)]
        .join(' ')
        .toLowerCase()
        .includes(filtro);
  }

  ngOnInit(): void {
    this.carregar();
  }

  ngAfterViewInit(): void {
    this.dados.sort = this.sort;
  }

  carregar(): void {
    this.carregando = true;
    this.service.listar(this.somenteVigentes).subscribe({
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

  descricaoSituacao(situacao: SituacaoLocacao): string {
    return { VIGENTE: 'Vigente', ATRASADA: 'Atrasada', DEVOLVIDA: 'Devolvida' }[situacao];
  }

  cancelar(locacao: Locacao): void {
    this.dialog
      .open(ConfirmacaoDialog, {
        data: `Deseja cancelar a locação do item ${locacao.item.numSerie} (${locacao.item.titulo.nome}) para ${locacao.cliente.nome}? Ela será excluída.`,
      })
      .afterClosed()
      .subscribe((confirmou) => {
        if (!confirmou) {
          return;
        }
        this.service.cancelar(locacao.id).subscribe({
          next: () => {
            this.mensagem.sucesso('Locação cancelada.');
            this.carregar();
          },
          error: (erro: HttpErrorResponse) => this.mensagem.erro(erro),
        });
      });
  }
}

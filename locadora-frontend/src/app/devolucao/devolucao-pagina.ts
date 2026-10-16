import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { DevolucaoService } from '../core/atendimento-services';
import { MensagemService } from '../core/mensagem-service';
import { DevolucaoInfo } from '../core/models';

/**
 * Efetuar Devolução: o funcionário informa o número de série, confere o valor
 * (locação + multa, se houver atraso) e confirma.
 */
@Component({
  selector: 'app-devolucao-pagina',
  standalone: false,
  templateUrl: './devolucao-pagina.html',
})
export class DevolucaoPagina implements OnInit {
  numSerie = new FormControl('', { nonNullable: true, validators: Validators.required });
  info?: DevolucaoInfo;
  /** Preenchido depois que a devolução é registrada. */
  registrada?: DevolucaoInfo;
  processando = false;

  constructor(
    private service: DevolucaoService,
    private mensagem: MensagemService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    // Vindo da lista de locações, o número de série chega pela URL
    const numSerie = this.route.snapshot.queryParamMap.get('numSerie');
    if (numSerie) {
      this.numSerie.setValue(numSerie);
      this.buscar();
    }
  }

  buscar(): void {
    if (this.numSerie.invalid) {
      this.numSerie.markAsTouched();
      return;
    }
    this.info = undefined;
    this.registrada = undefined;
    this.processando = true;
    this.service.consultar(this.numSerie.value.trim()).subscribe({
      next: (info) => {
        this.info = info;
        this.processando = false;
      },
      error: (erro: HttpErrorResponse) => {
        this.processando = false;
        this.mensagem.erro(erro);
      },
    });
  }

  confirmar(): void {
    this.processando = true;
    this.service.devolver(this.info!.locacao.item.numSerie).subscribe({
      next: (registrada) => {
        this.registrada = registrada;
        this.info = undefined;
        this.processando = false;
        this.numSerie.reset();
        this.mensagem.sucesso('Devolução registrada.');
      },
      error: (erro: HttpErrorResponse) => {
        this.processando = false;
        this.mensagem.erro(erro);
      },
    });
  }
}

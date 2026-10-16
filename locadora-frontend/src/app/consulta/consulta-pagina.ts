import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { debounceTime } from 'rxjs';

import { ConsultaService } from '../core/consulta-service';
import { MensagemService } from '../core/mensagem-service';
import { Ator, TituloConsulta } from '../core/models';

/**
 * Consultar Título por nome, categoria e ator. Os filtros podem ser combinados
 * e a lista é atualizada enquanto o usuário digita ou escolhe.
 */
@Component({
  selector: 'app-consulta-pagina',
  standalone: false,
  templateUrl: './consulta-pagina.html',
  styleUrl: './consulta-pagina.css',
})
export class ConsultaPagina implements OnInit {
  form;
  categorias: string[] = [];
  atores: Ator[] = [];
  titulos: TituloConsulta[] = [];
  carregando = false;

  constructor(
    fb: FormBuilder,
    private service: ConsultaService,
    private mensagem: MensagemService,
  ) {
    this.form = fb.group({
      nome: [''],
      categoria: [null as string | null],
      atorId: [null as number | null],
    });
  }

  ngOnInit(): void {
    this.service.categorias().subscribe((lista) => (this.categorias = lista));
    this.service.atores().subscribe((lista) => (this.atores = lista));
    this.form.valueChanges.pipe(debounceTime(300)).subscribe(() => this.consultar());
    this.consultar();
  }

  consultar(): void {
    this.carregando = true;
    this.service.consultar(this.form.getRawValue()).subscribe({
      next: (lista) => {
        this.titulos = lista;
        this.carregando = false;
      },
      error: (erro: HttpErrorResponse) => {
        this.carregando = false;
        this.mensagem.erro(erro);
      },
    });
  }

  get temFiltro(): boolean {
    const v = this.form.getRawValue();
    return !!(v.nome || v.categoria || v.atorId);
  }

  limpar(): void {
    this.form.reset({ nome: '', categoria: null, atorId: null });
  }

  /** Clicar numa categoria ou ator do cartão aplica esse filtro. */
  filtrarPorCategoria(categoria: string): void {
    this.form.patchValue({ categoria });
  }

  filtrarPorAtor(nome: string): void {
    const ator = this.atores.find((a) => a.nome === nome);
    if (ator) {
      this.form.patchValue({ atorId: ator.id! });
    }
  }
}

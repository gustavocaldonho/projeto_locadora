import { HttpErrorResponse } from '@angular/common/http';
import { Directive, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { CrudService } from '../core/crud-service';
import { MensagemService } from '../core/mensagem-service';

/**
 * Comportamento comum das telas de inclusão/alteração.
 * A rota "novo" abre o formulário vazio; a rota ":id" carrega o registro para editar.
 */
@Directive()
export abstract class FormBase<T extends { id?: number }> implements OnInit {
  abstract form: FormGroup;
  id?: number;
  salvando = false;

  constructor(
    protected service: CrudService<T>,
    protected mensagem: MensagemService,
    protected route: ActivatedRoute,
    protected router: Router,
  ) {}

  /** Coloca no formulário os dados vindos do backend. */
  protected preencher(objeto: T): void {
    this.form.patchValue(objeto as any);
  }

  /** Monta o objeto que será enviado ao backend a partir do formulário. */
  protected montar(): T {
    return { ...this.form.value, id: this.id } as T;
  }

  get editando(): boolean {
    return this.id !== undefined;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.id = Number(id);
      this.service.buscar(this.id).subscribe({
        next: (objeto) => this.preencher(objeto),
        error: (erro: HttpErrorResponse) => {
          this.mensagem.erro(erro);
          this.voltar();
        },
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.salvando = true;
    this.service.salvar(this.montar()).subscribe({
      next: () => {
        this.mensagem.sucesso(this.editando ? 'Alterado com sucesso.' : 'Incluído com sucesso.');
        this.voltar();
      },
      error: (erro: HttpErrorResponse) => {
        this.salvando = false;
        this.mensagem.erro(erro);
      },
    });
  }

  voltar(): void {
    this.router.navigate(['..'], { relativeTo: this.route });
  }
}

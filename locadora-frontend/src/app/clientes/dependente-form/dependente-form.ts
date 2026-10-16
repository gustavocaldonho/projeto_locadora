import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { DependenteService, SocioService } from '../../core/atendimento-services';
import { MensagemService } from '../../core/mensagem-service';
import { Dependente, SEXOS, Sexo, Socio } from '../../core/models';

@Component({
  selector: 'app-dependente-form',
  standalone: false,
  templateUrl: './dependente-form.html',
})
export class DependenteForm implements OnInit {
  readonly sexos = SEXOS;
  readonly hoje = new Date().toLocaleDateString('sv-SE');

  socioId!: number;
  socio?: Socio;
  id?: number;
  salvando = false;
  form;

  constructor(
    fb: FormBuilder,
    private service: DependenteService,
    private socioService: SocioService,
    private mensagem: MensagemService,
    private route: ActivatedRoute,
    private router: Router,
  ) {
    this.form = fb.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
      dtNascimento: ['', Validators.required],
      sexo: [null as Sexo | null, Validators.required],
    });
  }

  get editando(): boolean {
    return this.id !== undefined;
  }

  ngOnInit(): void {
    this.socioId = Number(this.route.snapshot.paramMap.get('socioId'));
    this.socioService.buscar(this.socioId).subscribe((socio) => (this.socio = socio));
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.id = Number(id);
      this.service.buscar(this.id).subscribe({
        next: (dependente) => this.form.patchValue(dependente),
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
    const dependente = {
      ...this.form.getRawValue(),
      numInscricao: this.id,
      socio: { numInscricao: this.socioId },
    } as Dependente;
    this.service.salvar(dependente).subscribe({
      next: (salvo) => {
        this.mensagem.sucesso(
          this.editando ? 'Dependente alterado.' : `Dependente incluído com o número ${salvo.numInscricao}.`,
        );
        this.voltar();
      },
      error: (erro: HttpErrorResponse) => {
        this.salvando = false;
        this.mensagem.erro(erro);
      },
    });
  }

  voltar(): void {
    this.router.navigate(['/clientes', this.socioId]);
  }
}

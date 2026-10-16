import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';

import { DependenteService, SocioService } from '../../core/atendimento-services';
import { MensagemService } from '../../core/mensagem-service';
import { Dependente, SEXOS, Sexo, Socio } from '../../core/models';
import { ConfirmacaoDialog } from '../../shared/confirmacao-dialog/confirmacao-dialog';

/** Inscrição e alteração de sócio. Na alteração, mostra também os dependentes. */
@Component({
  selector: 'app-socio-form',
  standalone: false,
  templateUrl: './socio-form.html',
})
export class SocioForm implements OnInit {
  readonly sexos = SEXOS;
  readonly hoje = new Date().toLocaleDateString('sv-SE');
  readonly colunasDependentes = ['numInscricao', 'nome', 'dtNascimento', 'sexo', 'estahAtivo', 'acoes'];

  id?: number;
  socio?: Socio;
  dependentes: Dependente[] = [];
  salvando = false;

  form;

  constructor(
    fb: FormBuilder,
    private service: SocioService,
    private dependenteService: DependenteService,
    private mensagem: MensagemService,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private router: Router,
  ) {
    this.form = fb.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
      dtNascimento: ['', Validators.required],
      sexo: [null as Sexo | null, Validators.required],
      cpf: ['', [Validators.required, Validators.pattern(/^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/)]],
      endereco: ['', [Validators.required, Validators.maxLength(200)]],
      tel: ['', [Validators.required, Validators.maxLength(20)]],
    });
  }

  get editando(): boolean {
    return this.id !== undefined;
  }

  get dependentesAtivos(): number {
    return this.dependentes.filter((d) => d.estahAtivo).length;
  }

  /** Só pode incluir dependente se o sócio estiver ativo e tiver menos de 3 dependentes ativos. */
  get podeIncluirDependente(): boolean {
    return !!this.socio?.estahAtivo && this.dependentesAtivos < 3;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.id = Number(id);
      this.carregar();
    }
  }

  carregar(): void {
    this.service.buscar(this.id!).subscribe({
      next: (socio) => {
        this.socio = socio;
        this.form.patchValue(socio);
      },
      error: (erro: HttpErrorResponse) => {
        this.mensagem.erro(erro);
        this.voltar();
      },
    });
    this.service.dependentes(this.id!).subscribe((lista) => (this.dependentes = lista));
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.salvando = true;
    const socio = { ...this.form.getRawValue(), numInscricao: this.id } as Socio;
    this.service.salvar(socio).subscribe({
      next: (salvo) => {
        this.salvando = false;
        if (this.editando) {
          this.mensagem.sucesso('Dados do sócio alterados.');
          this.voltar();
        } else {
          // Depois de inscrever, abre a tela do sócio para incluir dependentes
          this.mensagem.sucesso(`Sócio inscrito com o número ${salvo.numInscricao}.`);
          this.router.navigate(['..', salvo.numInscricao], { relativeTo: this.route });
        }
      },
      error: (erro: HttpErrorResponse) => {
        this.salvando = false;
        this.mensagem.erro(erro);
      },
    });
  }

  alternarDependente(dependente: Dependente): void {
    const acao = dependente.estahAtivo
      ? this.dependenteService.desativar(dependente.numInscricao!)
      : this.dependenteService.reativar(dependente.numInscricao!);
    acao.subscribe({
      next: () => {
        this.mensagem.sucesso(dependente.estahAtivo ? 'Dependente desativado.' : 'Dependente reativado.');
        this.carregar();
      },
      error: (erro: HttpErrorResponse) => this.mensagem.erro(erro),
    });
  }

  excluirDependente(dependente: Dependente): void {
    this.dialog
      .open(ConfirmacaoDialog, { data: `Deseja excluir o dependente "${dependente.nome}"?` })
      .afterClosed()
      .subscribe((confirmou) => {
        if (!confirmou) {
          return;
        }
        this.dependenteService.excluir(dependente.numInscricao!).subscribe({
          next: () => {
            this.mensagem.sucesso('Dependente excluído.');
            this.carregar();
          },
          error: (erro: HttpErrorResponse) => this.mensagem.erro(erro),
        });
      });
  }

  descricaoSexo(sexo: Sexo): string {
    return this.sexos.find((s) => s.valor === sexo)?.descricao ?? sexo;
  }

  voltar(): void {
    this.router.navigate(['/clientes']);
  }
}

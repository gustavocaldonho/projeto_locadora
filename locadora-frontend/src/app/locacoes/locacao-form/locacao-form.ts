import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { ClienteService, LocacaoService } from '../../core/atendimento-services';
import { MensagemService } from '../../core/mensagem-service';
import { Cliente, Item, Locacao, LocacaoRequest, TIPOS_ITEM } from '../../core/models';

/** Converte uma data para o formato yyyy-MM-dd usado pela API. */
function formatarData(data: Date): string {
  return data.toLocaleDateString('sv-SE');
}

@Component({
  selector: 'app-locacao-form',
  standalone: false,
  templateUrl: './locacao-form.html',
})
export class LocacaoForm implements OnInit {
  readonly hoje = formatarData(new Date());

  id?: number;
  locacao?: Locacao;
  clientes: Cliente[] = [];
  itens: Item[] = [];
  salvando = false;
  form;

  constructor(
    fb: FormBuilder,
    private service: LocacaoService,
    private clienteService: ClienteService,
    private mensagem: MensagemService,
    private route: ActivatedRoute,
    private router: Router,
  ) {
    this.form = fb.group({
      clienteId: [null as number | null, Validators.required],
      itemId: [null as number | null, Validators.required],
      valorCobrado: [null as number | null, [Validators.required, Validators.min(0)]],
      dtDevolucaoPrevista: ['', Validators.required],
      paga: [false],
    });
  }

  get editando(): boolean {
    return this.id !== undefined;
  }

  /** Item escolhido no formulário, para mostrar a classe e o prazo. */
  get itemSelecionado(): Item | undefined {
    return this.itens.find((i) => i.id === this.form.controls.itemId.value);
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.id = Number(id);
      // Na alteração, cliente e item não mudam
      this.form.controls.clienteId.disable();
      this.form.controls.itemId.disable();
      this.service.buscar(this.id).subscribe({
        next: (locacao) => {
          this.locacao = locacao;
          this.form.patchValue({
            clienteId: locacao.cliente.numInscricao,
            itemId: locacao.item.id,
            valorCobrado: locacao.valorCobrado,
            dtDevolucaoPrevista: locacao.dtDevolucaoPrevista,
            paga: locacao.paga,
          });
        },
        error: (erro: HttpErrorResponse) => {
          this.mensagem.erro(erro);
          this.voltar();
        },
      });
      return;
    }

    // Só clientes ativos e itens que não estão locados
    this.clienteService.listar().subscribe((lista) => (this.clientes = lista.filter((c) => c.estahAtivo)));
    this.service.itensDisponiveis().subscribe((lista) => (this.itens = lista));

    // Ao escolher o item, preenche o valor e a data prevista pela classe do título
    this.form.controls.itemId.valueChanges.subscribe(() => {
      const item = this.itemSelecionado;
      if (!item) {
        return;
      }
      const prevista = new Date();
      prevista.setDate(prevista.getDate() + item.titulo.classe.prazoDevolucao);
      this.form.patchValue({
        valorCobrado: item.titulo.classe.valor,
        dtDevolucaoPrevista: formatarData(prevista),
      });
    });
  }

  descricaoCliente(cliente: Cliente): string {
    return `${cliente.nome} (${cliente.tipo === 'SOCIO' ? 'sócio' : 'dependente'}, nº ${cliente.numInscricao})`;
  }

  descricaoItem(item: Item): string {
    const tipo = TIPOS_ITEM.find((t) => t.valor === item.tipoItem)?.descricao ?? item.tipoItem;
    return `${item.numSerie} – ${item.titulo.nome} (${tipo})`;
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const dados: LocacaoRequest = {
      clienteId: v.clienteId!,
      itemId: v.itemId!,
      valorCobrado: v.valorCobrado,
      dtDevolucaoPrevista: v.dtDevolucaoPrevista,
      paga: !!v.paga,
    };
    this.salvando = true;
    const requisicao = this.editando ? this.service.alterar(this.id!, dados) : this.service.criar(dados);
    requisicao.subscribe({
      next: () => {
        this.mensagem.sucesso(this.editando ? 'Locação alterada.' : 'Locação registrada.');
        this.voltar();
      },
      error: (erro: HttpErrorResponse) => {
        this.salvando = false;
        this.mensagem.erro(erro);
      },
    });
  }

  voltar(): void {
    this.router.navigate(['/locacoes']);
  }
}

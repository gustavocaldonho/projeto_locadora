import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MensagemService } from '../../core/mensagem-service';
import { Ator, Classe, Diretor, Titulo } from '../../core/models';
import { AtorService, ClasseService, DiretorService, TituloService } from '../../core/services';
import { FormBase } from '../../shared/form-base';

@Component({
  selector: 'app-titulo-form',
  standalone: false,
  templateUrl: './titulo-form.html',
})
export class TituloForm extends FormBase<Titulo> {
  form;

  // Opções dos campos de seleção, carregadas do backend
  atores: Ator[] = [];
  diretores: Diretor[] = [];
  classes: Classe[] = [];

  constructor(
    fb: FormBuilder,
    service: TituloService,
    mensagem: MensagemService,
    route: ActivatedRoute,
    router: Router,
    atorService: AtorService,
    diretorService: DiretorService,
    classeService: ClasseService,
  ) {
    super(service, mensagem, route, router);
    // Os selects guardam só os ids; o objeto completo é montado em montar()
    this.form = fb.group({
      nome: ['', [Validators.required, Validators.maxLength(150)]],
      ano: [null as number | null, [Validators.required, Validators.min(1888), Validators.max(2100)]],
      categoria: ['', [Validators.required, Validators.maxLength(50)]],
      sinopse: ['', Validators.maxLength(2000)],
      atoresIds: [[] as number[], Validators.required],
      diretorId: [null as number | null, Validators.required],
      classeId: [null as number | null, Validators.required],
    });
    atorService.listar().subscribe((lista) => (this.atores = lista));
    diretorService.listar().subscribe((lista) => (this.diretores = lista));
    classeService.listar().subscribe((lista) => (this.classes = lista));
  }

  protected override preencher(titulo: Titulo): void {
    this.form.patchValue({
      nome: titulo.nome,
      ano: titulo.ano,
      categoria: titulo.categoria,
      sinopse: titulo.sinopse ?? '',
      atoresIds: titulo.atores.map((a) => a.id!),
      diretorId: titulo.diretor.id!,
      classeId: titulo.classe.id!,
    });
  }

  protected override montar(): Titulo {
    const v = this.form.getRawValue();
    // O backend espera objetos com id: { atores: [{id: 1}], diretor: {id: 2}, classe: {id: 3} }
    return {
      id: this.id,
      nome: v.nome!,
      ano: v.ano!,
      categoria: v.categoria!,
      sinopse: v.sinopse ?? '',
      atores: v.atoresIds!.map((id) => ({ id }) as Ator),
      diretor: { id: v.diretorId! } as Diretor,
      classe: { id: v.classeId! } as Classe,
    };
  }
}

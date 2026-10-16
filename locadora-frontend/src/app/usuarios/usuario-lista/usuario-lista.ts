import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { MensagemService } from '../../core/mensagem-service';
import { PERFIS, Perfil, Usuario } from '../../core/models';
import { UsuarioService } from '../../core/services';
import { ListaBase } from '../../shared/lista-base';

@Component({
  selector: 'app-usuario-lista',
  standalone: false,
  templateUrl: './usuario-lista.html',
})
export class UsuarioLista extends ListaBase<Usuario> {
  readonly colunas = ['login', 'nome', 'perfil', 'acoes'];

  constructor(service: UsuarioService, mensagem: MensagemService, dialog: MatDialog) {
    super(service, mensagem, dialog);
  }

  descrever(x: Usuario): string {
    return `o usuário "${x.login}"`;
  }

  descricaoPerfil(perfil: Perfil): string {
    return PERFIS.find((p) => p.valor === perfil)?.descricao ?? perfil;
  }
}
